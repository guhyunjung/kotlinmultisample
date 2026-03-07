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
 */
actual val networkModule = module {

	/** OkHttpClient 싱글톤 등록 */
	single { buildOkHttpClient() }

	/**
	 * Retrofit 싱글톤 등록 (JVM Desktop)
	 * - 로컬 개발: "http://localhost:8080/"
	 */
	single<Retrofit> { buildRetrofit(get(), "http://localhost:8080/") }

	/** ProjectApiService 싱글톤 등록 */
	single<ProjectApiService> { get<Retrofit>().create(ProjectApiService::class.java) }

	/** RemoteProjectDataSource 싱글톤 등록 */
	single<RemoteProjectDataSource> { RemoteProjectDataSourceImpl(get()) }

	// ── JVM Desktop 전용 API Service 추가 시 여기에 등록하세요 ──────────────
}
