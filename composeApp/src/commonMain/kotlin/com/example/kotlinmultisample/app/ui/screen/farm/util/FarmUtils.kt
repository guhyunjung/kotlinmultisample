package com.example.kotlinmultisample.app.ui.screen.farm.util

// 간단한 파싱 확장 함수
internal fun String.toBiggerDecimal(): Double? {
	return this.toDoubleOrNull()
}

// KMP 호환용 간단 포맷터
internal fun formatDecimal(value: Double): String {
	if (value.isNaN()) return "0"
	val integerPart = value.toLong()
	val fraction = value - integerPart
	val fractionPart = (fraction * 100).toInt() // 소수점 2자리 간소화

	// 0.05 -> 5, 0.5 -> 50
	val fractionStr = if (fractionPart < 10) "0$fractionPart" else fractionPart.toString()
	
	// 소수점이 00이면 정수만 표시
	return if (fractionPart == 0) {
		formatInteger(integerPart)
	} else {
		"${formatInteger(integerPart)}.$fractionStr"
	}
}

internal fun formatInteger(value: Long): String {
	return value.toString().reversed().chunked(3).joinToString(",").reversed()
}

