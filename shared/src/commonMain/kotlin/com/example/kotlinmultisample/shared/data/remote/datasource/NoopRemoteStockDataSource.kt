package com.example.kotlinmultisample.shared.data.remote.datasource

import com.example.kotlinmultisample.shared.data.remote.dto.StockItemDto

/**
 * serviceKey가 제공되지 않았을 때 안전하게 동작하는 No-op 구현
 * 실제 요청을 수행하지 않고 빈 결과를 반환합니다.
 */
class NoopRemoteStockDataSource : RemoteStockDataSource {
    override suspend fun getStocks(basDt: String?, numOfRows: Int, pageNo: Int) = emptyList<StockItemDto>()
    override suspend fun getStockByCode(code: String) = null
}

