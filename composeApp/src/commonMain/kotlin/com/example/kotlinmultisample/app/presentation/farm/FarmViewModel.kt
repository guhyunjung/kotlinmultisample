package com.example.kotlinmultisample.app.presentation.farm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FarmViewModel : ViewModel() {
    private val _showTutorial = MutableStateFlow(true)
    val showTutorial = _showTutorial.asStateFlow()

    fun completeTutorial() {
        _showTutorial.value = false
    }
}

