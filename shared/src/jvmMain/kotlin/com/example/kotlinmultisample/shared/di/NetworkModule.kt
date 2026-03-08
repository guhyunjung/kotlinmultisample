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
 * JVM Desktop 전용 네트워크 Koin 모듈 (actual 구현)
 *
 * - projectRetrofit : "http://localhost:8080/" (로컬 Spring Boot 서버)
 * - countryRetrofit : "https://restcountries.com/" (REST Countries 공개 API)
 */
actual val networkModule = module {

    /** 공통 OkHttpClient 싱글톤 */
    single { buildOkHttpClient() }

    /**
     * Project API용 Retrofit (로컬 서버)
     * qualifier: "project"
     */
    single<Retrofit>(named("project")) {
        buildRetrofit(get(), "http://localhost:8080/")
    }

    /** ProjectApiService 싱글톤 등록 */
    single<ProjectApiService> {
        get<Retrofit>(named("project")).create(ProjectApiService::class.java)
    }

    /** RemoteProjectDataSource 싱글톤 등록 */
    single<RemoteProjectDataSource> { RemoteProjectDataSourceImpl(get()) }

    // ── Country (REST Countries API) ─────────────────────────────────────────

    /**
     * Country API용 Retrofit (REST Countries 공개 API)
     * qualifier: "country"
     * Android / JVM Desktop 모두 동일한 외부 URL을 사용합니다.
     */
    single<Retrofit>(named("country")) {
        buildRetrofit(get(), "https://restcountries.com/")
    }

    /** CountryApiService 싱글톤 등록 */
    single<CountryApiService> {
        get<Retrofit>(named("country")).create(CountryApiService::class.java)
    }

    /** RemoteCountryDataSource 싱글톤 등록 */
    single<RemoteCountryDataSource> { RemoteCountryDataSourceImpl(get()) }
}
