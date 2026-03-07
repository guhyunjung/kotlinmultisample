package com.example.kotlinmultisample.shared.data.remote.datasource

import com.example.kotlinmultisample.shared.data.remote.api.CountryApiService
import com.example.kotlinmultisample.shared.data.remote.dto.CountryDto

/**
 * [RemoteCountryDataSource] Retrofit 구현체 (Android 전용)
 *
 * @property apiService Koin으로 주입받은 CountryApiService
 */
class RemoteCountryDataSourceImpl(
    private val apiService: CountryApiService
) : RemoteCountryDataSource {

    /** 전체 국가 목록 조회 */
    override suspend fun getCountries(): List<CountryDto> {
        return apiService.getCountries()
    }

    /**
     * 특정 코드로 국가 단건 조회
     * REST Countries API는 단건도 List로 반환하므로 첫 번째 요소를 반환합니다.
     */
    override suspend fun getCountryByCode(code: String): CountryDto? {
        return try {
            apiService.getCountryByCode(code).firstOrNull()
        } catch (e: retrofit2.HttpException) {
            if (e.code() == 404) null else throw e
        }
    }
}