package com.example.kotlinmultisample.shared.data.local.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

/**
 * Room DAO - 국가 로컬 DB 접근 (Android)
 *
 * 인터페이스만 선언하면 Room이 컴파일 타임에 구현체를 자동 생성합니다.
 * 모든 메서드는 suspend 함수로 선언하여 코루틴에서 비동기 처리합니다.
 */
@Dao
interface CountryDao {

    /**
     * 전체 국가 목록 조회 (cca2 오름차순 정렬)
     *
     * @return 캐시된 [CountryEntity] 목록
     */
    @Query("SELECT * FROM countries ORDER BY cca2 ASC")
    suspend fun getAll(): List<CountryEntity>

    /**
     * 특정 ISO alpha-2 코드로 국가 단건 조회
     *
     * @param cca2 ISO alpha-2 국가 코드 (예: "CO")
     * @return 해당 [CountryEntity], 없으면 null
     */
    @Query("SELECT * FROM countries WHERE cca2 = :cca2")
    suspend fun getByCode(cca2: String): CountryEntity?

    /**
     * 국가 단건 삽입 또는 갱신 (Upsert)
     *
     * @param country 저장할 [CountryEntity]
     */
    @Upsert
    suspend fun upsert(country: CountryEntity)

    /**
     * 국가 목록 일괄 삽입 또는 갱신 (Upsert)
     *
     * @param countries 저장할 [CountryEntity] 목록
     */
    @Upsert
    suspend fun upsertAll(countries: List<CountryEntity>)

    /**
     * 전체 국가 데이터 삭제 (캐시 초기화)
     */
    @Query("DELETE FROM countries")
    suspend fun deleteAll()

    /**
     * 캐시된 국가 수 조회
     *
     * @return 저장된 국가 수
     */
    @Query("SELECT COUNT(*) FROM countries")
    suspend fun count(): Int
}


