package com.example.kotlinmultisample.shared.domain.usecase

import com.example.kotlinmultisample.shared.domain.model.Country

/**
 * 국가 검색 UseCase
 *
 * ## 책임
 * - 주어진 검색어로 국가 목록을 필터링하는 **순수 비즈니스 로직**을 담당합니다.
 * - Repository 의존성 없이 도메인 모델 목록만을 입력받아 처리합니다.
 * - 현재 ViewModel에 분산되어 있던 검색 로직을 도메인 계층으로 이동합니다.
 *
 * ## 검색 기준 (우선순위 순)
 * 1. 영어 일반 명칭 (예: "Korea")
 * 2. 영어 공식 명칭 (예: "Republic of Korea")
 * 3. 원어 명칭 (예: "한국")
 * 4. 지역/대륙 (예: "Asia")
 * 5. 수도 (예: "Seoul")
 *
 * ## 사용 예시
 * ```kotlin
 * val results = searchCountriesUseCase(allCountries, "korea")
 * val results = searchCountriesUseCase(allCountries, "")  // 빈 문자열 → 전체 반환
 * ```
 */
class SearchCountriesUseCase {
    /**
     * @param countries 검색 대상 전체 국가 목록
     * @param query 검색어 (빈 문자열이면 전체 반환)
     * @return 검색어와 일치하는 [Country] 목록
     */
    operator fun invoke(countries: List<Country>, query: String): List<Country> {
        if (query.isBlank()) return countries
        return countries.filter { country ->
            country.name.common.contains(query, ignoreCase = true) ||
            country.name.official.contains(query, ignoreCase = true) ||
            country.name.nativeName.values.any {
                it.common.contains(query, ignoreCase = true) ||
                it.official.contains(query, ignoreCase = true)
            } ||
            country.region.contains(query, ignoreCase = true) ||
            (country.subregion?.contains(query, ignoreCase = true) == true) ||
            country.capital.any { it.contains(query, ignoreCase = true) }
        }
    }
}

