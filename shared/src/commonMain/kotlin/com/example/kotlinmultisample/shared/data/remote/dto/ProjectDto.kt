package com.example.kotlinmultisample.shared.data.remote.dto

import com.example.kotlinmultisample.shared.domain.model.Project

/**
 * 원격 API 응답에서 사용되는 Project DTO (Data Transfer Object)
 *
 * API 서버에서 내려오는 JSON 필드명과 맞추고,
 * 필요한 경우 [toDomain] 확장함수를 통해 도메인 모델로 변환합니다.
 *
 * 예시 JSON:
 * ```json
 * {
 *   "id": "proj-001",
 *   "title": "KotlinMultiSample",
 *   "description": "Kotlin Multiplatform 샘플 프로젝트",
 *   "techStack": ["Kotlin", "Compose", "Koin"],
 *   "githubUrl": "https://github.com/example/project",
 *   "startDate": "2024-01-01",
 *   "endDate": "2024-12-31"
 * }
 * ```
 */
data class ProjectDto(
    val id: String,
    val title: String,
    val description: String,
    val techStack: List<String>,
    val githubUrl: String?,
    val startDate: String,
    val endDate: String
)

/**
 * [ProjectDto]를 도메인 모델 [Project]로 변환하는 확장 함수
 *
 * 네트워크 계층(data)과 도메인 계층의 분리를 위해
 * DTO → Domain 변환 책임을 확장함수로 캡슐화합니다.
 */
fun ProjectDto.toDomain(): Project = Project(
    id = id,
    title = title,
    description = description,
    techStack = techStack,
    githubUrl = githubUrl,
    startDate = startDate,
    endDate = endDate
)

