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
     * 전체 국가 목록 조회
     * @return 도메인 모델 [Country] 목록
     */
    suspend fun getCountries(): List<Country>

    /**
     * 특정 국가 코드로 단건 조회
     * @param code ISO alpha-2/alpha-3 국가 코드 (예: "CO", "COL")
     * @return 도메인 모델 [Country], 없으면 null
     */
    suspend fun getCountryByCode(code: String): Country?
}


