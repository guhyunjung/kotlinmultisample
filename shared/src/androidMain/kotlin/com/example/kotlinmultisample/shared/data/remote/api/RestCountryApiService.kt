package com.example.kotlinmultisample.shared.data.remote.api

import com.example.kotlinmultisample.shared.data.remote.dto.CountryDto
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * REST Countries API Service (Android 전용)
 * https://restcountries.com 기반
 */
interface RestCountryApiService {

    /**
     * 전체 국가 목록 조회
     * GET /v3.1/all
     */
    @GET("v3.1/all")
    suspend fun getCountries(): List<CountryDto>

    /**
     * 특정 국가 코드로 단건 조회
     * GET /v3.1/alpha/{code}
     * @param code ISO alpha-2/alpha-3 코드 (예: "CO", "COL")
     */
    @GET("v3.1/alpha/{code}")
    suspend fun getCountryByCode(@Path("code") code: String): List<CountryDto>
}