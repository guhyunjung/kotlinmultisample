package com.example.kotlinmultisample.shared.data.remote.datasource

import com.example.kotlinmultisample.shared.data.remote.api.StockApiService
import com.example.kotlinmultisample.shared.data.remote.dto.StockItemDto
import com.example.kotlinmultisample.shared.data.remote.dto.StockApiResponseDto
import retrofit2.HttpException

class RemoteStockDataSourceImpl(
    private val apiService: StockApiService,
    private val serviceKey: String
) : RemoteStockDataSource {

    override suspend fun getStocks(basDt: String?, numOfRows: Int, pageNo: Int): List<StockItemDto> {
        val resp: StockApiResponseDto = apiService.getStockPrices(
            serviceKey = serviceKey,
            numOfRows = numOfRows,
            pageNo = pageNo,
            basDt = basDt
        )
        return resp.response?.body?.items?.item ?: emptyList()
    }

    override suspend fun getStockByCode(code: String): StockItemDto? {
        return try {
            val resp = apiService.getStockPrices(serviceKey = serviceKey, numOfRows = 1, pageNo = 1, likeSrtnCd = code)
            resp.response?.body?.items?.item?.firstOrNull()
        } catch (e: HttpException) {
            if (e.code() == 404) null else throw e
        }
    }
}

