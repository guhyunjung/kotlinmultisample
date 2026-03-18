package com.example.kotlinmultisample.app.ui.util

import com.example.kotlinmultisample.app.util.JsDate

/**
 * WASM/JS용 현재 시간 반환 구현
 * 외부 선언한 JsDate를 사용합니다.
 */
actual fun getCurrentTimeMillis(): Long = JsDate.now().toLong()




