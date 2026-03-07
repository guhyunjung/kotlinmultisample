package com.example.kotlinmultisample.shared.domain.interactor

import com.example.kotlinmultisample.shared.domain.model.Project
import com.example.kotlinmultisample.shared.domain.repository.ProjectRepository

/**
 * 프로젝트 관련 작업을 관리하는 Interactor 클래스입니다.
 *
 * Interactor(상호 작용자)는 비즈니스 로직을 캡슐화하여 특정 유스케이스를 실행하는 역할을 합니다.
 * 프레젠테이션 계층과 데이터 계층 사이에서 데이터를 조작하거나 흐름을 제어하며,
 * 애플리케이션의 핵심 도메인 규칙을 담당합니다.
 * UseCase를 기능 단위로 묶는다.
 *
 * 이 클래스는 프로젝트를 조회, 저장 및 삭제하는 메서드를 제공합니다.
 *
 * @property repository 프로젝트 관련 데이터 작업을 수행하는 데 사용되는 저장소입니다.
 */
class ProjectInteractor(
    private val repository: ProjectRepository
) {
    /**
     * 시작 날짜를 기준으로 내림차순 정렬된 프로젝트 목록을 조회합니다.
     *
     * @return 정렬된 프로젝트 목록입니다.
     */
    suspend fun getProjects(): List<Project> {
        return repository.getProjects().sortedByDescending { it.startDate }
    }

    /**
     * 고유 식별자로 프로젝트를 조회합니다.
     *
     * @param id 프로젝트의 고유 식별자입니다.
     * @return 지정된 ID를 가진 프로젝트, 찾을 수 없는 경우 null입니다.
     */
    suspend fun getProjectById(id: String): Project? {
        return repository.getProjectById(id)
    }

    /**
     * 프로젝트를 저장소에 저장합니다.
     *
     * @param project 저장할 프로젝트입니다.
     * @throws IllegalArgumentException 프로젝트 제목이 비어 있는 경우 발생합니다.
     */
    suspend fun saveProject(project: Project) {
        require(project.title.isNotBlank()) {
            "프로젝트 제목은 비워둘 수 없습니다"
        }
        repository.saveProject(project)
    }

    /**
     * 고유 식별자로 프로젝트를 삭제합니다.
     *
     * @param id 삭제할 프로젝트의 고유 식별자입니다.
     */
    suspend fun deleteProject(id: String) {
        repository.deleteProject(id)
    }
}