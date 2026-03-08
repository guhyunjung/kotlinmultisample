package com.example.kotlinmultisample.shared.data.remote.datasource

import com.example.kotlinmultisample.shared.data.remote.api.CountryApiService
import com.example.kotlinmultisample.shared.data.remote.dto.CountryDto

/**
 * [RemoteCountryDataSource] Retrofit 구현체 (JVM Desktop 전용)
 *
 * REST Countries API (https://restcountries.com)를 통해
 * 국가 목록 및 단건 국가 정보를 가져옵니다.
 *
 * @property apiService Koin으로 주입받은 CountryApiService
 */
class RemoteCountryDataSourceImpl(
    private val apiService: CountryApiService
) : RemoteCountryDataSource {

    /**
     * 전체 국가 목록 조회
     *
     * fields 파라미터로 필요한 필드만 요청하여 응답 크기를 줄입니다.
     * cca2, cca3를 포함해야 LazyColumn의 고유 key로 사용할 수 있습니다.
     */
    override suspend fun getCountries(): List<CountryDto> {
        val fields = "name,capital,currencies,cca2,cca3,flag,region,flags"
        return apiService.getCountries(fields)
    }

    /**
     * 특정 코드로 국가 단건 조회
     *
     * REST Countries API는 단건도 List로 반환하므로 첫 번째 요소를 반환합니다.
     * 404 응답(국가 없음)은 null로 처리합니다.
     */
    override suspend fun getCountryByCode(code: String): CountryDto? {
        return try {
            apiService.getCountryByCode(code).firstOrNull()
        } catch (e: retrofit2.HttpException) {
            if (e.code() == 404) null else throw e
        }
    }
}

