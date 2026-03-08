package com.example.kotlinmultisample.shared.util

/**
 * KMP 공통 로거 (expect)
 *
 * 플랫폼별 로그 출력을 추상화합니다.
 * - Android : android.util.Log
 * - JVM     : println (System.out)
 */
expect object Logger {
    fun d(tag: String, message: String)
    fun e(tag: String, message: String, throwable: Throwable? = null)
    fun i(tag: String, message: String)
}

