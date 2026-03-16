package com.example.kotlinmultisample.app.presentation.farm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinmultisample.shared.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FarmViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    val showTutorial = combine(
        settingsRepository.isTutorialCompleted(),
        settingsRepository.isTutorialEnabled()
    ) { completed, enabled ->
        !completed && enabled
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun completeTutorial() {
        viewModelScope.launch {
            settingsRepository.setTutorialCompleted(true)
        }
    }
}
