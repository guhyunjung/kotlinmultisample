package com.example.kotlinmultisample.server.repository

import com.example.kotlinmultisample.server.entity.ProjectEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Project JPA Repository
 *
 * Spring Data JPA가 컴파일 타임에 구현체를 자동 생성합니다.
 * 기본 CRUD 메서드(save, findById, findAll, deleteById)는
 * [JpaRepository]에서 모두 제공됩니다.
 *
 * 필요 시 커스텀 쿼리 메서드를 추가하세요.
 * 예) fun findByTitleContaining(keyword: String): List<ProjectEntity>
 */
@Repository
interface ProjectRepository : JpaRepository<ProjectEntity, String> {

    /**
     * 시작일 기준 내림차순 정렬로 전체 프로젝트 조회
     * (메서드 이름만으로 Spring Data JPA가 쿼리를 자동 생성합니다)
     *
     * @return 최신 프로젝트가 먼저 오는 목록
     */
    fun findAllByOrderByStartDateDesc(): List<ProjectEntity>
}

