package com.example.kotlinmultisample.shared.domain.repository

import com.example.kotlinmultisample.shared.domain.model.Project

interface ProjectRepository {

	suspend fun getProjects(): List<Project>

	suspend fun getProjectById(id: String): Project?

	suspend fun saveProject(project: Project)

	suspend fun deleteProject(id: String)
}