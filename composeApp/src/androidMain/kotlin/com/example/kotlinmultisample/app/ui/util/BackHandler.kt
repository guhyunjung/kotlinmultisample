package com.example.kotlinmultisample.app.ui.util

import androidx.compose.runtime.Composable
import androidx.activity.compose.BackHandler as AndroidBackHandler

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) { // actual 실제의, 실직적인 구현
    AndroidBackHandler(enabled, onBack)
}

