package com.example.kotlinmultisample.shared.data.local.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

/**
 * Room DAO - 국가 로컬 DB 접근 (JVM Desktop)
 *
 * 인터페이스만 선언하면 Room이 컴파일 타임에 구현체를 자동 생성합니다.
 * 모든 메서드는 suspend 함수로 선언하여 코루틴에서 비동기 처리합니다.
 */
@Dao
interface CountryDao {

    @Query("SELECT * FROM countries ORDER BY cca2 ASC")
    suspend fun getAll(): List<CountryEntity>

    @Query("SELECT * FROM countries WHERE cca2 = :cca2")
    suspend fun getByCode(cca2: String): CountryEntity?

    @Upsert
    suspend fun upsert(country: CountryEntity)

    @Upsert
    suspend fun upsertAll(countries: List<CountryEntity>)

    @Query("DELETE FROM countries")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM countries")
    suspend fun count(): Int
}


