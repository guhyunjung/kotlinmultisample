package com.example.kotlinmultisample.shared.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Android 전용 네트워크 Koin 모듈
 *
 * Retrofit은 JVM 기반 라이브러리이므로 commonMain이 아닌
 * androidMain에 정의합니다.
 *
 * ※ JVM Desktop용 networkModule은 shared/jvmMain/di/NetworkModule.kt에 별도 정의되어 있습니다.
 *
 * 사용법 (Android Application):
 *   initKoin(additionalModules = androidModules)
 */
val networkModule = module {

	/**
	 * OkHttpClient 싱글톤 등록 (Android)
	 *
	 * - [HttpLoggingInterceptor]: 로그 출력
	 *   - BODY: 헤더 + 바디 모두 출력
	 * - Timeouts: 연결/읽기/쓰기 30초
	 */
	single<OkHttpClient> {
		val loggingInterceptor = HttpLoggingInterceptor().apply {
			// TODO: 운영(Release) 빌드에서는 NONE으로 변경하세요. (BuildConfig.DEBUG 활용 가능)
			level = HttpLoggingInterceptor.Level.BODY
		}

		OkHttpClient.Builder()
			.addInterceptor(loggingInterceptor)
			.connectTimeout(30, TimeUnit.SECONDS)
			.readTimeout(30, TimeUnit.SECONDS)
			.writeTimeout(30, TimeUnit.SECONDS)
			.build()
	}

	/**
	 * Retrofit 싱글톤 등록 (Android)
	 *
	 * - baseUrl: API 서버 기본 URL
	 * - client: OkHttpClient 주입
	 * - GsonConverterFactory: JSON 변환
	 */
	single<Retrofit> {
		Retrofit.Builder()
			.baseUrl("https://api.example.com/") // TODO: 실제 API URL로 변경하세요.
			.client(get<OkHttpClient>())
			.addConverterFactory(GsonConverterFactory.create())
			.build()
	}

	// ─────────────────────────────────────────────────────────────
	// API Service 등록 예시
	// single<ProjectApiService> {
	//     get<Retrofit>().create(ProjectApiService::class.java)
	// }
	// ─────────────────────────────────────────────────────────────
}

