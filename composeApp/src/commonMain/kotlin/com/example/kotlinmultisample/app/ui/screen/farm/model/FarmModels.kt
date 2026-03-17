package com.example.kotlinmultisample.app.ui.screen.farm.model

data class FarmSeed(
    val id: Long = 0, // 고유 ID (Room 등에서 자동생성을 위해 필요)
    val brokerId: Long, // 부모 증권사 ID
    val name: String,
    val emoji: String,
    val pct: Double = 0.0,
    val buyingPrice: Double = 0.0,
    val quantity: Int = 0
) {
    val currentPrice: Double
        get() = buyingPrice * (1 + pct / 100)
    
    val totalBuyingValue: Double
        get() = buyingPrice * quantity
        
    val totalCurrentValue: Double
        get() = currentPrice * quantity
        
    val profit: Double
        get() = totalCurrentValue - totalBuyingValue
}

data class SummaryData(
    val totalInvest: Double = 0.0,
    val totalCurrent: Double = 0.0,
    val totalProfit: Double = 0.0
)

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
