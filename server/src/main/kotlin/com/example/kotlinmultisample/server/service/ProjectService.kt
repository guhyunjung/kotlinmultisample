package com.example.kotlinmultisample.server.service

import com.example.kotlinmultisample.server.dto.ProjectDto
import com.example.kotlinmultisample.server.dto.toDto
import com.example.kotlinmultisample.server.dto.toEntity
import com.example.kotlinmultisample.server.repository.ProjectRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Project 비즈니스 로직 서비스
 *
 * Controller와 Repository 사이에서 비즈니스 규칙을 처리합니다.
 * - Entity ↔ DTO 변환 책임
 * - 트랜잭션 관리 (@Transactional)
 *
 * @property repository Project JPA Repository
 */
@Service
@Transactional(readOnly = true) // 기본: 읽기 전용 트랜잭션 (성능 최적화)
class ProjectService(
    private val repository: ProjectRepository
) {

    /**
     * 전체 프로젝트 목록 조회 (시작일 내림차순)
     *
     * @return [ProjectDto] 목록
     */
    fun getProjects(): List<ProjectDto> {
        return repository.findAllByOrderByStartDateDesc().map { it.toDto() }
    }

    /**
     * 특정 ID의 프로젝트 조회
     *
     * @param id 조회할 프로젝트 ID
     * @return [ProjectDto], 없으면 null
     */
    fun getProjectById(id: String): ProjectDto? {
        return repository.findById(id).orElse(null)?.toDto()
    }

    /**
     * 프로젝트 생성
     *
     * @param dto 생성할 프로젝트 데이터
     * @return 저장된 [ProjectDto]
     * @throws IllegalArgumentException 제목이 비어 있을 경우
     */
    @Transactional // 쓰기 작업: 일반 트랜잭션 적용
    fun createProject(dto: ProjectDto): ProjectDto {
        require(dto.title.isNotBlank()) { "프로젝트 제목은 비워둘 수 없습니다." }
        val saved = repository.save(dto.toEntity())
        return saved.toDto()
    }

    /**
     * 프로젝트 수정 (전체 교체 방식 - PUT)
     *
     * @param id 수정할 프로젝트 ID
     * @param dto 수정할 데이터
     * @return 수정된 [ProjectDto]
     * @throws NoSuchElementException 해당 ID의 프로젝트가 없을 경우
     */
    @Transactional
    fun updateProject(id: String, dto: ProjectDto): ProjectDto {
        // 존재 여부 확인 (없으면 예외 발생)
        if (!repository.existsById(id)) {
            throw NoSuchElementException("프로젝트를 찾을 수 없습니다. id=$id")
        }
        // id를 경로 변수로 강제 고정 후 저장 (클라이언트가 body에 다른 id를 보내도 무시)
        val saved = repository.save(dto.copy(id = id).toEntity())
        return saved.toDto()
    }

    /**
     * 프로젝트 삭제
     *
     * @param id 삭제할 프로젝트 ID
     * @throws NoSuchElementException 해당 ID의 프로젝트가 없을 경우
     */
    @Transactional
    fun deleteProject(id: String) {
        if (!repository.existsById(id)) {
            throw NoSuchElementException("프로젝트를 찾을 수 없습니다. id=$id")
        }
        repository.deleteById(id)
    }
}

