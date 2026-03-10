package com.example.kotlinmultisample.shared.domain.usecase

import com.example.kotlinmultisample.shared.domain.model.Country
import com.example.kotlinmultisample.shared.domain.repository.CountryRepository

/**
 * 국가 목록 강제 갱신 UseCase
 *
 * ## 책임
 * - 로컬 캐시를 무시하고 항상 API를 호출하여 최신 데이터로 갱신합니다.
 * - Pull-to-Refresh 또는 수동 새로고침 기능에서 사용합니다.
 *
 * ## 사용 예시
 * ```kotlin
 * val updatedCountries = refreshCountriesUseCase()
 * ```
 *
 * @property repository 국가 데이터 Repository
 */
class RefreshCountriesUseCase(
    private val repository: CountryRepository
) {
    /**
     * @return 갱신된 [Country] 목록
     * @throws Exception 네트워크 오류 시
     */
    suspend operator fun invoke(): List<Country> = repository.refreshCountries()
}

