package com.example.kotlinmultisample.shared.di

import com.example.kotlinmultisample.shared.data.remote.api.CountryApiService
import com.example.kotlinmultisample.shared.data.remote.api.StockApiService
import com.example.kotlinmultisample.shared.data.remote.datasource.NoopRemoteStockDataSource
import com.example.kotlinmultisample.shared.data.remote.datasource.RemoteCountryDataSource
import com.example.kotlinmultisample.shared.data.remote.datasource.RemoteCountryDataSourceImpl
import com.example.kotlinmultisample.shared.data.remote.datasource.RemoteStockDataSource
import com.example.kotlinmultisample.shared.data.remote.datasource.RemoteStockDataSourceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

/**
 * JVM Desktop 전용 네트워크 Koin 모듈 (actual 구현)
 *
 * - countryRetrofit : "https://restcountries.com/" (REST Countries 공개 API)
 */
actual val networkModule = module {

    /** 공통 OkHttpClient 싱글톤 */
    single { buildOkHttpClient() }

    /**
     * Country API용 Retrofit (REST Countries 공개 API)
     * qualifier: "country"
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
    
    // ── Stock API (공공데이터포털) 등록 예시
    // 실제 엔드포인트 경로와 serviceKey는 공공데이터포털 설정에 맞춰 수정하세요.
    single<Retrofit>(named("stock")) {
        // baseUrl은 반드시 '/'로 끝나야 합니다
        buildRetrofit(get(), "https://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/")
    }

    single {
        // StockApiService 생성
        get<Retrofit>(named("stock")).create(StockApiService::class.java)
    }

    // serviceKey(인증키)는 소스에 직접 하드코딩하지 말고 Gradle property / 환경변수 / local.properties를 통해 주입하세요.
    // 예: Koin 모듈을 초기화할 때 properties(mapOf("stockServiceKey" to "여기에키")) 형태로 전달하거나,
    // single(named("stockServiceKey")) { "${projectProperty}" } 식으로 등록하세요.
    single<RemoteStockDataSource> {
        val key: String? = getOrNull(named("stockServiceKey")) as? String
        if (key.isNullOrBlank()) {
            // 서비스키가 없으면 No-op 구현 사용
	        NoopRemoteStockDataSource()
        } else {
	        RemoteStockDataSourceImpl(get(), key)
        }
    }
}
