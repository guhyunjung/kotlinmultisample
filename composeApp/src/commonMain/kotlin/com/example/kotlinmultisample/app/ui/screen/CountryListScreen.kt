package com.example.kotlinmultisample.app.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.kotlinmultisample.app.presentation.country.CountryViewModel
import com.example.kotlinmultisample.shared.domain.model.Country

/**
 * 국가 목록 화면
 *
 * Offline-First:
 * - 첫 진입 시 캐시가 있으면 즉시 표시, 없으면 API 호출
 * - 상단 새로고침 버튼으로 강제 API 갱신 가능
 *
 * @param viewModel 국가 ViewModel
 * @param onCountryClick 국가 클릭 시 상세 화면으로 이동
 */
@Composable
fun CountryListScreen(
    viewModel: CountryViewModel,
    onCountryClick: (Country) -> Unit
) {
    val state by viewModel.state.collectAsState()

    // 화면 진입 시 목록 로드 (캐시가 없을 때만 API 호출)
    LaunchedEffect(Unit) {
        if (state.countries.isEmpty()) {
            viewModel.loadCountries()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // ── 상단 바: 검색 + 새로고침 ──────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchBar(
                query = state.searchQuery,
                onQueryChange = { viewModel.search(it) },
                modifier = Modifier.weight(1f)
            )
            // 새로고침 버튼 - 항상 API를 호출하여 최신 데이터로 갱신
            IconButton(
                onClick = { viewModel.refresh() },
                enabled = !state.isRefreshing && !state.isLoading
            ) {
                if (state.isRefreshing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Default.Refresh, contentDescription = "새로고침")
                }
            }
        }

        // ── 결과 수 / 캐시 상태 표시 ─────────────────────────────────────
        if (!state.isLoading && state.countries.isNotEmpty()) {
            Text(
                text = "${state.filteredCountries.size}개 국가",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        // ── 콘텐츠 ────────────────────────────────────────────────────────
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.error != null -> {
                    ErrorContent(
                        message = state.error!!,
                        onRetry = { viewModel.loadCountries() },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.filteredCountries.isEmpty() && state.searchQuery.isNotBlank() -> {
                    Text(
                        text = "'${state.searchQuery}' 검색 결과가 없습니다",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = state.filteredCountries,
                            // cca2가 빈 문자열인 국가(일부 특수 지역)가 있을 수 있으므로
                            // cca3, name.common 순으로 fallback하여 항상 고유한 키 보장
                            key = { country ->
                                country.cca2.ifBlank {
                                    country.cca3.ifBlank {
                                        country.name.common.ifBlank { country.hashCode().toString() }
                                    }
                                }
                            }
                        ) { country ->
                            CountryItem(
                                country = country,
                                onClick = { onCountryClick(country) }
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── 검색바 컴포넌트 ────────────────────────────────────────────────────────────

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = { Text("국가명, 지역, 수도 검색") },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "검색")
        },
        trailingIcon = {
            if (query.isNotBlank()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Clear, contentDescription = "검색어 지우기")
                }
            }
        },
        singleLine = true,
        shape = MaterialTheme.shapes.large
    )
}

// ── 국가 목록 아이템 ──────────────────────────────────────────────────────────

@Composable
private fun CountryItem(
    country: Country,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 국기 이모지
            Text(
                text = country.flag ?: "🏳",
                style = MaterialTheme.typography.headlineMedium
            )

            // 국가 정보
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = country.name.common,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = country.capital.firstOrNull() ?: "수도 정보 없음",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // 지역 뱃지
            SuggestionChip(
                onClick = {},
                label = {
                    Text(
                        text = country.region,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            )
        }
    }
}

// ── 에러 컴포넌트 ─────────────────────────────────────────────────────────────

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "오류가 발생했습니다",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Button(onClick = onRetry) {
            Text("다시 시도")
        }
    }
}
