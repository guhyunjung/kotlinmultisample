package com.example.kotlinmultisample.shared.data.remote.dto

import com.example.kotlinmultisample.shared.domain.model.Stock

/**
 * 공공데이터포털 GetStockSecuritiesInfoService 실제 응답 필드에 맞춘 DTO
 * 예시 응답(item) 필드:
 *  - basDt, srtnCd, isinCd, itmsNm, mrktCtg, clpr, vs, fltRt, mkp,
 *    hipr, lopr, trqu, trPrc, lstgStCnt, mrktTotAmt
 */
data class StockItemDto(
    val basDt: String? = null,
    val srtnCd: String? = null,
    val isinCd: String? = null,
    val itmsNm: String? = null,
    val mrktCtg: String? = null,
    val clpr: String? = null,    // 종가(또는 현재가)
    val vs: String? = null,
    val fltRt: String? = null,   // 등락률
    val mkp: String? = null,     // 등락(절대)
    val hipr: String? = null,
    val lopr: String? = null,
    val trqu: String? = null,    // 거래량
    val trPrc: String? = null,   // 거래대금
    val lstgStCnt: String? = null,
    val mrktTotAmt: String? = null
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

