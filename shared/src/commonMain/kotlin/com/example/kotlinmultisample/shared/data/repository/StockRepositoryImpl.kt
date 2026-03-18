package com.example.kotlinmultisample.shared.data.repository

import com.example.kotlinmultisample.shared.data.remote.datasource.RemoteStockDataSource
import com.example.kotlinmultisample.shared.data.remote.dto.toDomain
import com.example.kotlinmultisample.shared.domain.model.Stock
import com.example.kotlinmultisample.shared.domain.repository.StockRepository

class StockRepositoryImpl(
    private val remote: RemoteStockDataSource
) : StockRepository {
    override suspend fun getStocks(basDt: String?): List<Stock> =
        remote.getStocks(basDt = basDt).map { it.toDomain() }

    override suspend fun getStockByCode(code: String): Stock? =
        remote.getStockByCode(code)?.toDomain()
}

