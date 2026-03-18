package com.example.kotlinmultisample.app.util

expect object DateTimeProvider {
	fun currentDateString(): String
	fun epochMillisToUtcDateString(millis: Long): String
}
