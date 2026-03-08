package com.example.kotlinmultisample.shared.util

import android.util.Log

/**
 * Android 전용 Logger 구현체
 * android.util.Log를 사용하여 Logcat에 출력합니다.
 */
actual object Logger {

    actual fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    actual fun e(tag: String, message: String, throwable: Throwable?) {
        Log.e(tag, message, throwable)
    }

    actual fun i(tag: String, message: String) {
        Log.i(tag, message)
    }
}

