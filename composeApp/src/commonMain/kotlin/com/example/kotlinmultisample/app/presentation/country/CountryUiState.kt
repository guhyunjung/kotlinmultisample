package com.example.kotlinmultisample.app.presentation.country

import com.example.kotlinmultisample.shared.domain.model.Country

/**
 * 국가 목록 화면 UI 상태
 *
 * @property isLoading     최초 로딩 중 여부 (스켈레톤/스피너 표시)
 * @property isRefreshing  Pull-to-Refresh 등 강제 갱신 중 여부
 * @property countries     전체 국가 목록
 * @property filteredCountries 검색어로 필터링된 국가 목록
 * @property searchQuery   현재 검색어
 * @property error         에러 메시지 (null이면 정상)
 */
data class CountryUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val countries: List<Country> = emptyList(),
    val error: String? = null,
    val searchQuery: String = "",
    val filteredCountries: List<Country> = emptyList()
)
