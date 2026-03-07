package com.example.kotlinmultisample.shared.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * JVM / Android 공통 네트워크 유틸리티 함수
 *
 * OkHttpClient, Retrofit 생성 로직은 플랫폼(JVM/Android) 간에 동일하므로
 * 함수로 추출하여 각 actual NetworkModule에서 재사용합니다.
 *
 * - [buildOkHttpClient]: 로깅 인터셉터 + 타임아웃 설정이 포함된 OkHttpClient 생성
 * - [buildRetrofit]    : baseUrl을 인자로 받아 플랫폼별 URL 차이를 처리
 */

/**
 * OkHttpClient 생성 공통 함수
 *
 * - HttpLoggingInterceptor: 요청/응답 헤더+바디 로깅 (BODY 레벨)
 *   운영 환경에서는 Level.NONE 으로 변경하세요.
 * - connectTimeout / readTimeout / writeTimeout: 각 30초
 */
fun buildOkHttpClient(): OkHttpClient =
	OkHttpClient.Builder()
		.addInterceptor(
			HttpLoggingInterceptor().apply {
				// TODO: 운영 환경에서는 HttpLoggingInterceptor.Level.NONE 으로 변경하세요.
				level = HttpLoggingInterceptor.Level.BODY
			}
		)
		.connectTimeout(30, TimeUnit.SECONDS)
		.readTimeout(30, TimeUnit.SECONDS)
		.writeTimeout(30, TimeUnit.SECONDS)
		.build()

/**
 * Retrofit 생성 공통 함수
 *
 * baseUrl만 플랫폼별로 다르므로 파라미터로 주입받습니다.
 * - JVM Desktop : "http://localhost:8080/"
 * - Android 에뮬레이터: "http://10.0.2.2:8080/"
 * - 운영 환경   : "https://your-domain.com/"
 *
 * @param client OkHttpClient 인스턴스 (Koin에서 주입)
 * @param baseUrl API 서버 기본 URL (반드시 '/'로 끝나야 합니다)
 */
fun buildRetrofit(client: OkHttpClient, baseUrl: String): Retrofit =
	Retrofit.Builder()
		.baseUrl(baseUrl)
		.client(client)
		.addConverterFactory(GsonConverterFactory.create()) // JSON ↔ Kotlin 자동 변환
		.build()

