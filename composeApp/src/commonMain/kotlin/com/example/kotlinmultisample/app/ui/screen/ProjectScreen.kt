package com.example.kotlinmultisample.app.ui.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.kotlinmultisample.app.presentation.project.ProjectViewModel

@Composable
fun ProjectScreen(viewModel: ProjectViewModel) {
	val state by viewModel.state.collectAsState()

	LaunchedEffect(Unit) {
		viewModel.loadProjects()
	}

	when {
		state.isLoading -> {
			// 로딩 상태 UI 표시
			CircularProgressIndicator()
		}

		state.error != null -> {
			Text("Error: ${state.error}")
		}

		else -> {
			LazyColumn {
				items(state.projects) { project ->
					Text(project.title)
				}
			}
		}
	}
}

