package com.example.kotlinmultisample.shared.data.local.datasource

import com.example.kotlinmultisample.shared.data.local.database.CountryDao
import com.example.kotlinmultisample.shared.data.local.database.toDomain
import com.example.kotlinmultisample.shared.data.local.database.toEntity
import com.example.kotlinmultisample.shared.domain.model.Country

/**
 * [LocalCountryDataSource] Room 구현체 (Android 전용)
 *
 * Room의 [CountryDao]를 통해 로컬 DB에 국가 데이터를 저장하고 조회합니다.
 * Koin에서 [CountryDao]를 주입받습니다.
 *
 * @property dao Room DAO (Koin 주입)
 */
class LocalCountryDataSourceImpl(
    private val dao: CountryDao
) : LocalCountryDataSource {

    /**
     * 로컬 DB에서 전체 국가 목록 조회
     *
     * Entity → 도메인 모델로 변환하여 반환합니다.
     */
    override suspend fun getCountries(): List<Country> =
        dao.getAll().map { it.toDomain() }

    /**
     * 특정 코드로 국가 단건 조회
     *
     * @param cca2 ISO alpha-2 국가 코드
     */
    override suspend fun getCountryByCode(cca2: String): Country? =
        dao.getByCode(cca2)?.toDomain()

    /**
     * 국가 목록을 로컬 DB에 일괄 저장 (Upsert)
     *
     * 도메인 모델 → Entity로 변환 후 저장합니다.
     */
    override suspend fun saveCountries(countries: List<Country>) {
        dao.upsertAll(countries.map { it.toEntity() })
    }

    /**
     * 국가 단건을 로컬 DB에 저장 (Upsert)
     */
    override suspend fun saveCountry(country: Country) {
        dao.upsert(country.toEntity())
    }

    /**
     * 로컬 DB의 모든 국가 데이터 삭제 (캐시 초기화)
     */
    override suspend fun clearAll() {
        dao.deleteAll()
    }
}
