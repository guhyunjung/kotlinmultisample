package com.example.kotlinmultisample.shared.data.repository

import com.example.kotlinmultisample.shared.data.local.datasource.LocalCountryDataSource
import com.example.kotlinmultisample.shared.data.remote.datasource.RemoteCountryDataSource
import com.example.kotlinmultisample.shared.data.remote.dto.toDomain
import com.example.kotlinmultisample.shared.domain.model.Country
import com.example.kotlinmultisample.shared.domain.repository.CountryRepository
import co.touchlab.kermit.Logger

/**
 * [CountryRepository] 구현체 - Offline-First 캐시 전략
 *
 * ## 전략 개요
 *
 * ### getCountries() - Cache-First (캐시 우선)
 * ```
 * 1. 로컬 DB에 데이터가 있으면 즉시 반환 (빠른 UI 표시)
 * 2. 로컬 DB가 비어 있으면 API 호출 → DB에 저장 → 반환
 * ```
 *
 * ### getCountryByCode() - Remote-First with Local Fallback
 * ```
 * 1. 로컬 DB에서 먼저 조회
 * 2. 없으면 API 호출 → DB에 저장 → 반환
 * ```
 *
 * ### refreshCountries() - 강제 갱신
 * ```
 * API 호출 → DB 저장 → 반환
 * (항상 최신 데이터를 가져올 때 사용)
 * ```
 *
 * @property remoteDataSource 원격 데이터 소스 (Retrofit)
 * @property localDataSource  로컬 데이터 소스 (Room DB)
 */
class CountryRepositoryImpl(
    private val remoteDataSource: RemoteCountryDataSource,
    private val localDataSource: LocalCountryDataSource
) : CountryRepository {
    private val logger = Logger.withTag("CountryRepositoryImpl")

    /**
     * 전체 국가 목록 조회 (Cache-First)
     *
     * - 로컬 DB에 캐시가 있으면 즉시 반환합니다.
     * - 캐시가 없으면 API를 호출하고 결과를 DB에 저장한 뒤 반환합니다.
     *
     * @return 도메인 모델 [Country] 목록
     */
    override suspend fun getCountries(): List<Country> {
        logger.d { "getCountries() 호출" }
        // 1. 로컬 캐시 확인
        val cached = localDataSource.getCountries()
        if (cached.isNotEmpty()) {
            return cached
        }

        // 2. 캐시 없음 → API 호출 후 저장
        return fetchAndCacheCountries()
    }

    /**
     * 특정 국가 코드로 단건 조회 (Local-First with Remote Fallback)
     *
     * - 로컬 DB에 있으면 즉시 반환합니다.
     * - 없으면 API를 호출하고 결과를 DB에 저장한 뒤 반환합니다.
     *
     * @param code ISO alpha-2/alpha-3 국가 코드 (예: "CO", "COL")
     * @return 도메인 모델 [Country], 없으면 null
     */
    override suspend fun getCountryByCode(code: String): Country? {
        logger.d { "getCountryByCode(cca2=$code) 호출" }
        // 1. 로컬 캐시에서 alpha-2 코드로 조회
        val cached = localDataSource.getCountryByCode(code)
        if (cached != null) {
            return cached
        }

        // 2. 원격 API 호출
        val remote = remoteDataSource.getCountryByCode(code)?.toDomain()

        // 3. 결과가 있으면 로컬에 저장
        if (remote != null) {
            localDataSource.saveCountry(remote)
        }

        return remote
    }

    /**
     * 강제 갱신 - 항상 API를 호출하여 최신 데이터로 갱신합니다.
     *
     * Pull-to-Refresh, 수동 새로고침 시 사용합니다.
     * 기존 캐시는 Upsert로 갱신되므로 별도 삭제가 필요하지 않습니다.
     *
     * @return 갱신된 [Country] 목록
     */
    override suspend fun refreshCountries(): List<Country> {
        logger.d { "refreshCountries() 호출" }
        return fetchAndCacheCountries()
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 내부 헬퍼
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * API에서 국가 목록을 가져와 로컬 DB에 저장하고 반환합니다.
     *
     * 네트워크 오류 시 예외를 그대로 전파합니다.
     * 호출하는 쪽(ViewModel)에서 예외를 처리하도록 설계합니다.
     */
    private suspend fun fetchAndCacheCountries(): List<Country> {
        val countries = remoteDataSource.getCountries().map { it.toDomain() }
        // Upsert: 기존 데이터는 갱신, 신규 데이터는 삽입
        localDataSource.saveCountries(countries)
        return countries
    }
}
