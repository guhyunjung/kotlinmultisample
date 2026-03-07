package com.example.kotlinmultisample.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.kotlinmultisample.shared.domain.model.Project

/**
 * Room 로컬 DB에 저장되는 Project Entity (JVM Desktop)
 *
 * 도메인 모델 [Project]와 1:1 대응하며,
 * 네트워크에서 받아온 데이터를 로컬에 캐시할 때 사용합니다.
 *
 * - tableName: DB에 생성될 테이블 이름
 * - techStack: Room은 List를 직접 저장할 수 없으므로 쉼표(,)로 구분된 문자열로 저장합니다.
 *   → [toDomain] / [fromDomain]에서 변환 처리합니다.
 */
@Entity(tableName = "projects")
data class ProjectEntity(
    /** 프로젝트 고유 식별자 (기본 키) */
    @PrimaryKey
    val id: String,

    /** 프로젝트 제목 */
    val title: String,

    /** 프로젝트 설명 */
    val description: String,

    /**
     * 기술 스택 목록 (List<String>를 "," 구분 문자열로 직렬화)
     * 예) "Kotlin,Compose,Koin"
     */
    val techStack: String,

    /** GitHub 저장소 URL (없을 경우 null) */
    val githubUrl: String?,

    /** 프로젝트 시작일 (yyyy-MM-dd 형식) */
    val startDate: String,

    /** 프로젝트 종료일 (yyyy-MM-dd 형식) */
    val endDate: String
)

/**
 * [ProjectEntity]를 도메인 모델 [Project]로 변환합니다.
 *
 * techStack: 쉼표 구분 문자열 → List<String>
 */
fun ProjectEntity.toDomain(): Project = Project(
    id = id,
    title = title,
    description = description,
    techStack = techStack.split(",").map { it.trim() }.filter { it.isNotEmpty() },
    githubUrl = githubUrl,
    startDate = startDate,
    endDate = endDate
)

/**
 * 도메인 모델 [Project]를 [ProjectEntity]로 변환합니다.
 *
 * techStack: List<String> → 쉼표 구분 문자열
 */
fun Project.toEntity(): ProjectEntity = ProjectEntity(
    id = id,
    title = title,
    description = description,
    techStack = techStack.joinToString(","),
    githubUrl = githubUrl,
    startDate = startDate,
    endDate = endDate
)

