package com.example.kotlinmultisample.shared.data.remote.datasource

import com.example.kotlinmultisample.shared.data.remote.dto.CountryDto

/**
 * 국가 정보 원격 데이터 소스 인터페이스
 *
 * REST Countries API 등 외부 API와의 통신을 담당합니다.
 * 실제 구현체는 jvmMain / androidMain에 정의합니다.
 */
interface RemoteRestCountryDataSource {

    /**
     * 전체 국가 목록 조회
     * @return 서버에서 반환된 [CountryDto] 목록
     */
    suspend fun getCountries(): List<CountryDto>

    /**
     * 특정 국가 코드로 단건 조회
     * @param code ISO alpha-2/alpha-3 국가 코드 (예: "CO", "COL")
     * @return 해당 국가의 [CountryDto]
     */
    suspend fun getCountryByCode(code: String): CountryDto?
}