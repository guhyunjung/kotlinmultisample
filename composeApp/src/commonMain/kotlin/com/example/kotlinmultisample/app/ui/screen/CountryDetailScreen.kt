package com.example.kotlinmultisample.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.kotlinmultisample.shared.domain.model.Country

/**
 * 국가 상세 정보 화면 컴포저블
 *
 * 선택된 국가의 국기, 지리 정보, 인구, 경제, 문화 등 상세한 데이터를 카드 형태로 보여줍니다.
 *
 * @param country 화면에 표시할 국가 데이터 객체
 * @param onBack 이전 화면으로 돌아가기 위한 콜백
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryDetailScreen(
	country: Country,
	onBack: () -> Unit
) {
	Scaffold(
		topBar = {
			// 상단 앱 바: 국기와 국가명을 제목으로 표시
			TopAppBar(
				title = {
					Text(
						text = "${country.flag ?: ""} ${country.name.common}",
						maxLines = 1,
						overflow = TextOverflow.Ellipsis
					)
				},
				navigationIcon = {
					// 뒤로가기 버튼
					IconButton(onClick = onBack) {
						Icon(
							imageVector = Icons.AutoMirrored.Filled.ArrowBack,
							contentDescription = "뒤로가기"
						)
					}
				}
			)
		}
	) { innerPadding ->
		// 상세 정보를 담은 스크롤 가능한 컬럼
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
				.verticalScroll(rememberScrollState())
				.padding(16.dp),
			verticalArrangement = Arrangement.spacedBy(12.dp)
		) {
			// ── 1. 국기 및 명칭 요약 ───────────────────────────────────────────
			Card(modifier = Modifier.fillMaxWidth()) {
				Column(
					modifier = Modifier.padding(16.dp),
					verticalArrangement = Arrangement.spacedBy(8.dp)
				) {
					// 국기 이모지를 크게 중앙에 표시
					Text(
						text = country.flag ?: "🏳️",
						style = MaterialTheme.typography.displayLarge,
						modifier = Modifier.align(Alignment.CenterHorizontally)
					)
					HorizontalDivider()
					// 공식 명칭 및 일반 명칭 표시
					DetailRow("공식 명칭", country.name.official)
					DetailRow("일반 명칭", country.name.common)
					// 원어 이름이 있는 경우 첫 번째 항목 표시
					country.name.nativeName.values.firstOrNull()?.let {
						DetailRow("원어 명칭", "${it.common} (${it.official})")
					}
				}
			}

			// ── 2. 지리 정보 섹션 ─────────────────────────────────────────────────
			SectionCard(title = "🌍 지리 정보") {
				DetailRow("대륙", country.continents.joinToString())
				DetailRow("지역", country.region)
				country.subregion?.let { DetailRow("세부 지역", it) }
				DetailRow("수도", country.capital.joinToString().ifBlank { "정보 없음" })
				DetailRow("면적", "${"%,.0f".format(country.area)} km²")
				DetailRow("내륙국 여부", if (country.landlocked) "예" else "아니오")
				if (country.borders.isNotEmpty()) {
					DetailRow("인접 국가", country.borders.joinToString())
				}
				DetailRow("위도/경도", "${country.latlng.getOrNull(0) ?: "-"}, ${country.latlng.getOrNull(1) ?: "-"}")
			}

			// ── 3. 인구 및 경제 정보 섹션 ───────────────────────────────────────────
			SectionCard(title = "👥 인구 / 경제") {
				DetailRow("인구수", "%,d 명".format(country.population))
				country.gini.entries.firstOrNull()?.let {
					DetailRow("지니계수 (${it.key}년)", "${it.value}")
				}
				if (country.currencies.isNotEmpty()) {
					val currency = country.currencies.entries.first()
					DetailRow("사용 통화", "${currency.value.name} (${currency.key}, ${currency.value.symbol})")
				}
			}

			// ── 4. 언어 및 문화 정보 섹션 ───────────────────────────────────────────
			SectionCard(title = "🗣️ 언어 / 문화") {
				if (country.languages.isNotEmpty()) {
					DetailRow("사용 언어", country.languages.values.joinToString())
				}
				DetailRow("시간대(Timezone)", country.timezones.joinToString())
				country.startOfWeek?.let { DetailRow("주 시작 요일", it.replaceFirstChar { it.uppercase() }) }
				country.car?.let {
					DetailRow("운전석 위치", if (it.side == "right") "우측" else "좌측")
				}
			}

			// ── 5. 국가 코드 섹션 ─────────────────────────────────────────────────
			SectionCard(title = "🔖 국가 코드 및 기타") {
				DetailRow("ISO alpha-2", country.cca2)
				DetailRow("ISO alpha-3", country.cca3)
				country.ccn3?.let { DetailRow("ISO numeric", it) }
				country.cioc?.let { DetailRow("IOC 코드", it) }
				country.fifa?.let { DetailRow("FIFA 코드", it) }
				DetailRow("UN 회원 여부", if (country.unMember) "예" else "아니오")
				DetailRow(
					"독립 국가", when (country.independent) {
						true -> "예"
						false -> "아니오"
						null -> "정보 없음"
					}
				)
				country.tld.takeIf { it.isNotEmpty() }?.let {
					DetailRow("인터넷 도메인", it.joinToString())
				}
			}

			// ── 6. 국제 전화 코드 섹션 ───────────────────────────────────────────
			country.idd?.let { idd ->
				SectionCard(title = "📞 국제 전화") {
					val suffixes = idd.suffixes.joinToString()
					DetailRow("전화 코드", "${idd.root}$suffixes")
				}
			}

			// 하단 여백 추가
			Spacer(modifier = Modifier.height(16.dp))
		}
	}
}

/**
 * 상세 화면의 정보를 카테고리별로 묶어서 보여주는 카드 컴포넌트
 *
 * @param title 섹션 제목
 * @param content 섹션 내부에 표시될 내용 (상세 행들)
 */
@Composable
private fun SectionCard(
	title: String,
	content: @Composable ColumnScope.() -> Unit
) {
	Card(
		modifier = Modifier.fillMaxWidth(),
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
		)
	) {
		Column(
			modifier = Modifier.padding(16.dp),
			verticalArrangement = Arrangement.spacedBy(8.dp)
		) {
			Text(
				text = title,
				style = MaterialTheme.typography.titleSmall,
				fontWeight = FontWeight.Bold,
				color = MaterialTheme.colorScheme.primary
			)
			HorizontalDivider(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
			content()
		}
	}
}

/**
 * 정보를 라벨과 값의 형태로 보여주는 한 행
 *
 * @param label 정보의 이름 (왼쪽)
 * @param value 정보의 실제 값 (오른쪽)
 */
@Composable
private fun DetailRow(
	label: String,
	value: String
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 2.dp),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.Top
	) {
		Text(
			text = label,
			style = MaterialTheme.typography.bodySmall,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			modifier = Modifier.weight(0.4f)
		)
		Text(
			text = value,
			style = MaterialTheme.typography.bodySmall,
			fontWeight = FontWeight.Medium,
			color = MaterialTheme.colorScheme.onSurface,
			modifier = Modifier.weight(0.6f)
		)
	}
}
