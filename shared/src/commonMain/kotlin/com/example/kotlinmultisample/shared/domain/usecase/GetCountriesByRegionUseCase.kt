package com.example.kotlinmultisample.shared.domain.usecase

import com.example.kotlinmultisample.shared.domain.model.Country

/**
 * 대륙별 국가 필터링 UseCase
 *
 * ## 책임
 * - 특정 대륙/지역 코드로 국가 목록을 필터링합니다.
 * - Repository 의존성 없이 도메인 모델 목록만을 입력받아 처리합니다.
 *
 * ## 지역 값 예시 (region)
 * - "Africa", "Americas", "Asia", "Europe", "Oceania", "Antarctic"
 *
 * ## 사용 예시
 * ```kotlin
 * val asianCountries = getCountriesByRegionUseCase(allCountries, "Asia")
 * val europeanCountries = getCountriesByRegionUseCase(allCountries, "Europe")
 * ```
 */
class GetCountriesByRegionUseCase {
    /**
     * @param countries 필터링 대상 전체 국가 목록
     * @param region 필터링할 지역명 (빈 문자열이면 전체 반환)
     * @return 해당 지역에 속하는 [Country] 목록 (인구 내림차순 정렬)
     */
    operator fun invoke(countries: List<Country>, region: String): List<Country> {
        if (region.isBlank()) return countries
        return countries
            .filter { it.region.equals(region, ignoreCase = true) }
            .sortedByDescending { it.population }
    }
}

