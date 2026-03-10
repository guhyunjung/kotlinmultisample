package com.example.kotlinmultisample.shared.domain.usecase

import com.example.kotlinmultisample.shared.domain.model.Country
import com.example.kotlinmultisample.shared.domain.model.CountryName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * GetCountriesByRegionUseCase 단위 테스트 (commonTest)
 *
 * Repository/DB 의존성 없이 순수 비즈니스 로직만 검증합니다.
 */
class GetCountriesByRegionUseCaseTest {

    private val useCase = GetCountriesByRegionUseCase()

    private fun makeCountry(
        cca2: String,
        commonName: String,
        region: String,
        population: Long = 0
    ) = Country(
        tld = emptyList(), cca2 = cca2, ccn3 = null, cca3 = "", cioc = null,
        independent = null, status = "officially-assigned", unMember = true,
        idd = null, capital = emptyList(), altSpellings = emptyList(),
        region = region, subregion = null, landlocked = false,
        borders = emptyList(), area = 0.0, maps = null,
        latlng = emptyList(), continents = emptyList(), population = population,
        gini = emptyMap(), fifa = null,
        name = CountryName(common = commonName, official = commonName, nativeName = emptyMap()),
        languages = emptyMap(), translations = emptyMap(), demonyms = null,
        currencies = emptyMap(), car = null, timezones = emptyList(),
        startOfWeek = null, capitalInfo = null, postalCode = null,
        flags = null, coatOfArms = null, flag = null
    )

    private val fakeCountries = listOf(
        makeCountry("KR", "South Korea", "Asia", population = 51_000_000),
        makeCountry("JP", "Japan",        "Asia", population = 125_000_000),
        makeCountry("CN", "China",        "Asia", population = 1_400_000_000),
        makeCountry("DE", "Germany",      "Europe", population = 83_000_000),
        makeCountry("FR", "France",       "Europe", population = 67_000_000),
        makeCountry("BR", "Brazil",       "Americas", population = 212_000_000),
    )

    @Test
    fun `빈 지역명은 전체 목록을 반환한다`() {
        val result = useCase(fakeCountries, "")
        assertEquals(fakeCountries.size, result.size)
    }

    @Test
    fun `Asia 지역 국가를 반환한다`() {
        val result = useCase(fakeCountries, "Asia")
        assertEquals(3, result.size)
        assertTrue(result.all { it.region == "Asia" })
    }

    @Test
    fun `결과는 인구 내림차순으로 정렬된다`() {
        val result = useCase(fakeCountries, "Asia")
        // China(1.4B) > Japan(125M) > Korea(51M)
        assertEquals("CN", result[0].cca2)
        assertEquals("JP", result[1].cca2)
        assertEquals("KR", result[2].cca2)
    }

    @Test
    fun `대소문자 구분 없이 필터링한다`() {
        val lower = useCase(fakeCountries, "europe")
        val upper = useCase(fakeCountries, "EUROPE")
        assertEquals(lower.size, upper.size)
        assertEquals(2, lower.size)
    }

    @Test
    fun `일치하는 지역이 없으면 빈 목록을 반환한다`() {
        val result = useCase(fakeCountries, "Antarctica")
        assertTrue(result.isEmpty())
    }
}

