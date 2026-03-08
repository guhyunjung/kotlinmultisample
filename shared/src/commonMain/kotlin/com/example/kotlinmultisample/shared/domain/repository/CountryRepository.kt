package com.example.kotlinmultisample.shared.domain.repository

import com.example.kotlinmultisample.shared.domain.model.Country

/**
 * 국가 정보 Repository 인터페이스 (도메인 계층 소유)
 *
 * 도메인 계층은 이 인터페이스에만 의존하며,
 * 실제 구현(네트워크/캐시 등)은 data 계층의 CountryRepositoryImpl에서 담당합니다.
 */
interface CountryRepository {

    /**
     * 전체 국가 목록 조회 (Cache-First)
     *
     * 로컬 캐시가 있으면 즉시 반환, 없으면 API 호출 후 저장합니다.
     *
     * @return 도메인 모델 [Country] 목록
     */
    suspend fun getCountries(): List<Country>

    /**
     * 특정 국가 코드로 단건 조회
     *
     * 로컬 캐시 → 없으면 API 호출 순으로 진행합니다.
     *
     * @param code ISO alpha-2/alpha-3 국가 코드 (예: "CO", "COL")
     * @return 도메인 모델 [Country], 없으면 null
     */
    suspend fun getCountryByCode(code: String): Country?

    /**
     * 강제 갱신 - 항상 API를 호출하여 캐시를 최신 상태로 갱신합니다.
     *
     * Pull-to-Refresh 또는 수동 새로고침 시 사용합니다.
     *
     * @return 갱신된 [Country] 목록
     */
    suspend fun refreshCountries(): List<Country>
}
