package com.example.kotlinmultisample.shared.di

import com.example.kotlinmultisample.shared.data.remote.api.ProjectApiService
import com.example.kotlinmultisample.shared.data.remote.datasource.RemoteProjectDataSource
import com.example.kotlinmultisample.shared.data.remote.datasource.RemoteProjectDataSourceImpl
import org.koin.dsl.module
import retrofit2.Retrofit

/**
 * JVM Desktop 전용 네트워크 Koin 모듈 (actual 구현)
 *
 * - OkHttpClient / Retrofit 생성은 NetworkUtils.kt의 공통 함수를 재사용합니다.
 * - baseUrl: "http://localhost:8080/" (로컬 Spring Boot 서버)
 * - Android actual: shared/androidMain/di/NetworkModule.kt
 *
 * ※ CountryApiService는 Android 전용입니다.
 *   JVM Desktop에서 국가 정보 API가 필요한 경우 이 모듈에 추가하세요.
 */
actual val networkModule = module {

	/**
	 * OkHttpClient 싱글톤 등록
	 * NetworkUtils.buildOkHttpClient() 공통 함수로 생성합니다.
	 */
	single { buildOkHttpClient() }

	/**
	 * Retrofit 싱글톤 등록
	 * NetworkUtils.buildRetrofit()에 JVM Desktop용 baseUrl을 전달합니다.
	 *
	 * - 로컬 개발: "http://localhost:8080/"
	 * - 운영 환경: "https://your-domain.com/" 으로 변경하세요.
	 */
	single<Retrofit> { buildRetrofit(get(), "http://localhost:8080/") }

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

	// ── JVM Desktop 전용 API Service 추가 시 여기에 등록하세요 ──────────────
}
