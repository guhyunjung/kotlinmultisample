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
 * @property repository 국가 데이터를 가져오는 CountryRepository
 */
class CountryViewModel(
    private val repository: CountryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CountryUiState())
    val state: StateFlow<CountryUiState> = _state

    /**
     * 국가 목록 로드
     * 로드 완료 후 filteredCountries도 함께 초기화합니다.
     */
    fun loadCountries() {
        viewModelScope.launch {
            Logger.i(TAG, "loadCountries() 시작")
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val countries = repository.getCountries()
                Logger.i(TAG, "국가 목록 로드 성공: ${countries.size}개")

                // 화면 진입 시 기본 검색어 "kor"로 필터링
                val initialQuery = "kor"
                val filtered = countries.filter { country ->
                    country.name.common.contains(initialQuery, ignoreCase = true) ||
                    country.name.official.contains(initialQuery, ignoreCase = true) ||
                    country.region.contains(initialQuery, ignoreCase = true) ||
                    country.capital.any { it.contains(initialQuery, ignoreCase = true) } ||
                    country.name.nativeName.values.any {
                        it.common.contains(initialQuery, ignoreCase = true)
                    }
                }
                Logger.i(TAG, "\"$initialQuery\" 필터링 결과: ${filtered.size}개")

                _state.value = _state.value.copy(
                    isLoading = false,
                    countries = countries,
                    filteredCountries = filtered,
                    searchQuery = initialQuery
                )
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
     * 검색어로 국가 필터링
     * 국가명(영어/한국어), 지역, 수도 기준으로 검색합니다.
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
                // 한국어 이름도 검색
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

    companion object {
        private const val TAG = "CountryViewModel"
    }
}
