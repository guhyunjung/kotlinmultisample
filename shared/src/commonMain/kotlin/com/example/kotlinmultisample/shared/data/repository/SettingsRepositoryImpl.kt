package com.example.kotlinmultisample.shared.data.repository

import com.example.kotlinmultisample.shared.domain.repository.SettingsRepository
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getBooleanFlow
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalSettingsApi::class)
class SettingsRepositoryImpl(
    private val settings: ObservableSettings
) : SettingsRepository {

    private val KEY_TUTORIAL_COMPLETED = "tutorial_completed"
    private val KEY_TUTORIAL_ENABLED = "tutorial_enabled"

    override fun isTutorialCompleted(): Flow<Boolean> {
        return settings.getBooleanFlow(KEY_TUTORIAL_COMPLETED, false)
    }

    override suspend fun setTutorialCompleted(completed: Boolean) {
        settings.putBoolean(KEY_TUTORIAL_COMPLETED, completed)
    }

    override fun isTutorialEnabled(): Flow<Boolean> {
        return settings.getBooleanFlow(KEY_TUTORIAL_ENABLED, true)
    }

    override suspend fun setTutorialEnabled(enabled: Boolean) {
        settings.putBoolean(KEY_TUTORIAL_ENABLED, enabled)
    }
}

