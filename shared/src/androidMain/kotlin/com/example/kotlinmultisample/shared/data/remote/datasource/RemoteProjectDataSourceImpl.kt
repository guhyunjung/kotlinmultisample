package com.example.kotlinmultisample.shared.data.remote.datasource

import com.example.kotlinmultisample.shared.data.remote.api.ProjectApiService
import com.example.kotlinmultisample.shared.data.remote.dto.ProjectDto

/**
 * [RemoteProjectDataSource] Retrofit 구현체 (Android 전용)
 *
 * Koin에서 [ProjectApiService]를 주입받아 실제 HTTP 요청을 수행합니다.
 * Retrofit의 suspend 함수는 자동으로 백그라운드 스레드에서 실행되므로
 * 별도의 withContext(Dispatchers.IO) 없이 호출해도 됩니다.
 *
 * @property apiService Koin으로 주입받은 Retrofit API Service
 */
class RemoteProjectDataSourceImpl(
    private val apiService: ProjectApiService
) : RemoteProjectDataSource {

    /**
     * 원격 서버에서 전체 프로젝트 목록을 조회합니다.
     * GET /projects
     */
    override suspend fun getProjects(): List<ProjectDto> {
        return apiService.getProjects()
    }

    /**
     * 특정 ID의 프로젝트를 원격 서버에서 조회합니다.
     * GET /projects/{id}
     *
     * 서버가 404를 반환하면 Retrofit이 HttpException을 던지므로
     * 여기서는 null을 반환하도록 예외를 처리합니다.
     */
    override suspend fun getProjectById(id: String): ProjectDto? {
        return try {
            apiService.getProjectById(id)
        } catch (e: retrofit2.HttpException) {
            // 404 Not Found인 경우 null 반환, 나머지 HTTP 오류는 재전파
            if (e.code() == 404) null else throw e
        }
    }

    /**
     * 새 프로젝트를 원격 서버에 저장합니다.
     * POST /projects
     *
     * 서버가 저장 후 ID가 부여된 [ProjectDto]를 반환합니다.
     */
    override suspend fun saveProject(project: ProjectDto): ProjectDto {
        return apiService.createProject(project)
    }

    /**
     * 원격 서버에서 프로젝트를 삭제합니다.
     * DELETE /projects/{id}
     */
    override suspend fun deleteProject(id: String) {
        apiService.deleteProject(id)
    }
}

