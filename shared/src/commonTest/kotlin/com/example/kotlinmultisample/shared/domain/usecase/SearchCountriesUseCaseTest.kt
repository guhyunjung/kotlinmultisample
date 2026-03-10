package com.example.kotlinmultisample.shared.domain.usecase

import com.example.kotlinmultisample.shared.domain.model.Country
import com.example.kotlinmultisample.shared.domain.model.CountryName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * SearchCountriesUseCase 단위 테스트 (commonTest)
 *
 * Repository/DB 의존성 없이 순수 비즈니스 로직만 검증합니다.
 * 모든 플랫폼(JVM, Android)에서 실행됩니다.
 */
class SearchCountriesUseCaseTest {

    private val useCase = SearchCountriesUseCase()

    // ── 테스트용 Fake Country 목록 ────────────────────────────────────────────

    private fun makeCountry(
        cca2: String,
        commonName: String,
        officialName: String,
        region: String = "Unknown",
        capital: List<String> = emptyList(),
        subregion: String? = null
    ) = Country(
        tld = emptyList(), cca2 = cca2, ccn3 = null, cca3 = "", cioc = null,
        independent = null, status = "officially-assigned", unMember = true,
        idd = null, capital = capital, altSpellings = emptyList(),
        region = region, subregion = subregion, landlocked = false,
        borders = emptyList(), area = 0.0, maps = null,
        latlng = emptyList(), continents = emptyList(), population = 0,
        gini = emptyMap(), fifa = null,
        name = CountryName(common = commonName, official = officialName, nativeName = emptyMap()),
        languages = emptyMap(), translations = emptyMap(), demonyms = null,
        currencies = emptyMap(), car = null, timezones = emptyList(),
        startOfWeek = null, capitalInfo = null, postalCode = null,
        flags = null, coatOfArms = null, flag = null
    )

    private val fakeCountries = listOf(
        makeCountry("KR", "South Korea", "Republic of Korea", "Asia", listOf("Seoul"), "Eastern Asia"),
        makeCountry("CO", "Colombia", "Republic of Colombia", "Americas", listOf("Bogotá"), "South America"),
        makeCountry("JP", "Japan", "Japan", "Asia", listOf("Tokyo"), "Eastern Asia"),
        makeCountry("DE", "Germany", "Federal Republic of Germany", "Europe", listOf("Berlin"), "Western Europe"),
        makeCountry("BR", "Brazil", "Federative Republic of Brazil", "Americas", listOf("Brasília"), "South America"),
    )

    // ── 테스트 케이스 ─────────────────────────────────────────────────────────

    @Test
    fun `빈 검색어는 전체 목록을 반환한다`() {
        val result = useCase(fakeCountries, "")
        assertEquals(fakeCountries.size, result.size)
    }

    @Test
    fun `공백만 있는 검색어는 전체 목록을 반환한다`() {
        val result = useCase(fakeCountries, "   ")
        assertEquals(fakeCountries.size, result.size)
    }

    @Test
    fun `영어 일반 명칭으로 검색한다`() {
        val result = useCase(fakeCountries, "Korea")
        assertEquals(1, result.size)
        assertEquals("KR", result.first().cca2)
    }

    @Test
    fun `영어 공식 명칭으로 검색한다`() {
        val result = useCase(fakeCountries, "Federal Republic")
        assertEquals(1, result.size)
        assertEquals("DE", result.first().cca2)
    }

    @Test
    fun `지역명(region)으로 검색한다`() {
        val result = useCase(fakeCountries, "Asia")
        assertEquals(2, result.size) // Korea, Japan
    }

    @Test
    fun `수도명으로 검색한다`() {
        val result = useCase(fakeCountries, "Tokyo")
        assertEquals(1, result.size)
        assertEquals("JP", result.first().cca2)
    }

    @Test
    fun `세부 지역(subregion)으로 검색한다`() {
        val result = useCase(fakeCountries, "South America")
        assertEquals(2, result.size) // Colombia, Brazil
    }

    @Test
    fun `대소문자 구분 없이 검색한다`() {
        val lowerResult = useCase(fakeCountries, "korea")
        val upperResult = useCase(fakeCountries, "KOREA")
        assertEquals(lowerResult.size, upperResult.size)
    }

    @Test
    fun `일치하는 결과가 없으면 빈 목록을 반환한다`() {
        val result = useCase(fakeCountries, "zzz_no_match")
        assertTrue(result.isEmpty())
    }
}

