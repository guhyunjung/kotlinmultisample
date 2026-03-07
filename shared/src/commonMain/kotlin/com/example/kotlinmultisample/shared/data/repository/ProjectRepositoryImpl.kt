package com.example.kotlinmultisample.shared.data.repository

import com.example.kotlinmultisample.shared.data.remote.datasource.RemoteProjectDataSource
import com.example.kotlinmultisample.shared.data.remote.dto.ProjectDto
import com.example.kotlinmultisample.shared.data.remote.dto.toDomain
import com.example.kotlinmultisample.shared.domain.model.Project
import com.example.kotlinmultisample.shared.domain.repository.ProjectRepository

/**
 * [ProjectRepository] 구현체 - 원격 데이터 소스 기반
 *
 * 현재는 원격(Remote) 데이터 소스만 사용합니다.
 * 추후 로컬 캐시(Room)를 추가하려면 LocalProjectDataSource를 주입받아
 * 오프라인 우선(Offline-First) 전략으로 확장할 수 있습니다.
 *
 * 예시 확장 방향:
 * ```
 * // 1. 로컬 캐시에서 먼저 읽기
 * val cached = localDataSource.getProjects()
 * if (cached.isNotEmpty()) return cached.map { it.toDomain() }
 *
 * // 2. 원격에서 가져와 로컬에 저장(캐시 갱신)
 * val remote = remoteDataSource.getProjects()
 * localDataSource.saveProjects(remote)
 * return remote.map { it.toDomain() }
 * ```
 *
 * @property remoteDataSource 원격 데이터 소스 (Retrofit 구현체)
 */
class ProjectRepositoryImpl(
    private val remoteDataSource: RemoteProjectDataSource
) : ProjectRepository {

    /**
     * 원격 서버에서 전체 프로젝트 목록을 가져와 도메인 모델로 변환합니다.
     *
     * @return 도메인 [Project] 목록
     */
    override suspend fun getProjects(): List<Project> {
        return remoteDataSource.getProjects().map { it.toDomain() }
    }

    /**
     * 원격 서버에서 특정 ID의 프로젝트를 조회합니다.
     *
     * @param id 조회할 프로젝트의 고유 식별자
     * @return 도메인 [Project], 존재하지 않으면 null
     */
    override suspend fun getProjectById(id: String): Project? {
        return remoteDataSource.getProjectById(id)?.toDomain()
    }

    /**
     * 도메인 모델을 DTO로 변환 후 원격 서버에 저장합니다.
     *
     * @param project 저장할 도메인 [Project]
     */
    override suspend fun saveProject(project: Project) {
        // 도메인 → DTO 변환 후 원격 저장
        val dto = project.toDto()
        remoteDataSource.saveProject(dto)
    }

    /**
     * 원격 서버에서 프로젝트를 삭제합니다.
     *
     * @param id 삭제할 프로젝트의 고유 식별자
     */
    override suspend fun deleteProject(id: String) {
        remoteDataSource.deleteProject(id)
    }

    // ─────────────────────────────────────────────────────────────────
    // 내부 변환 함수
    // ─────────────────────────────────────────────────────────────────

    /**
     * 도메인 모델 [Project]를 네트워크 전송용 [ProjectDto]로 변환합니다.
     * (역변환: DTO → Domain은 ProjectDto.kt의 toDomain() 참고)
     */
    private fun Project.toDto(): ProjectDto = ProjectDto(
        id = id,
        title = title,
        description = description,
        techStack = techStack,
        githubUrl = githubUrl,
        startDate = startDate,
        endDate = endDate
    )
}

