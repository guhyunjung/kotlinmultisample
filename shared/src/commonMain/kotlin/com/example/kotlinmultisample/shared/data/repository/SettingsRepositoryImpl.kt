package com.example.kotlinmultisample.shared.data.repository

import com.example.kotlinmultisample.shared.domain.repository.SettingsRepository
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getBooleanFlow
import kotlinx.coroutines.flow.Flow
import co.touchlab.kermit.Logger

@OptIn(ExperimentalSettingsApi::class)
class SettingsRepositoryImpl(
    private val settings: ObservableSettings
) : SettingsRepository {
    private val logger = Logger.withTag("SettingsRepositoryImpl")

    private val KEY_TUTORIAL_COMPLETED = "tutorial_completed"
    private val KEY_TUTORIAL_ENABLED = "tutorial_enabled"

    override fun isTutorialCompleted(): Flow<Boolean> {
        logger.d { "isTutorialCompleted() 호출" }
        return settings.getBooleanFlow(KEY_TUTORIAL_COMPLETED, false)
    }

    override suspend fun setTutorialCompleted(completed: Boolean) {
        logger.d { "setTutorialCompleted(completed=$completed) 호출" }
        settings.putBoolean(KEY_TUTORIAL_COMPLETED, completed)
    }

    override fun isTutorialEnabled(): Flow<Boolean> {
        logger.d { "isTutorialEnabled() 호출" }
        return settings.getBooleanFlow(KEY_TUTORIAL_ENABLED, true)
    }

    override suspend fun setTutorialEnabled(enabled: Boolean) {
        logger.d { "setTutorialEnabled(enabled=$enabled) 호출" }
        settings.putBoolean(KEY_TUTORIAL_ENABLED, enabled)
    }
}
