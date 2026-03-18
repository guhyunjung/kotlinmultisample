package com.example.kotlinmultisample.shared.domain.model

/**
 * 주식(종목) 도메인 모델
 * 실제 API 응답 필드명은 공공데이터포털 문서를 참고하여 DTO에서 매핑하세요.
 */
data class Stock(
    val code: String,          // 종목 코드 (예: "005930")
    val name: String,          // 종목명
    val tradePrice: Double?,   // 현재가
    val changeAmount: Double?, // 등락(절대값)
    val changeRate: Double?,   // 등락률 (%)
    val volume: Long?,         // 거래량
    val baseDate: String?      // 기준일자 (YYYYMMDD)
)

