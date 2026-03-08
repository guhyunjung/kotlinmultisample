package com.example.kotlinmultisample.shared.di

import com.example.kotlinmultisample.shared.data.remote.api.CountryApiService
import com.example.kotlinmultisample.shared.data.remote.api.ProjectApiService
import com.example.kotlinmultisample.shared.data.remote.datasource.RemoteCountryDataSource
import com.example.kotlinmultisample.shared.data.remote.datasource.RemoteCountryDataSourceImpl
import com.example.kotlinmultisample.shared.data.remote.datasource.RemoteProjectDataSource
import com.example.kotlinmultisample.shared.data.remote.datasource.RemoteProjectDataSourceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

/**
 * Android 전용 네트워크 Koin 모듈 (actual 구현)
 *
 * ProjectApiService와 CountryApiService는 baseUrl이 다르므로
 * Retrofit 인스턴스를 qualifier로 분리합니다.
 *
 * - projectRetrofit : "http://10.0.2.2:8080/" (에뮬레이터 → 로컬 Spring Boot)
 * - countryRetrofit : "https://restcountries.com/" (외부 REST Countries API)
 */
actual val networkModule = module {

    /** 공통 OkHttpClient 싱글톤 */
    single { buildOkHttpClient() }

    /**
     * Project API용 Retrofit (로컬 서버)
     * qualifier: "project"
     */
    single<Retrofit>(named("project")) {
        buildRetrofit(get(), "http://10.0.2.2:8080/")
    }

    /**
     * Country API용 Retrofit (REST Countries 외부 API)
     * qualifier: "country"
     */
    single<Retrofit>(named("country")) {
        buildRetrofit(get(), "https://restcountries.com/")
    }

    /** ProjectApiService - project Retrofit 사용 */
    single<ProjectApiService> {
        get<Retrofit>(named("project")).create(ProjectApiService::class.java)
    }

    /** RemoteProjectDataSource 싱글톤 등록 */
    single<RemoteProjectDataSource> { RemoteProjectDataSourceImpl(get()) }

    /** CountryApiService - country Retrofit 사용 */
    single<CountryApiService> {
        get<Retrofit>(named("country")).create(CountryApiService::class.java)
    }

    /** RemoteCountryDataSource 싱글톤 등록 */
    single<RemoteCountryDataSource> { RemoteCountryDataSourceImpl(get()) }
}
