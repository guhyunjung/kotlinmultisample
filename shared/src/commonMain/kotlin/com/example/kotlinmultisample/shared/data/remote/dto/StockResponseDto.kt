package com.example.kotlinmultisample.shared.data.remote.dto

data class StockApiResponseDto(
    val response: StockResponseWrapperDto? = null
)

data class StockResponseWrapperDto(
    val header: Any? = null,
    val body: StockBodyDto? = null
)

data class StockBodyDto(
    val items: StockItemsDto? = null,
    val numOfRows: Int? = null,
    val pageNo: Int? = null,
    val totalCount: Int? = null
)

data class StockItemsDto(
    val item: List<StockItemDto>? = null
)

