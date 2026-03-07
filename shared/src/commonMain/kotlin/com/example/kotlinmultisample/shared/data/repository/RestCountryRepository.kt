package com.example.kotlinmultisample.shared.data.repository

import com.example.kotlinmultisample.shared.data.remote.datasource.RemoteRestCountryDataSource
import com.example.kotlinmultisample.shared.data.remote.dto.toDomain
import com.example.kotlinmultisample.shared.domain.model.Country

/**
 * 국가 정보 Repository 구현체
 *
 * [RemoteRestCountryDataSource]를 통해 원격 API에서 국가 데이터를 가져와
 * 도메인 모델 [Country]로 변환하여 반환합니다.
 *
 * @property remoteDataSource 원격 국가 데이터 소스
 */
class RestCountryRepository(
    private val remoteDataSource: RemoteRestCountryDataSource
) {

    /**
     * 전체 국가 목록 조회
     * @return 도메인 모델 [Country] 목록
     */
    suspend fun getCountries(): List<Country> {
        return remoteDataSource.getCountries().map { it.toDomain() }
    }

    /**
     * 특정 국가 코드로 단건 조회
     * @param code ISO alpha-2/alpha-3 국가 코드 (예: "CO", "COL")
     * @return 도메인 모델 [Country], 없으면 null
     */
    suspend fun getCountryByCode(code: String): Country? {
        return remoteDataSource.getCountryByCode(code)?.toDomain()
    }
}