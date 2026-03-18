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
    val numOfRows: Int? = null,         // 한 페이지 결과 수
    val pageNo: Int? = null,            // 페이지 번호
    val totalCount: Int? = null         // 전체 결과 수
)

data class StockItemsDto(
    val item: List<StockItemDto>? = null
)