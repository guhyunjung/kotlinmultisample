package com.example.kotlinmultisample.shared.domain.model

data class Project(
	val id: String,
	val title: String,
	val description: String,
	val techStack: List<String>,
	val githubUrl: String?,
	val startDate: String,
	val endDate: String
)
