package com.example.kotlinmultisample.app.presentation.country

import com.example.kotlinmultisample.shared.domain.model.Country

/**
 * 국가 목록 화면 UI 상태
 */
data class CountryUiState(
    val isLoading: Boolean = false,
    val countries: List<Country> = emptyList(),
    val error: String? = null,
    /** 검색어 */
    val searchQuery: String = "",
    /** 검색 필터링된 결과 */
    val filteredCountries: List<Country> = emptyList()
)

