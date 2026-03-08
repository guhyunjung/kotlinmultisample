package com.example.kotlinmultisample.database

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
     * 전체 국가 목록 조회
     *
     * 영어 일반 명칭(nameJson의 common 필드) 기준으로 정렬하면 이상적이나,
     * JSON 컬럼 내부 필드 정렬은 Room에서 직접 지원하지 않으므로
     * 기본 키(cca2) 기준 정렬을 사용합니다.
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
     * 국가 삽입 또는 갱신 (Upsert)
     *
     * - 동일 cca2가 없으면 INSERT, 있으면 UPDATE합니다.
     * - API 결과를 로컬에 동기화할 때 사용합니다.
     *
     * @param country 저장할 [CountryEntity]
     */
    @Upsert
    suspend fun upsert(country: CountryEntity)

    /**
     * 국가 목록 일괄 삽입 또는 갱신 (Upsert)
     *
     * API에서 받은 전체 목록을 한 번에 동기화합니다.
     *
     * @param countries 저장할 [CountryEntity] 목록
     */
    @Upsert
    suspend fun upsertAll(countries: List<CountryEntity>)

    /**
     * 전체 국가 데이터 삭제 (캐시 초기화)
     *
     * 강제 새로고침 시 기존 캐시를 초기화할 때 사용합니다.
     */
    @Query("DELETE FROM countries")
    suspend fun deleteAll()

    /**
     * 캐시된 국가 수 조회
     *
     * 캐시 존재 여부를 빠르게 확인할 때 사용합니다.
     *
     * @return 저장된 국가 수
     */
    @Query("SELECT COUNT(*) FROM countries")
    suspend fun count(): Int
}

