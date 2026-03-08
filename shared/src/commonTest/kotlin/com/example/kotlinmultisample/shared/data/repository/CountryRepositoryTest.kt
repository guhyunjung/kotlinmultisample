package com.example.kotlinmultisample.shared.data.repository

import com.example.kotlinmultisample.shared.data.local.datasource.LocalCountryDataSource
import com.example.kotlinmultisample.shared.data.remote.datasource.RemoteCountryDataSource
import com.example.kotlinmultisample.shared.data.remote.dto.CountryDto
import com.example.kotlinmultisample.shared.data.remote.dto.NameDto
import com.example.kotlinmultisample.shared.data.remote.dto.toDomain
import com.example.kotlinmultisample.shared.domain.model.Country
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * CountryRepositoryImpl 단위 테스트 (commonTest)
 *
 * - 실제 네트워크/DB 없이 Fake 구현체로 로직을 검증합니다.
 * - 모든 플랫폼(JVM, Android)에서 실행됩니다.
 *
 * Offline-First 전략 검증:
 * - 로컬 캐시가 있으면 원격 호출 없이 캐시 반환
 * - 캐시가 없으면 원격 호출 후 저장
 * - refreshCountries()는 항상 원격 호출
 */
class CountryRepositoryTest {

    // ── 테스트용 Fake DTO 목록 ─────────────────────────────────────────────────

    private val fakeDtos = listOf(
        CountryDto(
            cca2 = "CO", cca3 = "COL",
            status = "officially-assigned", unMember = true,
            altSpellings = listOf("CO", "Republic of Colombia"),
            region = "Americas", landlocked = false,
            area = 1141748.0, latlng = listOf(4.0, -72.0),
            continents = listOf("South America"), population = 53057212,
            name = NameDto(common = "Colombia", official = "Republic of Colombia", nativeName = emptyMap()),
            timezones = listOf("UTC-05:00")
        ),
        CountryDto(
            cca2 = "BR", cca3 = "BRA",
            status = "officially-assigned", unMember = true,
            altSpellings = listOf("BR", "Brasil"),
            region = "Americas", landlocked = false,
            area = 8515767.0, latlng = listOf(-10.0, -55.0),
            continents = listOf("South America"), population = 212559417,
            name = NameDto(common = "Brazil", official = "Federative Republic of Brazil", nativeName = emptyMap()),
            timezones = listOf("UTC-05:00", "UTC-04:00")
        )
    )

    // ── Fake Remote DataSource ────────────────────────────────────────────────

    /** 성공 케이스: 미리 정의된 fakeDtos 반환 */
    private inner class FakeSuccessRemote : RemoteCountryDataSource {
        var callCount = 0
        override suspend fun getCountries(): List<CountryDto> { callCount++; return fakeDtos }
        override suspend fun getCountryByCode(code: String): CountryDto? =
            fakeDtos.find { it.cca2 == code || it.cca3 == code }
    }

    /** 빈 결과 반환 */
    private inner class FakeEmptyRemote : RemoteCountryDataSource {
        override suspend fun getCountries(): List<CountryDto> = emptyList()
        override suspend fun getCountryByCode(code: String): CountryDto? = null
    }

    /** 예외 발생 */
    private inner class FakeErrorRemote : RemoteCountryDataSource {
        override suspend fun getCountries(): List<CountryDto> = throw RuntimeException("네트워크 오류")
        override suspend fun getCountryByCode(code: String): CountryDto? = throw RuntimeException("네트워크 오류")
    }

    // ── Fake Local DataSource (In-Memory) ────────────────────────────────────

    /**
     * 인메모리 로컬 캐시 Fake - Room DB 역할을 합니다.
     * 테스트에서 실제 저장/조회 동작을 시뮬레이션합니다.
     */
    private inner class FakeLocalDataSource(
        initialData: List<Country> = emptyList()
    ) : LocalCountryDataSource {
        // 인메모리 저장소
        private val store = initialData.toMutableList()

        override suspend fun getCountries(): List<Country> = store.toList()
        override suspend fun getCountryByCode(cca2: String): Country? =
            store.find { it.cca2 == cca2 }
        override suspend fun saveCountries(countries: List<Country>) {
            // 기존 데이터를 Upsert 방식으로 갱신
            countries.forEach { new ->
                val idx = store.indexOfFirst { it.cca2 == new.cca2 }
                if (idx >= 0) store[idx] = new else store.add(new)
            }
        }
        override suspend fun saveCountry(country: Country) {
            val idx = store.indexOfFirst { it.cca2 == country.cca2 }
            if (idx >= 0) store[idx] = country else store.add(country)
        }
        override suspend fun clearAll() = store.clear()
    }

    // ── 헬퍼: Repository 생성 ─────────────────────────────────────────────────

    private fun makeRepo(
        remote: RemoteCountryDataSource,
        local: LocalCountryDataSource = FakeLocalDataSource()
    ) = CountryRepositoryImpl(remote, local)

    // ── 테스트 케이스 ─────────────────────────────────────────────────────────

