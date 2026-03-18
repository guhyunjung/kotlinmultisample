package com.example.kotlinmultisample.app.util

import kotlin.js.Date

actual object DateTimeProvider {
    actual fun currentDateString(): String = Date().toISOString().substring(0, 10)

    actual fun epochMillisToUtcDateString(millis: Long): String = Date(millis.toDouble()).toISOString().substring(0, 10)
}

