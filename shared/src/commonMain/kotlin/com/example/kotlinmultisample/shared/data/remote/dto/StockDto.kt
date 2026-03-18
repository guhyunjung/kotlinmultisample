package com.example.kotlinmultisample.shared.data.remote.dto

import com.example.kotlinmultisample.shared.domain.model.Stock

/**
 * 공공데이터포털 GetStockSecuritiesInfoService 실제 응답 필드에 맞춘 DTO
 * 예시 응답(item) 필드:
 *  - basDt, srtnCd, isinCd, itmsNm, mrktCtg, clpr, vs, fltRt, mkp,
 *    hipr, lopr, trqu, trPrc, lstgStCnt, mrktTotAmt
 */
data class StockItemDto(
    val basDt: String? = null,      // 기준일자
    val srtnCd: String? = null,     // 단축코드
    val isinCd: String? = null,     // ISIN코드
    val itmsNm: String? = null,     // 종목명
    val mrktCtg: String? = null,    // 시장구분
    val clpr: String? = null,       // 종가(또는 현재가)
    val vs: String? = null, 	    // 대비(전일대비)
    val fltRt: String? = null,      // 등락률
    val mkp: String? = null,        // 시가
    val hipr: String? = null,       // 고가
    val lopr: String? = null,	    // 저가
    val trqu: String? = null,       // 거래량
    val trPrc: String? = null,      // 거래대금
    val lstgStCnt: String? = null,  // 상장주식수
    val mrktTotAmt: String? = null  // 시가총액
)

private fun String?.toDoubleClean(): Double? = this?.replace(",", "")?.toDoubleOrNull()
private fun String?.toLongClean(): Long? = this?.replace(",", "")?.toLongOrNull()

fun StockItemDto.toDomain(): Stock = Stock(
    code = srtnCd ?: isinCd ?: "",
    name = itmsNm ?: "",
    // clpr(종가)를 tradePrice로 사용. API에 따라 적절한 필드로 교체 가능
    tradePrice = clpr.toDoubleClean(),
    // mkp를 등락(절대)으로 사용
    changeAmount = mkp.toDoubleClean(),
    // fltRt는 등락률(%)
    changeRate = fltRt.toDoubleClean(),
    // 거래량
    volume = trqu.toLongClean(),
    baseDate = basDt
)