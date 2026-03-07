package com.example.kotlinmultisample.shared.di

import com.example.kotlinmultisample.shared.data.remote.api.ProjectApiService
import com.example.kotlinmultisample.shared.data.remote.datasource.RemoteProjectDataSource
import com.example.kotlinmultisample.shared.data.remote.datasource.RemoteProjectDataSourceImpl
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

	/**
	 * ProjectApiService 싱글톤 등록 (Android)
	 *
	 * Retrofit.create()를 통해 [ProjectApiService] 인터페이스의 구현체를
	 * 런타임에 자동 생성합니다.
	 * - get<Retrofit>(): 위에서 등록한 Retrofit 인스턴스를 자동 주입
	 */
	single<ProjectApiService> {
		get<Retrofit>().create(ProjectApiService::class.java)
	}

	/**
	 * RemoteProjectDataSource 싱글톤 등록 (Android)
	 *
	 * [RemoteProjectDataSourceImpl]에 [ProjectApiService]를 주입하여
	 * 원격 데이터 소스 구현체를 등록합니다.
	 * - get(): Koin이 [ProjectApiService] 인스턴스를 자동으로 찾아 주입합니다.
	 */
	single<RemoteProjectDataSource> {
		RemoteProjectDataSourceImpl(get())
	}
}

