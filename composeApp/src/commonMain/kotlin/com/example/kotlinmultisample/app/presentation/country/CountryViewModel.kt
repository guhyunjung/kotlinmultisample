package com.example.kotlinmultisample.app.presentation.country

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinmultisample.shared.domain.model.Country
import com.example.kotlinmultisample.shared.domain.usecase.GetCountriesUseCase
import com.example.kotlinmultisample.shared.domain.usecase.RefreshCountriesUseCase
import com.example.kotlinmultisample.shared.domain.usecase.SearchCountriesUseCase
import co.touchlab.kermit.Logger
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
 * UseCase 기반 설계:
 * - [GetCountriesUseCase]   : 목록 조회 (Cache-First)
 * - [RefreshCountriesUseCase]: 강제 갱신
 * - [SearchCountriesUseCase] : 검색/필터링 (순수 도메인 로직)
 *
 * @property getCountriesUseCase 국가 목록 조회 UseCase
 * @property refreshCountriesUseCase 국가 목록 강제 갱신 UseCase
 * @property searchCountriesUseCase 국가 검색 UseCase
 */
class CountryViewModel(
    private val getCountriesUseCase: GetCountriesUseCase,
    private val refreshCountriesUseCase: RefreshCountriesUseCase,
    private val searchCountriesUseCase: SearchCountriesUseCase
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
            Logger.i(TAG) { "loadCountries() 시작" }
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val countries = getCountriesUseCase()
                Logger.i(TAG) { "국가 목록 로드 성공: ${countries.size}개" }
                applyCountriesWithQuery(countries, _state.value.searchQuery.ifBlank { "kor" })
            } catch (e: Exception) {
                Logger.e(TAG, e) { "국가 목록 로드 실패: ${e.message}" }
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
            Logger.i(TAG) { "refresh() 시작 - 강제 API 호출" }
            _state.value = _state.value.copy(isRefreshing = true, error = null)
            try {
                val countries = refreshCountriesUseCase()
                Logger.i(TAG) { "강제 갱신 성공: ${countries.size}개" }
                applyCountriesWithQuery(countries, _state.value.searchQuery)
            } catch (e: Exception) {
                Logger.e(TAG, e) { "강제 갱신 실패: ${e.message}" }
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
     * [SearchCountriesUseCase]에 검색 로직을 위임합니다.
     * 국가명(영어/원어), 지역, 수도 기준으로 검색합니다.
     *
     * @param query 검색어
     */
    fun search(query: String) {
        Logger.d(TAG) { "search() query=\"$query\"" }
        val filtered = searchCountriesUseCase(_state.value.countries, query)
        _state.value = _state.value.copy(
            searchQuery = query,
            filteredCountries = filtered
        )
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 내부 헬퍼
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * 전체 국가 목록을 상태에 반영하고, [SearchCountriesUseCase]로 필터링합니다.
     */
    private fun applyCountriesWithQuery(countries: List<Country>, query: String) {
        val filtered = searchCountriesUseCase(countries, query)
        Logger.i(TAG) { "\"$query\" 필터링 결과: ${filtered.size}개" }
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
