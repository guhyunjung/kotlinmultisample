package com.example.kotlinmultisample.app.ui.util

/**
 * JS 브라우저용 현재 시간 반환
 */
actual fun getCurrentTimeMillis(): Long = (kotlin.js.Date.now() as Double).toLong()

