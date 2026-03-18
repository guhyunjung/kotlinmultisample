package com.example.kotlinmultisample.shared.data.remote.api

import com.example.kotlinmultisample.shared.data.remote.dto.StockApiResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApiService {

    // TODO: 실제 엔드포인트 이름과 파라미터는 공공데이터포털 문서에 맞게 수정하세요.
    @GET("getStockPriceInfo")
    suspend fun getStockPrices(
        @Query("serviceKey", encoded = true) serviceKey: String,
        @Query("numOfRows") numOfRows: Int = 100,
        @Query("pageNo") pageNo: Int = 1,
        @Query("resultType", encoded = true) resultType: String = "json",
        @Query("basDt") basDt: String? = null,
        @Query("likeSrtnCd") likeSrtnCd: String? = null
    ): StockApiResponseDto
}

