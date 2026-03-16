package com.example.kotlinmultisample.app.ui.screen.farm

data class FarmSeed(
    val name: String,
    val emoji: String,
    val pct: Double,
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
