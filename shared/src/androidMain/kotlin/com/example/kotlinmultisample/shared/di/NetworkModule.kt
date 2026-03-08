package com.example.kotlinmultisample.shared.di

import com.example.kotlinmultisample.shared.data.remote.api.CountryApiService
import com.example.kotlinmultisample.shared.data.remote.datasource.RemoteCountryDataSource
import com.example.kotlinmultisample.shared.data.remote.datasource.RemoteCountryDataSourceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

/**
 * Android 전용 네트워크 Koin 모듈 (actual 구현)
 *
 * - countryRetrofit : "https://restcountries.com/" (REST Countries 외부 API)
 */
actual val networkModule = module {

    /** 공통 OkHttpClient 싱글톤 */
    single { buildOkHttpClient() }

    /**
     * Country API용 Retrofit (REST Countries 외부 API)
     * qualifier: "country"
     */
    single<Retrofit>(named("country")) {
        buildRetrofit(get(), "https://restcountries.com/")
    }

    /** CountryApiService - country Retrofit 사용 */
    single<CountryApiService> {
        get<Retrofit>(named("country")).create(CountryApiService::class.java)
    }

    /** RemoteCountryDataSource 싱글톤 등록 */
    single<RemoteCountryDataSource> { RemoteCountryDataSourceImpl(get()) }
}
