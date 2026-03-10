package com.example.kotlinmultisample.shared.domain.usecase

import com.example.kotlinmultisample.shared.domain.model.Country
import com.example.kotlinmultisample.shared.domain.repository.CountryRepository

/**
 * 국가 코드로 단건 조회 UseCase
 *
 * ## 책임
 * - ISO alpha-2(예: "KR") 또는 alpha-3(예: "KOR") 코드로 특정 국가를 조회합니다.
 * - 로컬 캐시 우선 조회 → 없으면 API 호출 전략을 사용합니다.
 *
 * ## 사용 예시
 * ```kotlin
 * val korea = getCountryByCodeUseCase("KR")
 * val korea = getCountryByCodeUseCase("KOR")
 * ```
 *
 * @property repository 국가 데이터 Repository
 */
class GetCountryByCodeUseCase(
    private val repository: CountryRepository
) {
    /**
     * @param code ISO alpha-2 또는 alpha-3 국가 코드
     * @return 해당 국가의 도메인 모델 [Country], 없으면 null
     * @throws Exception 네트워크 오류 시
     */
    suspend operator fun invoke(code: String): Country? = repository.getCountryByCode(code)
}

