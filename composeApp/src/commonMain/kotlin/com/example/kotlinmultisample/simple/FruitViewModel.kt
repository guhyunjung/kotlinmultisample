package com.example.kotlinmultisample.simple

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 간단한 ViewModel (FruitViewModel)
 * Presentation Layer: UI 상태 관리 및 비즈니스 로직(Repository) 연결
 * 
 * * UseCase 없음 (간소화: ViewModel -> Repository 직접 호출)
 */
class FruitViewModel(
    private val repository: FruitRepository
) : ViewModel() {

    // UI State: 과일 목록
    private val _fruits = MutableStateFlow<List<Fruit>>(emptyList())
    val fruits: StateFlow<List<Fruit>> = _fruits.asStateFlow()

    // Loading State
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadFruits()
    }

    private fun loadFruits() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Repository에서 데이터 가져오기
                val result = repository.getFruits()
                _fruits.value = result
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    // Refresh 기능 (예제용)
    fun refresh() {
        loadFruits()
    }
}

