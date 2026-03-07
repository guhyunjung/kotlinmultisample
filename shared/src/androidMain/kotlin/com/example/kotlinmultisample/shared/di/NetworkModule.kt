package com.example.kotlinmultisample.shared.di

import com.example.kotlinmultisample.shared.data.remote.api.ProjectApiService
import com.example.kotlinmultisample.shared.data.remote.api.RestCountryApiService
import com.example.kotlinmultisample.shared.data.remote.datasource.RemoteProjectDataSource
import com.example.kotlinmultisample.shared.data.remote.datasource.RemoteProjectDataSourceImpl
import com.example.kotlinmultisample.shared.data.remote.datasource.RemoteRestCountryDataSource
import com.example.kotlinmultisample.shared.data.remote.datasource.RemoteRestCountryDataSourceImpl
import org.koin.dsl.module
import retrofit2.Retrofit

/**
 * Android 전용 네트워크 Koin 모듈 (actual 구현)
 *
 * - OkHttpClient / Retrofit 생성은 NetworkUtils.kt의 공통 함수를 재사용합니다.
 * - baseUrl: "http://10.0.2.2:8080/" (에뮬레이터 → 호스트 PC localhost)
 * - JVM Desktop actual: shared/jvmMain/di/NetworkModule.kt
 */
actual val networkModule = module {

	/**
	 * OkHttpClient 싱글톤 등록
	 * NetworkUtils.buildOkHttpClient() 공통 함수로 생성합니다.
	 */
	single { buildOkHttpClient() }

	/**
	 * Retrofit 싱글톤 등록
	 * NetworkUtils.buildRetrofit()에 Android용 baseUrl을 전달합니다.
	 *
	 * - 에뮬레이터  : "http://10.0.2.2:8080/" (10.0.2.2 = 호스트 PC의 localhost)
	 * - 실제 기기   : "http://192.168.x.x:8080/" (PC 로컬 IP)
	 * - 운영 환경   : "https://your-domain.com/" 으로 변경하세요.
	 */
	single<Retrofit> { buildRetrofit(get(), "http://10.0.2.2:8080/") }

	/**
	 * ProjectApiService 싱글톤 등록
	 * Retrofit.create()로 인터페이스 구현체를 자동 생성합니다.
	 */
	single<ProjectApiService> { get<Retrofit>().create(ProjectApiService::class.java) }

	/**
	 * RemoteProjectDataSource 싱글톤 등록
	 * ProjectApiService를 주입받아 실제 HTTP 요청을 수행합니다.
	 */
	single<RemoteProjectDataSource> { RemoteProjectDataSourceImpl(get()) }

	// ── Android 전용 API Service 추가 시 여기에 등록하세요 ───────────────────
	single<RestCountryApiService> { get<Retrofit>().create(RestCountryApiService::class.java) }
	single<RemoteRestCountryDataSource> { RemoteRestCountryDataSourceImpl(get()) }
}
