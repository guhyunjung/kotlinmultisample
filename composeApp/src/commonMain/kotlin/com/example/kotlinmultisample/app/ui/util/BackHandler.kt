package com.example.kotlinmultisample.app.ui.util

import androidx.compose.runtime.Composable

@Composable
expect fun BackHandler(enabled: Boolean = true, onBack: () -> Unit) // expect 기대하다,예상하다

