package com.example.kotlinmultisample.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

/**
 * Room DAO (Data Access Object) - 프로젝트 로컬 DB 접근 (Android)
 *
 * 인터페이스만 선언하면 Room이 컴파일 타임에 구현체를 자동 생성합니다.
 * 모든 메서드는 suspend 함수로 선언하여 코루틴에서 비동기 처리합니다.
 */
@Dao
interface ProjectDao {

    /**
     * 전체 프로젝트 목록 조회
     *
     * startDate 기준 내림차순 정렬 (최신 프로젝트 먼저)
     *
     * @return 로컬 DB에 저장된 [ProjectEntity] 목록
     */
    @Query("SELECT * FROM projects ORDER BY startDate DESC")
    suspend fun getAll(): List<ProjectEntity>

    /**
     * 특정 ID의 프로젝트 단건 조회
     *
     * @param id 조회할 프로젝트 ID
     * @return 해당 [ProjectEntity], 없으면 null
     */
    @Query("SELECT * FROM projects WHERE id = :id")
    suspend fun getById(id: String): ProjectEntity?

    /**
     * 프로젝트 삽입 또는 갱신 (Upsert)
     *
     * - 동일 id가 없으면 INSERT, 있으면 UPDATE 합니다.
     * - 원격에서 받아온 데이터를 로컬에 캐시할 때 사용합니다.
     *
     * @param project 저장할 [ProjectEntity]
     */
    @Upsert
    suspend fun upsert(project: ProjectEntity)

    /**
     * 여러 프로젝트 일괄 삽입 또는 갱신 (Upsert)
     *
     * 원격 목록 전체를 로컬에 동기화할 때 사용합니다.
     *
     * @param projects 저장할 [ProjectEntity] 목록
     */
    @Upsert
    suspend fun upsertAll(projects: List<ProjectEntity>)

    /**
     * 특정 ID의 프로젝트 삭제
     *
     * @param id 삭제할 프로젝트 ID
     */
    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteById(id: String)

    /**
     * 전체 프로젝트 삭제 (캐시 초기화)
     *
     * 원격 데이터와 전체 동기화가 필요할 때 사용합니다.
     */
    @Query("DELETE FROM projects")
    suspend fun deleteAll()
}

