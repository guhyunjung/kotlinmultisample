package com.example.kotlinmultisample.app.presentation.country

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinmultisample.shared.domain.repository.CountryRepository
import com.example.kotlinmultisample.shared.util.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * 국가 목록/상세 화면 ViewModel
 *
 * Offline-First 전략:
 * - loadCountries(): 캐시가 있으면 즉시 표시, 없으면 API 호출
 * - refresh(): 강제로 API를 호출하여 최신 데이터로 갱신
 *
 * @property repository 국가 데이터를 가져오는 CountryRepository
 */
class CountryViewModel(
    private val repository: CountryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CountryUiState())
    val state: StateFlow<CountryUiState> = _state

    /**
     * 국가 목록 로드 (Cache-First)
     *
     * - 로컬 캐시가 있으면 즉시 반환하여 빠른 UI를 표시합니다.
     * - 캐시가 없으면 API를 호출합니다.
     * - 로드 완료 후 기본 검색어 "kor"로 필터링합니다.
     */
    fun loadCountries() {
        viewModelScope.launch {
            Logger.i(TAG, "loadCountries() 시작")
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val countries = repository.getCountries()
                Logger.i(TAG, "국가 목록 로드 성공: ${countries.size}개")
                applyCountriesWithQuery(countries, _state.value.searchQuery.ifBlank { "kor" })
            } catch (e: Exception) {
                Logger.e(TAG, "국가 목록 로드 실패: ${e.message}", e)
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "알 수 없는 오류가 발생했습니다"
                )
            }
        }
    }

    /**
     * 강제 갱신 (항상 API 호출)
     *
     * Pull-to-Refresh 또는 수동 새로고침 버튼에서 호출합니다.
     * API에서 최신 데이터를 가져와 로컬 DB를 갱신합니다.
     */
    fun refresh() {
        viewModelScope.launch {
            Logger.i(TAG, "refresh() 시작 - 강제 API 호출")
            _state.value = _state.value.copy(isRefreshing = true, error = null)
            try {
                val countries = repository.refreshCountries()
                Logger.i(TAG, "강제 갱신 성공: ${countries.size}개")
                applyCountriesWithQuery(countries, _state.value.searchQuery)
            } catch (e: Exception) {
                Logger.e(TAG, "강제 갱신 실패: ${e.message}", e)
                _state.value = _state.value.copy(
                    isRefreshing = false,
                    error = e.message ?: "갱신 중 오류가 발생했습니다"
                )
            }
        }
    }

    /**
     * 검색어로 국가 필터링
     *
     * 국가명(영어/원어), 지역, 수도 기준으로 검색합니다.
     * 기존 전체 목록을 유지하면서 필터링 결과만 업데이트합니다.
     *
     * @param query 검색어
     */
    fun search(query: String) {
        Logger.d(TAG, "search() query=\"$query\"")
        val filtered = if (query.isBlank()) {
            _state.value.countries
        } else {
            _state.value.countries.filter { country ->
                country.name.common.contains(query, ignoreCase = true) ||
                country.name.official.contains(query, ignoreCase = true) ||
                country.region.contains(query, ignoreCase = true) ||
                country.capital.any { it.contains(query, ignoreCase = true) } ||
                country.name.nativeName.values.any {
                    it.common.contains(query, ignoreCase = true)
                }
            }
        }
        _state.value = _state.value.copy(
            searchQuery = query,
            filteredCountries = filtered
        )
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 내부 헬퍼
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * 전체 국가 목록을 상태에 반영하고, 현재 검색어로 필터링합니다.
     */
    private fun applyCountriesWithQuery(countries: List<com.example.kotlinmultisample.shared.domain.model.Country>, query: String) {
        val filtered = if (query.isBlank()) {
            countries
        } else {
            countries.filter { country ->
                country.name.common.contains(query, ignoreCase = true) ||
                country.name.official.contains(query, ignoreCase = true) ||
                country.region.contains(query, ignoreCase = true) ||
                country.capital.any { it.contains(query, ignoreCase = true) } ||
                country.name.nativeName.values.any {
                    it.common.contains(query, ignoreCase = true)
                }
            }
        }
        Logger.i(TAG, "\"$query\" 필터링 결과: ${filtered.size}개")
        _state.value = _state.value.copy(
            isLoading = false,
            isRefreshing = false,
            countries = countries,
            filteredCountries = filtered,
            searchQuery = query
        )
    }

    companion object {
        private const val TAG = "CountryViewModel"
    }
}
