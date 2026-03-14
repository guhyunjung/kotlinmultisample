package com.example.kotlinmultisample.app.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
 * 국가 목록 화면 컴포저블
 *
 * 이 화면은 서버 또는 로컬 캐시로부터 국가 목록을 가져와 표시합니다.
 * Offline-First 전략을 사용하며, 검색 및 당겨서 새로고침 기능을 제공합니다.
 *
 * @param viewModel 국가 데이터를 관리하는 ViewModel
 * @param onMenuClick 햄버거 버튼 클릭 시 드로어를 여는 콜백
 * @param onCountryClick 특정 국가 선택 시 상세 화면으로 이동하는 콜백
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryListScreen(
	viewModel: CountryViewModel,
	onMenuClick: () -> Unit = {},
	onCountryClick: (Country) -> Unit
) {
	// ViewModel의 상태를 관찰 가능한 Compose State로 변환
	val state by viewModel.state.collectAsState()

	// 화면 진입 시 초기 데이터 로드 (이미 데이터가 있다면 생략하여 불필요한 네트워크 요청 방지)
	LaunchedEffect(Unit) {
		if (state.countries.isEmpty()) {
			viewModel.loadCountries()
		}
	}

	Scaffold(
		modifier = Modifier.fillMaxSize(),
		topBar = {
			// 검색 기능과 새로고침 버튼이 포함된 커스텀 앱 바
			CountryListAppBar(
				query = state.searchQuery,
				onQueryChange = { viewModel.search(it) },
				isRefreshing = state.isRefreshing,
				isLoading = state.isLoading,
				onRefresh = { viewModel.refresh() },
				onMenuClick = onMenuClick
			)
		}
	) { innerPadding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
		) {
			// 검색 결과 개수 및 상태 요약 표시
			if (!state.isLoading && state.countries.isNotEmpty()) {
				Text(
					text = "${state.filteredCountries.size}개 국가 검색됨",
					style = MaterialTheme.typography.labelMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant,
					modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
				)
			}

			// 콘텐츠 영역 (새로고침 기능을 포함한 리스트)
			val pullRefreshState = rememberPullToRefreshState()
			
			PullToRefreshBox(
				isRefreshing = state.isRefreshing,
				onRefresh = { viewModel.refresh() },
				state = pullRefreshState,
				modifier = Modifier.fillMaxSize()
			) {
				when {
					// 1. 초기 데이터 로딩 상태 (새로고침이 아닐 때만 중앙 인디케이터 표시)
					state.isLoading && !state.isRefreshing -> {
						CircularProgressIndicator(
							modifier = Modifier.align(Alignment.Center)
						)
					}

					// 2. 에러 발생 시 에러 메시지 및 재시도 버튼 표시
					state.error != null -> {
						ErrorContent(
							message = state.error!!,
							onRetry = { viewModel.loadCountries() },
							modifier = Modifier.align(Alignment.Center)
						)
					}

					// 3. 검색 결과가 비어있는 상태
					state.filteredCountries.isEmpty() && state.searchQuery.isNotBlank() -> {
						Text(
							text = "'${state.searchQuery}'에 매칭되는 국가가 없습니다.",
							style = MaterialTheme.typography.bodyLarge,
							color = MaterialTheme.colorScheme.onSurfaceVariant,
							modifier = Modifier.align(Alignment.Center)
						)
					}

					// 4. 정상적인 국가 목록 표시
					else -> {
						LazyColumn(
							contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
							verticalArrangement = Arrangement.spacedBy(8.dp),
							modifier = Modifier.fillMaxSize()
						) {
							items(
								items = state.filteredCountries,
								// 고유 키 설정을 통해 리스트 업데이트 성능 최적화
								key = { country ->
									country.cca3.ifBlank { country.name.common }
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
}

/**
 * 국가 목록 화면 전용 상단 앱 바
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CountryListAppBar(
	query: String,
	onQueryChange: (String) -> Unit,
	isRefreshing: Boolean,
	isLoading: Boolean,
	onRefresh: () -> Unit,
	onMenuClick: () -> Unit = {},
	modifier: Modifier = Modifier
) {
	TopAppBar(
		modifier = modifier,
		title = {
			// 앱 바 내부에 검색창 배치
			SearchBar(
				query = query,
				onQueryChange = onQueryChange,
				modifier = Modifier.fillMaxWidth()
			)
		},
		actions = {
			// 수동 새로고침 버튼
			IconButton(
				onClick = onRefresh,
				enabled = !isRefreshing && !isLoading // 로딩 중에는 클릭 비활성화
			) {
				if (isRefreshing) {
					// 새로고침 중에는 작게 로딩 표시
					CircularProgressIndicator(
						modifier = Modifier.size(20.dp),
						strokeWidth = 2.dp
					)
				} else {
					Icon(Icons.Default.Refresh, contentDescription = "새로고침")
				}
			}
            
            // 드로어를 열기 위한 햄버거 아이콘 - 우측 배치
			IconButton(onClick = onMenuClick) {
				Icon(Icons.Default.Menu, contentDescription = "메뉴 열기")
			}
		},
		colors = TopAppBarDefaults.topAppBarColors(
			containerColor = MaterialTheme.colorScheme.surface
		)
	)
}

/**
 * 국가 검색을 위한 커스텀 텍스트 필드
 */
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
		placeholder = { Text("국가, 수도, 지역 검색...") },
		leadingIcon = {
			Icon(Icons.Default.Search, contentDescription = "검색 아이콘")
		},
		trailingIcon = {
			// 검색어가 있을 때만 삭제 버튼(X) 표시
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

/**
 * 국가 목록에 표시될 개별 항목 카드
 */
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
			// 국기 이모지 또는 기본 아이콘
			Text(
				text = country.flag ?: "🏳️",
				style = MaterialTheme.typography.headlineMedium
			)

			// 국가 기본 정보 (이름, 수도)
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

			// 지역(대륙) 표시 뱃지
			SuggestionChip(
				onClick = {}, // 클릭 기능 없음
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

/**
 * 데이터 로딩 실패 시 표시되는 에러 화면
 */
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
			text = "데이터를 불러오지 못했습니다.",
			style = MaterialTheme.typography.titleMedium,
			color = MaterialTheme.colorScheme.error
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
