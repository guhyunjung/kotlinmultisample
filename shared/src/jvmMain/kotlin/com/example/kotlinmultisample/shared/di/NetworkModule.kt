package com.example.kotlinmultisample.shared.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * JVM Desktop 전용 네트워크 Koin 모듈
 *
 * Retrofit은 JVM 기반 라이브러리이므로 commonMain이 아닌
 * jvmMain에 정의합니다.
 *
 * ※ Android용 networkModule은 shared/androidMain/di/NetworkModule.kt에 별도 정의되어 있습니다.
 *
 * 사용법 (JVM Desktop main.kt):
 *   initKoin(additionalModules = jvmModules)
 */
val networkModule = module {

	/**
	 * OkHttpClient 싱글톤 등록
	 *
	 * - [HttpLoggingInterceptor]: 네트워크 요청/응답 로그를 출력합니다.
	 *   - BODY: 요청/응답의 헤더 + 바디를 모두 출력 (개발 환경에서 사용)
	 *   - 운영 환경에서는 NONE으로 변경하거나 BuildConfig로 분기하세요.
	 * - connectTimeout: 서버 연결 제한 시간 (30초)
	 * - readTimeout: 서버 응답 읽기 제한 시간 (30초)
	 * - writeTimeout: 서버로 데이터 전송 제한 시간 (30초)
	 */
	single<OkHttpClient> {
		val loggingInterceptor = HttpLoggingInterceptor().apply {
			// TODO: 운영 환경에서는 HttpLoggingInterceptor.Level.NONE 으로 변경하세요.
			level = HttpLoggingInterceptor.Level.BODY
		}

		OkHttpClient.Builder()
			.addInterceptor(loggingInterceptor) // 로깅 인터셉터 추가
			.connectTimeout(30, TimeUnit.SECONDS)
			.readTimeout(30, TimeUnit.SECONDS)
			.writeTimeout(30, TimeUnit.SECONDS)
			.build()
	}

	/**
	 * Retrofit 싱글톤 등록
	 *
	 * - baseUrl: API 서버의 기본 URL (반드시 '/'로 끝나야 합니다)
	 * - client: 위에서 등록한 OkHttpClient를 get()으로 자동 주입
	 * - GsonConverterFactory: JSON ↔ Kotlin 데이터 클래스 자동 변환
	 */
	single<Retrofit> {
		Retrofit.Builder()
			.baseUrl("https://api.example.com/") // TODO: 실제 API URL로 변경하세요.
			.client(get<OkHttpClient>())          // OkHttpClient 주입
			.addConverterFactory(GsonConverterFactory.create())
			.build()
	}

	// ─────────────────────────────────────────────────────────────
	// API Service 등록 예시
	// Retrofit 인스턴스를 get()으로 주입받아 Service 인터페이스를 구현체로 생성합니다.
	//
	// single<ProjectApiService> {
	//     get<Retrofit>().create(ProjectApiService::class.java)
	// }
	// ─────────────────────────────────────────────────────────────
}

