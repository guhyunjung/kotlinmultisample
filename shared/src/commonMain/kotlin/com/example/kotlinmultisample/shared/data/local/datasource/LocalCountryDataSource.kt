package com.example.kotlinmultisample.shared.data.local.datasource

import com.example.kotlinmultisample.shared.domain.model.Country

/**
 * 국가 정보 로컬 데이터 소스 인터페이스 (공통)
 *
 * Room DB를 추상화한 인터페이스입니다.
 * commonMain에 인터페이스를 선언하고,
 * 실제 Room 구현체는 androidMain / jvmMain에 정의합니다.
 *
 * Offline-First 전략에서 사용:
 *  1. 앱 시작 시 로컬 캐시가 있으면 즉시 반환 (빠른 응답)
 *  2. 원격 API 결과를 로컬에 저장하여 다음 사용 시 캐시 활용
 */
interface LocalCountryDataSource {

    /**
     * 로컬 DB에서 전체 국가 목록 조회
     *
     * @return 캐시된 [Country] 목록. 데이터가 없으면 빈 리스트 반환.
     */
    suspend fun getCountries(): List<Country>

    /**
     * 로컬 DB에서 특정 국가 코드로 단건 조회
     *
     * @param cca2 ISO alpha-2 국가 코드 (예: "CO")
     * @return 캐시된 [Country], 없으면 null
     */
    suspend fun getCountryByCode(cca2: String): Country?

    /**
     * 국가 목록을 로컬 DB에 저장 (Upsert)
     *
     * 동일한 cca2가 이미 있으면 갱신, 없으면 삽입합니다.
     * API에서 받아온 전체 목록을 동기화할 때 사용합니다.
     *
     * @param countries 저장할 [Country] 목록
     */
    suspend fun saveCountries(countries: List<Country>)

    /**
     * 특정 국가를 로컬 DB에 저장 (Upsert)
     *
     * @param country 저장할 [Country]
     */
    suspend fun saveCountry(country: Country)

    /**
     * 로컬 DB의 모든 국가 데이터 삭제 (캐시 초기화)
     *
     * 강제 새로고침이 필요할 때 사용합니다.
     */
    suspend fun clearAll()
}

