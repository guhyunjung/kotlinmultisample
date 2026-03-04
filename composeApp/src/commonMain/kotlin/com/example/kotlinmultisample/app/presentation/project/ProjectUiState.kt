package com.example.kotlinmultisample.app.presentation.project

import com.example.kotlinmultisample.shared.domain.model.Project

data class ProjectUiState (
	val isLoading: Boolean = false,
	val projects: List<Project> = emptyList(),
	val error: String? = null
)
