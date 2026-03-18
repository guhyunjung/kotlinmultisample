package com.example.kotlinmultisample.shared.data.remote.datasource

import com.example.kotlinmultisample.shared.data.remote.dto.StockItemDto

interface RemoteStockDataSource {
    suspend fun getStocks(basDt: String? = null, numOfRows: Int = 100, pageNo: Int = 1): List<StockItemDto>
    suspend fun getStockByCode(code: String): StockItemDto?
}

