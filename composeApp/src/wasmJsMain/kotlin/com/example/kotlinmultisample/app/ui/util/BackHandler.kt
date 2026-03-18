package com.example.kotlinmultisample.app.ui.util

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    // WASM/JS 환경에서는 기본적으로 Android의 BackHandler가 없으므로
    // 일단 No-op 구현으로 두었습니다. 브라우저의 뒤로가기(pushState/popstate)
    // 등을 직접 처리하려면 JS interop(window.onpopstate 등)를 추가하세요.
}

