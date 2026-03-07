package com.example.kotlinmultisample.shared.data.remote.datasource

import com.example.kotlinmultisample.shared.data.remote.dto.ProjectDto

/**
 * 원격 데이터 소스 인터페이스 (공통)
 *
 * commonMain에 인터페이스를 선언하고,
 * 실제 Retrofit 구현은 [RemoteProjectDataSourceImpl] (jvmMain / androidMain) 에서 제공합니다.
 *
 * 이 인터페이스를 통해 도메인/프레젠테이션 계층은
 * 플랫폼 구체 구현에 의존하지 않습니다.
 */
interface RemoteProjectDataSource {

    /**
     * 원격 서버에서 전체 프로젝트 목록을 조회합니다.
     *
     * @return 서버에서 받은 [ProjectDto] 목록
     * @throws Exception 네트워크 오류, HTTP 오류 등 발생 시
     */
    suspend fun getProjects(): List<ProjectDto>

    /**
     * 특정 ID의 프로젝트를 원격 서버에서 조회합니다.
     *
     * @param id 조회할 프로젝트의 고유 식별자
     * @return 해당 프로젝트 [ProjectDto], 존재하지 않으면 null
     * @throws Exception 네트워크 오류, HTTP 오류 등 발생 시
     */
    suspend fun getProjectById(id: String): ProjectDto?

    /**
     * 새 프로젝트를 원격 서버에 저장합니다.
     *
     * @param project 저장할 [ProjectDto]
     * @return 서버에서 생성된 [ProjectDto] (서버 측 ID가 부여된 상태)
     * @throws Exception 네트워크 오류, HTTP 오류 등 발생 시
     */
    suspend fun saveProject(project: ProjectDto): ProjectDto

    /**
     * 원격 서버에서 프로젝트를 삭제합니다.
     *
     * @param id 삭제할 프로젝트의 고유 식별자
     * @throws Exception 네트워크 오류, HTTP 오류 등 발생 시
     */
    suspend fun deleteProject(id: String)
}

