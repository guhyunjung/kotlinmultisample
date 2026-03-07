package com.example.kotlinmultisample.server.controller

import com.example.kotlinmultisample.server.dto.ProjectDto
import com.example.kotlinmultisample.server.service.ProjectService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Project REST API Controller
 *
 * 기본 경로: /projects
 *
 * 이 엔드포인트가 Android/JVM 클라이언트의 Retrofit과 연결됩니다.
 * Android → Retrofit → 이 Controller → ProjectService → ProjectRepository → Supabase(PostgreSQL)
 */
@RestController
@RequestMapping("/projects")
class ProjectController(
    private val service: ProjectService
) {

    /**
     * 전체 프로젝트 목록 조회
     * GET /projects
     *
     * @return 200 OK + 프로젝트 목록 JSON
     */
    @GetMapping
    fun getProjects(): ResponseEntity<List<ProjectDto>> {
        return ResponseEntity.ok(service.getProjects())
    }

    /**
     * 특정 프로젝트 단건 조회
     * GET /projects/{id}
     *
     * @param id URL 경로의 프로젝트 ID
     * @return 200 OK + 프로젝트 JSON, 없으면 404 Not Found
     */
    @GetMapping("/{id}")
    fun getProjectById(@PathVariable id: String): ResponseEntity<ProjectDto> {
        val project = service.getProjectById(id)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(project)
    }

    /**
     * 새 프로젝트 생성
     * POST /projects
     *
     * @param dto 생성할 프로젝트 데이터 (JSON Body)
     * @return 201 Created + 생성된 프로젝트 JSON
     */
    @PostMapping
    fun createProject(@RequestBody dto: ProjectDto): ResponseEntity<ProjectDto> {
        val created = service.createProject(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    /**
     * 프로젝트 수정
     * PUT /projects/{id}
     *
     * @param id URL 경로의 프로젝트 ID
     * @param dto 수정할 데이터 (JSON Body)
     * @return 200 OK + 수정된 프로젝트 JSON, 없으면 404 Not Found
     */
    @PutMapping("/{id}")
    fun updateProject(
        @PathVariable id: String,
        @RequestBody dto: ProjectDto
    ): ResponseEntity<ProjectDto> {
        return try {
            ResponseEntity.ok(service.updateProject(id, dto))
        } catch (_: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }
    }

    /**
     * 프로젝트 삭제
     * DELETE /projects/{id}
     *
     * @param id URL 경로의 프로젝트 ID
     * @return 204 No Content, 없으면 404 Not Found
     */
    @DeleteMapping("/{id}")
    fun deleteProject(@PathVariable id: String): ResponseEntity<Void> {
        return try {
            service.deleteProject(id)
            ResponseEntity.noContent().build()
        } catch (_: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }
    }
}


