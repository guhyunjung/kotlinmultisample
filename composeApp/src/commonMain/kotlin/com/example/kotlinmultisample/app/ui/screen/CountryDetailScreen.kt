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
import androidx.compose.ui.unit.dp
import com.example.kotlinmultisample.shared.domain.model.Country

/**
 * 국가 상세 화면
 *
 * @param country 표시할 국가 도메인 모델
 * @param onBack 뒤로가기 콜백
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryDetailScreen(
    country: Country,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "${country.flag ?: ""} ${country.name.common}",
                        maxLines = 1
                    )
                },
                navigationIcon = {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ── 국기 & 기본 정보 ───────────────────────────────────────────
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 국기 이모지 크게 표시
                    Text(
                        text = country.flag ?: "🏳",
                        style = MaterialTheme.typography.displayLarge,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    HorizontalDivider()
                    DetailRow("공식 명칭", country.name.official)
                    DetailRow("일반 명칭", country.name.common)
                    // 원어 이름 (첫 번째만 표시)
                    country.name.nativeName.values.firstOrNull()?.let {
                        DetailRow("원어 명칭", "${it.common} (${it.official})")
                    }
                }
            }

            // ── 지리 정보 ─────────────────────────────────────────────────
            SectionCard(title = "🌍 지리 정보") {
                DetailRow("대륙", country.continents.joinToString())
                DetailRow("지역", country.region)
                country.subregion?.let { DetailRow("세부 지역", it) }
                DetailRow("수도", country.capital.joinToString().ifBlank { "정보 없음" })
                DetailRow("면적", "${"%,.0f".format(country.area)} km²")
                DetailRow("내륙국", if (country.landlocked) "예" else "아니오")
                if (country.borders.isNotEmpty()) {
                    DetailRow("인접 국가", country.borders.joinToString())
                }
                DetailRow("위도/경도", "${country.latlng.getOrNull(0) ?: "-"}, ${country.latlng.getOrNull(1) ?: "-"}")
            }

            // ── 인구/경제 ─────────────────────────────────────────────────
            SectionCard(title = "👥 인구 / 경제") {
                DetailRow("인구", "%,d명".format(country.population))
                country.gini.entries.firstOrNull()?.let {
                    DetailRow("지니계수 (${it.key}년)", "${it.value}")
                }
                if (country.currencies.isNotEmpty()) {
                    val currency = country.currencies.entries.first()
                    DetailRow("통화", "${currency.value.name} (${currency.key}, ${currency.value.symbol})")
                }
            }

            // ── 언어/문화 ─────────────────────────────────────────────────
            SectionCard(title = "🗣️ 언어 / 문화") {
                if (country.languages.isNotEmpty()) {
                    DetailRow("공용어", country.languages.values.joinToString())
                }
                DetailRow("타임존", country.timezones.joinToString())
                country.startOfWeek?.let { DetailRow("주 시작일", it) }
                country.car?.let {
                    DetailRow("운전 방향", if (it.side == "right") "우측 통행" else "좌측 통행")
                }
            }

            // ── 국가 코드 ─────────────────────────────────────────────────
            SectionCard(title = "🔖 국가 코드") {
                DetailRow("ISO alpha-2", country.cca2)
                DetailRow("ISO alpha-3", country.cca3)
                country.ccn3?.let { DetailRow("ISO numeric", it) }
                country.cioc?.let { DetailRow("IOC 코드", it) }
                country.fifa?.let { DetailRow("FIFA 코드", it) }
                DetailRow("UN 회원국", if (country.unMember) "예" else "아니오")
                DetailRow("독립국", when (country.independent) {
                    true -> "예"
                    false -> "아니오"
                    null -> "정보 없음"
                })
                country.tld.takeIf { it.isNotEmpty() }?.let {
                    DetailRow("최상위 도메인", it.joinToString())
                }
            }

            // ── 전화 코드 ─────────────────────────────────────────────────
            country.idd?.let { idd ->
                SectionCard(title = "📞 국제 전화") {
                    val suffixes = idd.suffixes.joinToString()
                    DetailRow("전화 코드", "${idd.root}$suffixes")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ── 공통 컴포넌트 ─────────────────────────────────────────────────────────────

/**
 * 섹션 카드 (제목 + 내용)
 */
@Composable
private fun SectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
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
            HorizontalDivider()
            content()
        }
    }
}

/**
 * 라벨 + 값 한 행
 */
@Composable
private fun DetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
            modifier = Modifier.weight(0.6f)
        )
    }
}