    /**
     * 캐시가 비어 있을 때 원격 API를 호출하고 결과를 반환한다
     */
    @Test
    fun `getCountries - 캐시 없으면 원격 호출 후 반환한다`() = runTest {
        val remote = FakeSuccessRemote()
        val repo = makeRepo(remote)

        val result = repo.getCountries()

        assertEquals(2, result.size)
        assertEquals("Colombia", result[0].name.common)
        assertEquals(1, remote.callCount) // 원격이 1회 호출됐는지 확인
    }

    /**
     * 캐시가 있을 때 원격 API를 호출하지 않고 캐시를 반환한다 (Cache-First)
     */
    @Test
    fun `getCountries - 캐시 있으면 원격 호출 없이 캐시 반환한다`() = runTest {
        val remote = FakeSuccessRemote()
        // 미리 로컬에 데이터를 채워둔다
        val preloaded = fakeDtos.map { it.toDomain() }
        val local = FakeLocalDataSource(preloaded)
        val repo = makeRepo(remote, local)

        val result = repo.getCountries()

        assertEquals(2, result.size)
        assertEquals(0, remote.callCount) // 원격이 호출되지 않아야 함
    }

    /**
     * getCountries() - DTO가 도메인 모델로 올바르게 변환되는지 검증
     */
    @Test
    fun `getCountries - DTO가 도메인 모델로 올바르게 변환된다`() = runTest {
        val repo = makeRepo(FakeSuccessRemote())

        val result = repo.getCountries()
        val colombia = result.first()

        assertEquals("CO", colombia.cca2)
        assertEquals("COL", colombia.cca3)
        assertEquals("Americas", colombia.region)
        assertEquals(53057212L, colombia.population)
        assertEquals(listOf("South America"), colombia.continents)
    }

    /**
     * getCountries() - API 결과가 로컬 DB에 저장되는지 검증
     */
    @Test
    fun `getCountries - API 결과가 로컬에 저장된다`() = runTest {
        val local = FakeLocalDataSource()
        val repo = makeRepo(FakeSuccessRemote(), local)

        repo.getCountries()

        // 저장 후 로컬에서 바로 꺼낼 수 있어야 함
        val cached = local.getCountries()
        assertEquals(2, cached.size)
    }

    /**
     * getCountries() - 빈 목록 반환 시 빈 리스트를 반환하는지 검증
     */
    @Test
    fun `getCountries - 빈 목록이면 빈 리스트를 반환한다`() = runTest {
        val repo = makeRepo(FakeEmptyRemote())

        val result = repo.getCountries()

        assertEquals(0, result.size)
    }

    /**
     * refreshCountries() - 캐시가 있어도 항상 원격 API를 호출한다
     */
    @Test
    fun `refreshCountries - 캐시 있어도 항상 원격 호출한다`() = runTest {
        val remote = FakeSuccessRemote()
        val preloaded = fakeDtos.map { it.toDomain() }
        val local = FakeLocalDataSource(preloaded)
        val repo = makeRepo(remote, local)

        val result = repo.refreshCountries()

        assertEquals(2, result.size)
        assertEquals(1, remote.callCount) // 캐시 있어도 원격이 1회 호출돼야 함
    }

    /**
     * getCountryByCode() - 로컬 캐시에 있으면 캐시에서 반환한다
     */
    @Test
    fun `getCountryByCode - 캐시에 있으면 캐시에서 반환한다`() = runTest {
        val remote = FakeSuccessRemote()
        val preloaded = fakeDtos.map { it.toDomain() }
        val local = FakeLocalDataSource(preloaded)
        val repo = makeRepo(remote, local)

        val result = repo.getCountryByCode("CO")

        assertNotNull(result)
        assertEquals("Colombia", result.name.common)
        assertEquals(0, remote.callCount) // 원격 미호출
    }

    /**
     * getCountryByCode() - 캐시에 없으면 원격 API를 호출한다
     */
    @Test
    fun `getCountryByCode - 캐시 없으면 원격 호출한다`() = runTest {
        val remote = FakeSuccessRemote()
        val repo = makeRepo(remote) // 로컬 비어있음

        val result = repo.getCountryByCode("CO")

        assertNotNull(result)
        assertEquals("Colombia", result.name.common)
    }

    /**
     * getCountryByCode() - 존재하지 않는 코드 조회 시 null을 반환한다
     */
    @Test
    fun `getCountryByCode - 존재하지 않는 코드면 null을 반환한다`() = runTest {
        val repo = makeRepo(FakeSuccessRemote())

        val result = repo.getCountryByCode("XX")

        assertNull(result)
    }

    /**
     * getCountries() - 네트워크 오류 발생 시 예외가 전파된다
     */
    @Test
    fun `getCountries - 네트워크 오류 발생 시 예외가 전파된다`() = runTest {
        val repo = makeRepo(FakeErrorRemote())

        try {
            repo.getCountries()
            throw AssertionError("예외가 발생해야 합니다")
        } catch (e: RuntimeException) {
            assertEquals("네트워크 오류", e.message)
        }
    }
}

