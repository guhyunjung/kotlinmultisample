package com.example.kotlinmultisample.server.dto

import com.example.kotlinmultisample.server.entity.ProjectEntity

/**
 * Project API 응답/요청에 사용되는 DTO
 *
 * Entity와 달리 techStack을 List<String>으로 노출합니다.
 * 클라이언트(Android/JVM)는 이 형태의 JSON을 주고받습니다.
 *
 * 예시 JSON:
 * ```json
 * {
 *   "id": "proj-001",
 *   "title": "KotlinMultiSample",
 *   "description": "Kotlin Multiplatform 샘플",
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
    /** API에서는 List<String>으로 직렬화/역직렬화 */
    val techStack: List<String>,
    val githubUrl: String?,
    val startDate: String,
    val endDate: String
)

/**
 * [ProjectDto]를 JPA [ProjectEntity]로 변환합니다.
 *
 * techStack: List<String> → "," 구분 문자열
 */
fun ProjectDto.toEntity(): ProjectEntity = ProjectEntity(
    id = id,
    title = title,
    description = description,
    techStack = techStack.joinToString(","),
    githubUrl = githubUrl,
    startDate = startDate,
    endDate = endDate
)

/**
 * JPA [ProjectEntity]를 [ProjectDto]로 변환합니다.
 *
 * techStack: "," 구분 문자열 → List<String>
 */
fun ProjectEntity.toDto(): ProjectDto = ProjectDto(
    id = id,
    title = title,
    description = description,
    techStack = techStack.split(",").map { it.trim() }.filter { it.isNotEmpty() },
    githubUrl = githubUrl,
    startDate = startDate,
    endDate = endDate
)

