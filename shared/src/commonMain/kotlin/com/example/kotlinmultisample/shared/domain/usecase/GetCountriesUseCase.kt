package com.example.kotlinmultisample.shared.domain.usecase

import com.example.kotlinmultisample.shared.domain.model.Country
import com.example.kotlinmultisample.shared.domain.repository.CountryRepository

/**
 * 전체 국가 목록 조회 UseCase (Cache-First)
 *
 * ## 책임
 * - Repository에서 국가 목록을 가져옵니다.
 * - 로컬 캐시가 있으면 즉시 반환, 없으면 API 호출 후 저장합니다.
 *
 * ## 사용 예시
 * ```kotlin
 * val countries = getCountriesUseCase()
 * ```
 *
 * @property repository 국가 데이터 Repository
 */
class GetCountriesUseCase(
    private val repository: CountryRepository
) {
    /**
     * @return 도메인 모델 [Country] 목록
     * @throws Exception 네트워크 오류 또는 캐시 조회 실패 시
     */
    suspend operator fun invoke(): List<Country> = repository.getCountries()
}

