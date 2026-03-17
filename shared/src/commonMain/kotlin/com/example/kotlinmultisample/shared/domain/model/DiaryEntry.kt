package com.example.kotlinmultisample.shared.domain.model

enum class DiaryType(val label: String) {
    DAILY("오늘일기"),
    INTEREST("관심종목"),
    STUDY("공부노트")
}

data class DiaryEntry(
    val id: Long = 0,
    val date: String, // YYYY-MM-DD
    val content: String,
    val type: DiaryType = DiaryType.DAILY
)

