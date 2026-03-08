package com.example.kotlinmultisample.shared.util

import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * JVM Desktop 전용 Logger 구현체
 * System.out(println)으로 콘솔에 출력합니다.
 */
actual object Logger {

    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS")

    private fun now() = LocalTime.now().format(timeFormatter)

    actual fun d(tag: String, message: String) {
        println("[${now()}] D/$tag: $message")
    }

    actual fun e(tag: String, message: String, throwable: Throwable?) {
        System.err.println("[${now()}] E/$tag: $message")
        throwable?.printStackTrace(System.err)
    }

    actual fun i(tag: String, message: String) {
        println("[${now()}] I/$tag: $message")
    }
}

