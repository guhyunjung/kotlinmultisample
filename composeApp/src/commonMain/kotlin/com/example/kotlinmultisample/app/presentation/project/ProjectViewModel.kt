package com.example.kotlinmultisample.app.presentation.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinmultisample.shared.domain.interactor.ProjectInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * `ProjectViewModel`은 UI 상태를 관리하고, 비즈니스 로직을 호출하여 데이터를 가져오는 역할을 합니다.
 *
 * @property interactor 비즈니스 로직을 처리하는 `ProjectInteractor` 객체
 * @constructor `ProjectInteractor`를 주입받아 초기화합니다.
 */
class ProjectViewModel(
	private val interactor: ProjectInteractor
): ViewModel() {

	// UI 상태를 저장하는 StateFlow. 외부에서는 읽기 전용으로 접근 가능.
	private val _state = MutableStateFlow(ProjectUiState())
	val state: StateFlow<ProjectUiState> = _state

	/**
	 * 프로젝트 데이터를 로드하는 메서드입니다.
	 *
	 * - UI 상태를 로딩 중으로 설정한 후, `ProjectInteractor`를 통해 프로젝트 데이터를 가져옵니다.
	 * - 데이터 로드 성공 시, 상태를 업데이트합니다.
	 * - 예외 발생 시, 에러 메시지를 상태에 반영합니다.
	 */
	fun loadProjects() {
		viewModelScope.launch {
			_state.value = _state.value.copy(isLoading = true)
			try {
				val result = interactor.getProjects()
				_state.value = ProjectUiState(isLoading = false, projects = result)
			} catch (e: Exception) {
				_state.value = ProjectUiState(isLoading = false, error = e.message)
			}
		}
	}
}
