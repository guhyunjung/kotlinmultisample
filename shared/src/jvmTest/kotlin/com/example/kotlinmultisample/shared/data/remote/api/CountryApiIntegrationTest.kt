package com.example.kotlinmultisample.shared.data.remote.api

import com.example.kotlinmultisample.shared.data.remote.datasource.RemoteCountryDataSourceImpl
import com.example.kotlinmultisample.shared.data.repository.CountryRepositoryImpl
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * REST Countries API 통합 테스트 (jvmTest)
 *
 * 실제 네트워크를 통해 https://restcountries.com API를 호출합니다.
 * JVM 환경에서만 실행됩니다 (Android/JS에서는 실행 불가).
 *
 * 실행 방법:
 *   ./gradlew :shared:jvmTest
 *
 * 주의:
 * - 인터넷 연결이 필요합니다.
 * - CI/CD에서는 @Ignore로 비활성화하거나 별도 태그로 분리하세요.
 * - 외부 API 의존으로 인해 API 서버 상태에 따라 실패할 수 있습니다.
 */
class CountryApiIntegrationTest {

    // ── 실제 Retrofit 인스턴스 구성 ──────────────────────────────────────────

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor(::println).apply {
                level = HttpLoggingInterceptor.Level.BASIC // 요청/응답 URL만 출력
            }
        )
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://restcountries.com/") // 실제 REST Countries API
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**
     * 테스트 대상: 실제 CountryApiService → DataSourceImpl → RepositoryImpl 체인
     */
    private val apiService = retrofit.create(CountryApiService::class.java)
    private val dataSource = RemoteCountryDataSourceImpl(apiService)
    private val repository = CountryRepositoryImpl(dataSource)

    // ── 테스트 케이스 ─────────────────────────────────────────────────────────


    /**
     * 특정 국가 코드(COL = 콜롬비아) API 호출 검증
     */
    @Test
    fun `실제 API - 콜롬비아를 코드로 조회한다`() = runTest {
        // when
        val country = repository.getCountryByCode("COL")

        // then
        assertNotNull(country, "콜롬비아가 조회되어야 합니다")
        println("조회된 국가: ${country.name.common}")
        println("  수도: ${country.capital}")
        println("  인구: ${country.population}")
        println("  지역: ${country.region} / ${country.subregion}")
        println("  통화: ${country.currencies}")
        println("  언어: ${country.languages}")

        assertTrue(
            country.name.common.contains("Colombia", ignoreCase = true),
            "국가명에 Colombia가 포함되어야 합니다"
        )
    }

    /**
     * 한국(KOR) API 호출 검증
     */
    @Test
    fun `실제 API - 한국을 코드로 조회한다`() = runTest {
        // when
        val country = repository.getCountryByCode("KOR")

        // then
        assertNotNull(country, "한국이 조회되어야 합니다")
        println("조회된 국가: ${country.name.common}")
        println("  한국어 이름: ${country.name.nativeName}")
        println("  수도: ${country.capital}")
        println("  인구: ${country.population}")
        println("  타임존: ${country.timezones}")

        assertTrue(
            country.name.common.contains("Korea", ignoreCase = true),
            "국가명에 Korea가 포함되어야 합니다"
        )
    }

    /**
     * 존재하지 않는 코드 조회 시 null 반환 검증
     */
    @Test
    fun `실제 API - 존재하지 않는 코드는 null을 반환한다`() = runTest {
        // when
        val country = repository.getCountryByCode("ZZZ")

        // then
        assertTrue(country == null, "존재하지 않는 코드는 null이어야 합니다")
        println("ZZZ 코드 조회 결과: null (정상)")
    }
}


