package com.example.kotlinmultisample.shared.data.repository

import com.example.kotlinmultisample.shared.data.remote.datasource.RemoteCountryDataSource
import com.example.kotlinmultisample.shared.data.remote.dto.toDomain
import com.example.kotlinmultisample.shared.domain.model.Country
import com.example.kotlinmultisample.shared.domain.repository.CountryRepository

/**
 * [CountryRepository] 구현체
 *
 * [RemoteCountryDataSource]를 통해 원격 API에서 국가 데이터를 가져와
 * 도메인 모델 [Country]로 변환하여 반환합니다.
 *
 * @property remoteDataSource 원격 국가 데이터 소스
 */
class CountryRepositoryImpl(
    private val remoteDataSource: RemoteCountryDataSource
) : CountryRepository {

    /**
     * 전체 국가 목록 조회
     * @return 도메인 모델 [Country] 목록
     */
    override suspend fun getCountries(): List<Country> {
        return remoteDataSource.getCountries().map { it.toDomain() }
    }

    /**
     * 특정 국가 코드로 단건 조회
     * @param code ISO alpha-2/alpha-3 국가 코드 (예: "CO", "COL")
     * @return 도메인 모델 [Country], 없으면 null
     */
    override suspend fun getCountryByCode(code: String): Country? {
        return remoteDataSource.getCountryByCode(code)?.toDomain()
    }
}

