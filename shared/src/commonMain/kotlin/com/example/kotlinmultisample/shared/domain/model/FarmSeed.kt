package com.example.kotlinmultisample.shared.domain.model

/**
 * 농작물(주식 종목) 도메인 모델
 *
 * @property id 고유 ID
 * @property brokerId 소속된 증권사 ID
 * @property name 종목명 (예: 삼성전자)
 * @property emoji 아이콘 (예: 🌿)
 * @property pct 수익률 (퍼센트)
 * @property buyingPrice 매수 평균가
 * @property quantity 보유 수량
 */
data class FarmSeed(
    val id: Long = 0,
    val brokerId: Long,
    val name: String,
    val emoji: String,
    val pct: Double = 0.0,
    val buyingPrice: Double = 0.0,
    val quantity: Int = 0
)

