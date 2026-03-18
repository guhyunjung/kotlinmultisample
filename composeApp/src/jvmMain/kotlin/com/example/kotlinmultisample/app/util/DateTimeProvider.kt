package com.example.kotlinmultisample.app.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

actual object DateTimeProvider {
	actual fun currentDateString(): String {
		return LocalDate.now(ZoneId.systemDefault()).toString()
	}

	actual fun epochMillisToUtcDateString(millis: Long): String {
		return Instant.ofEpochMilli(millis)
			.atZone(ZoneId.of("UTC"))
			.toLocalDate()
			.toString()
	}
}
