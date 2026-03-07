package com.example.kotlinmultisample.shared.data.remote.api

import com.example.kotlinmultisample.shared.data.remote.dto.CountryDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * REST Countries API Service (JVM Desktop 전용)
 * https://restcountries.com 기반
 */
interface CountryApiService {

    /**
     * 전체 국가 목록 조회
     * GET /v3.1/all?fields=cca2,cca3,...
     *
     * @param fields 반환받을 필드 목록 (쉼표 구분 문자열)
     *   기본값으로 필요한 필드만 요청하여 응답 크기를 줄입니다.
     */
    @GET("v3.1/all")
    suspend fun getCountries(
        /**
         * encoded = true: 쉼표(,)를 %2C로 인코딩하지 않고 그대로 전송합니다.
         * REST Countries API는 fields=name,cca2 형식을 요구합니다.
         */
        @Query("fields", encoded = true) fields: String
    ): List<CountryDto>

    /**
     * 특정 국가 코드로 단건 조회
     * GET /v3.1/alpha/{code}
     * @param code ISO alpha-2/alpha-3 코드 (예: "CO", "COL")
     */
    @GET("v3.1/alpha/{code}")
    suspend fun getCountryByCode(@Path("code") code: String): List<CountryDto>
}




