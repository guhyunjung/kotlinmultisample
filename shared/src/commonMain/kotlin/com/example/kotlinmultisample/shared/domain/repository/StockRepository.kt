package com.example.kotlinmultisample.shared.domain.repository

import com.example.kotlinmultisample.shared.domain.model.Stock

interface StockRepository {
    suspend fun getStocks(basDt: String? = null): List<Stock>
    suspend fun getStockByCode(code: String): Stock?
}

