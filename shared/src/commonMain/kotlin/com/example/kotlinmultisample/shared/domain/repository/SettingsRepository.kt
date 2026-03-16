package com.example.kotlinmultisample.shared.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    /**
     * 튜토리얼 완료 여부를 관찰하는 Flow
     */
    fun isTutorialCompleted(): Flow<Boolean>

    /**
     * 튜토리얼 완료 상태를 저장
     */
    suspend fun setTutorialCompleted(completed: Boolean)
    
    /**
     * 설정 화면에서 튜토리얼 보기 활성화 여부
     */
    fun isTutorialEnabled(): Flow<Boolean>

    /**
     * 튜토리얼 보기 활성화 상태 저장
     */
    suspend fun setTutorialEnabled(enabled: Boolean)
}

