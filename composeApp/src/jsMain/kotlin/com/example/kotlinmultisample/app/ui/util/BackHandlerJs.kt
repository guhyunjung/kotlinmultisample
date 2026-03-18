package com.example.kotlinmultisample.app.ui.util

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    // JS 브라우저 환경에서는 Android의 BackHandler가 없으므로 기본적으로 No-op입니다.
    // 필요하면 window.onpopstate 등으로 뒤로가기 처리를 추가하세요.
}

