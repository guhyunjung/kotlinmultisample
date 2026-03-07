package com.example.kotlinmultisample.shared.data.repository

import com.example.kotlinmultisample.shared.data.remote.datasource.RemoteCountryDataSource
import com.example.kotlinmultisample.shared.data.remote.dto.CountryDto
import com.example.kotlinmultisample.shared.data.remote.dto.NameDto
import com.example.kotlinmultisample.shared.data.remote.dto.toDomain
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * CountryRepositoryImpl 단위 테스트 (commonTest)
 *
 * - 실제 네트워크 없이 FakeDataSource로 빠르게 로직을 검증합니다.
 * - 모든 플랫폼(JVM, Android)에서 실행됩니다.
 *
 * KMP 테스트 핵심:
 * - kotlin.test: KMP 공식 테스트 라이브러리 (@Test, assertEquals 등)
 * - kotlinx.coroutines.test: suspend 함수 테스트용 runTest
 * - Mockito/MockK 사용 불가 (JVM 전용) → Fake 클래스로 대체
 */
class CountryRepositoryTest {

    // ── Fake DataSource ──────────────────────────────────────────────────────
    // commonTest에서는 MockK 등 JVM 전용 Mock 라이브러리를 사용할 수 없습니다.
    // 대신 인터페이스를 직접 구현한 Fake 클래스를 사용합니다.

    /**
     * 테스트용 가짜 국가 DTO 목록
     */
    private val fakeDtos = listOf(
        CountryDto(
            cca2 = "CO",
            cca3 = "COL",
            status = "officially-assigned",
            unMember = true,
            altSpellings = listOf("CO", "Republic of Colombia"),
            region = "Americas",
            landlocked = false,
            area = 1141748.0,
            latlng = listOf(4.0, -72.0),
            continents = listOf("South America"),
            population = 53057212,
            name = NameDto(
                common = "Colombia",
                official = "Republic of Colombia",
                nativeName = emptyMap()
            ),
            timezones = listOf("UTC-05:00")
        ),
        CountryDto(
            cca2 = "BR",
            cca3 = "BRA",
            status = "officially-assigned",
            unMember = true,
            altSpellings = listOf("BR", "Brasil"),
            region = "Americas",
            landlocked = false,
            area = 8515767.0,
            latlng = listOf(-10.0, -55.0),
            continents = listOf("South America"),
            population = 212559417,
            name = NameDto(
                common = "Brazil",
                official = "Federative Republic of Brazil",
                nativeName = emptyMap()
            ),
            timezones = listOf("UTC-05:00", "UTC-04:00", "UTC-03:00", "UTC-02:00")
        )
    )

    /**
     * 성공 케이스용 Fake DataSource
     * 미리 정의된 fakeDtos를 반환합니다.
     */
    private inner class FakeSuccessDataSource : RemoteCountryDataSource {
        override suspend fun getCountries(): List<CountryDto> = fakeDtos
        override suspend fun getCountryByCode(code: String): CountryDto? =
            fakeDtos.find { it.cca2 == code || it.cca3 == code }
    }

    /**
     * 빈 결과 반환 Fake DataSource
     */
    private inner class FakeEmptyDataSource : RemoteCountryDataSource {
        override suspend fun getCountries(): List<CountryDto> = emptyList()
        override suspend fun getCountryByCode(code: String): CountryDto? = null
    }

    /**
     * 예외 발생 Fake DataSource
     */
    private inner class FakeErrorDataSource : RemoteCountryDataSource {
        override suspend fun getCountries(): List<CountryDto> =
            throw RuntimeException("네트워크 오류")
        override suspend fun getCountryByCode(code: String): CountryDto? =
            throw RuntimeException("네트워크 오류")
    }

    // ── 테스트 케이스 ─────────────────────────────────────────────────────────

    /**
     * getCountries() - 정상적으로 국가 목록을 반환하는지 검증
     */
    @Test
    fun `getCountries - 국가 목록을 정상적으로 반환한다`() = runTest {
        // given
        val repository = CountryRepositoryImpl(FakeSuccessDataSource())

        // when
        val result = repository.getCountries()

        // then
        assertEquals(2, result.size)
        assertEquals("Colombia", result[0].name.common)
        assertEquals("Brazil", result[1].name.common)
    }

    /**
     * getCountries() - DTO가 도메인 모델로 올바르게 변환되는지 검증
     */
    @Test
    fun `getCountries - DTO가 도메인 모델로 올바르게 변환된다`() = runTest {
        // given
        val repository = CountryRepositoryImpl(FakeSuccessDataSource())

        // when
        val result = repository.getCountries()
        val colombia = result.first()

        // then: 주요 필드 변환 검증
        assertEquals("CO", colombia.cca2)
        assertEquals("COL", colombia.cca3)
        assertEquals("Americas", colombia.region)
        assertEquals(53057212L, colombia.population)
        assertEquals(listOf("South America"), colombia.continents)
    }

    /**
     * getCountries() - 빈 목록 반환 시 빈 리스트를 반환하는지 검증
     */
    @Test
    fun `getCountries - 빈 목록이면 빈 리스트를 반환한다`() = runTest {
        // given
        val repository = CountryRepositoryImpl(FakeEmptyDataSource())

        // when
        val result = repository.getCountries()

        // then
        assertEquals(0, result.size)
    }

    /**
     * getCountryByCode() - 존재하는 코드로 조회 시 해당 국가를 반환하는지 검증
     */
    @Test
    fun `getCountryByCode - 존재하는 코드로 조회하면 해당 국가를 반환한다`() = runTest {
        // given
        val repository = CountryRepositoryImpl(FakeSuccessDataSource())

        // when
        val result = repository.getCountryByCode("CO")

        // then
        assertNotNull(result)
        assertEquals("Colombia", result.name.common)
    }

    /**
     * getCountryByCode() - 존재하지 않는 코드 조회 시 null을 반환하는지 검증
     */
    @Test
    fun `getCountryByCode - 존재하지 않는 코드면 null을 반환한다`() = runTest {
        // given
        val repository = CountryRepositoryImpl(FakeSuccessDataSource())

        // when
        val result = repository.getCountryByCode("XX")

        // then
        assertNull(result)
    }

    /**
     * getCountries() - 네트워크 오류 발생 시 예외가 전파되는지 검증
     */
    @Test
    fun `getCountries - 네트워크 오류 발생 시 예외가 전파된다`() = runTest {
        // given
        val repository = CountryRepositoryImpl(FakeErrorDataSource())

        // when & then
        try {
            repository.getCountries()
            throw AssertionError("예외가 발생해야 합니다")
        } catch (e: RuntimeException) {
            assertEquals("네트워크 오류", e.message)
        }
    }
}

