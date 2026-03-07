package com.example.kotlinmultisample.shared.data.remote.api

import com.example.kotlinmultisample.shared.data.remote.dto.ProjectDto
import retrofit2.http.*

/**
 * Retrofit API Service 인터페이스 (JVM Desktop 전용)
 *
 * Retrofit은 JVM 기반이므로 commonMain이 아닌 jvmMain에 선언합니다.
 * 동일한 인터페이스가 androidMain에도 존재합니다.
 *
 * 기본 URL: NetworkModule에서 설정한 baseUrl 기준으로 상대 경로를 사용합니다.
 * 예) baseUrl = "https://api.example.com/" → GET /projects → "https://api.example.com/projects"
 *
 * ※ 실제 서버 엔드포인트에 맞춰 경로를 수정하세요.
 */
interface ProjectApiService {

    /**
     * 전체 프로젝트 목록 조회
     * GET /projects
     *
     * @return 서버에서 반환된 [ProjectDto] 목록
     */
    @GET("projects")
    suspend fun getProjects(): List<ProjectDto>

    /**
     * 특정 프로젝트 단건 조회
     * GET /projects/{id}
     *
     * @param id 조회할 프로젝트 ID (URL 경로 변수)
     * @return 해당 프로젝트의 [ProjectDto]
     */
    @GET("projects/{id}")
    suspend fun getProjectById(@Path("id") id: String): ProjectDto

    /**
     * 새 프로젝트 생성
     * POST /projects
     *
     * @param project 생성할 프로젝트 데이터 ([ProjectDto], JSON Body로 전송)
     * @return 서버에서 생성된 [ProjectDto] (서버 측 ID 포함)
     */
    @POST("projects")
    suspend fun createProject(@Body project: ProjectDto): ProjectDto

    /**
     * 기존 프로젝트 수정
     * PUT /projects/{id}
     *
     * @param id 수정할 프로젝트 ID (URL 경로 변수)
     * @param project 수정할 데이터 ([ProjectDto], JSON Body로 전송)
     * @return 서버에서 수정된 [ProjectDto]
     */
    @PUT("projects/{id}")
    suspend fun updateProject(@Path("id") id: String, @Body project: ProjectDto): ProjectDto

    /**
     * 프로젝트 삭제
     * DELETE /projects/{id}
     *
     * @param id 삭제할 프로젝트 ID (URL 경로 변수)
     */
    @DELETE("projects/{id}")
    suspend fun deleteProject(@Path("id") id: String)
}

