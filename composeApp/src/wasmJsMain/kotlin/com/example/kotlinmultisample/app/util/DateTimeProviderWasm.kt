package com.example.kotlinmultisample.app.util

/**
 * WASM/JS 환경용 DateTimeProvider 실제 구현
 * 외부 선언한 JsDate를 사용하여 ISO 문자열에서 날짜 부분(YYYY-MM-DD)을 반환합니다.
 */
actual object DateTimeProvider {
    actual fun currentDateString(): String = JsDate().toISOString().substring(0, 10)

    actual fun epochMillisToUtcDateString(millis: Long): String = JsDate(millis.toDouble()).toISOString().substring(0, 10)
}




