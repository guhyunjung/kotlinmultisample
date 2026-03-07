package com.example.kotlinmultisample.server.entity

import jakarta.persistence.*

/**
 * 서버 측 Project JPA Entity
 *
 * Supabase(PostgreSQL)의 "projects" 테이블과 매핑됩니다.
 * ddl-auto: update 설정으로 서버 최초 실행 시 테이블이 자동 생성됩니다.
 *
 * - techStack: List<String>를 DB에 문자열로 저장 (쉼표 구분)
 *   JPA는 기본적으로 컬렉션을 별도 테이블로 관리하므로,
 *   단순 문자열 직렬화가 성능/관리 면에서 유리합니다.
 */
@Entity
@Table(name = "projects")
data class ProjectEntity(

    /** 프로젝트 고유 식별자 (UUID 문자열 권장) */
    @Id
    val id: String,

    /** 프로젝트 제목 */
    @Column(nullable = false)
    val title: String,

    /** 프로젝트 설명 */
    @Column(nullable = false, columnDefinition = "TEXT")
    val description: String,

    /**
     * 기술 스택 목록
     * DB 저장 형식: "Kotlin,Compose,Koin" (쉼표 구분 문자열)
     * API 응답 시 [ProjectDto]에서 List<String>으로 변환합니다.
     */
    @Column(nullable = false)
    val techStack: String,

    /** GitHub 저장소 URL (없을 경우 null) */
    @Column(nullable = true)
    val githubUrl: String? = null,

    /** 프로젝트 시작일 (yyyy-MM-dd 형식) */
    @Column(nullable = false)
    val startDate: String,

    /** 프로젝트 종료일 (yyyy-MM-dd 형식) */
    @Column(nullable = false)
    val endDate: String
)

