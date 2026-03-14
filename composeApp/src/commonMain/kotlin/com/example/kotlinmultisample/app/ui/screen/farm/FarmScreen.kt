package com.example.kotlinmultisample.app.ui.screen.farm

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinmultisample.app.presentation.farm.FarmViewModel
import kotlinx.coroutines.delay
import org.koin.compose.koinInject

data class Seed(val name: String, val emoji: String, val pct: Double)

@Composable
fun FarmScreen(onMenuClick: () -> Unit = {}) {
	// Koin을 통해 싱글톤 ViewModel 주입 (앱 실행 중 튜토리얼 상태 유지)
	val viewModel = koinInject<FarmViewModel>()
	val showTutorial by viewModel.showTutorial.collectAsState()

	// 오버레이 상태 관리
	var selectedSeed by remember { mutableStateOf<Seed?>(null) }
	var showWarehouse by remember { mutableStateOf(false) }
	var showDiary by remember { mutableStateOf(false) }

	val seeds = remember {
		listOf(
			Seed("한화에어로", "🌾", 10.5),
			Seed("삼성전자", "🌿", 8.4),
			Seed("카카오", "🍂", -3.2),
			Seed("NAVER", "🌱", 5.1),
			Seed("LG에너지", "🥀", -11.2)
		)
	}

	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(FarmColors.getSky())
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = {}
			) // 상위 레이어 터치 차단용
	) {
		Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
			// 1. 하늘 섹션 (구름 애니메이션 포함)
			SkySection(onMenuClick = onMenuClick)

			// 2. 브로커 탭 (간략 구현)
			BrokerTabs()

			// 3. 농장 영역 (메인 콘텐츠)
			Column(
				modifier = Modifier
					.weight(1f) // 남은 공간을 모두 차지하도록 변경
					.fillMaxWidth()
					.background(FarmColors.getGround())
					.padding(horizontal = 12.dp)
			) {
				Spacer(modifier = Modifier.height(8.dp))
				// 요약 바
				SummaryBar()
				Spacer(modifier = Modifier.height(8.dp))
				// 밭 그리드
				FieldGrid(seeds, onSeedClick = { selectedSeed = it })
			}

			// 4. 하단 메뉴바 (복구됨)
			BottomMenuBar(
				onWarehouseClick = { showWarehouse = true },
				onDiaryClick = { showDiary = true }
			)
		}

		// 튜토리얼 오버레이
		AnimatedVisibility(
			visible = showTutorial, // ViewModel 상태 사용
			enter = fadeIn() + scaleIn(initialScale = 0.9f),
			exit = fadeOut() + scaleOut(targetScale = 0.9f)
		) {
			TutorialOverlay(onDismiss = { viewModel.completeTutorial() }) // ViewModel 액션으로 변경
		}

		// 상세 정보 오버레이
		AnimatedVisibility(
			visible = selectedSeed != null,
			enter = slideInVertically(initialOffsetY = { it }),
			exit = slideOutVertically(targetOffsetY = { it })
		) {
			selectedSeed?.let { seed ->
				DetailOverlay(seed = seed, onDismiss = { selectedSeed = null })
			}
		}

		// 창고 오버레이
		AnimatedVisibility(
			visible = showWarehouse,
			enter = slideInVertically(initialOffsetY = { it }),
			exit = slideOutVertically(targetOffsetY = { it })
		) {
			WarehouseOverlay(onDismiss = { showWarehouse = false })
		}

		// 일기 오버레이
		AnimatedVisibility(
			visible = showDiary,
			enter = slideInVertically(initialOffsetY = { it }),
			exit = slideOutVertically(targetOffsetY = { it })
		) {
			DiaryOverlay(onDismiss = { showDiary = false })
		}
	}
}

@Composable
fun TutorialOverlay(onDismiss: () -> Unit) {
	val tutSteps = listOf(
		"안녕하세요! 저는 농장지기 씨앗이에요. 여러분의 첫 투자 여정을 함께할게요!",
		"주식이란 회사의 아주 작은 조각이에요. 삼성전자 1주를 사면 삼성의 주인이 되는 거예요!",
		"한 달 커피값으로 주식 1주를 살 수 있어요. 커피는 마시면 없어지지만 주식은 남아요!",
		"단, 모든 투자의 책임은 농부인 당신에게 있어요. 자, 이제 씨앗을 사러 가볼까요?"
	)
	var stepIdx by remember { mutableStateOf(0) }
	val currentText = tutSteps[stepIdx]

	// 타이핑 효과 애니메이션
	var animatedText by remember { mutableStateOf("") }
	LaunchedEffect(stepIdx) {
		animatedText = ""
		currentText.forEach { char ->
			animatedText += char
			delay(50)
		}
	}

	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(Color.Black.copy(alpha = 0.7f))
			.statusBarsPadding()
			.padding(24.dp),
		contentAlignment = Alignment.Center
	) {
		Column(horizontalAlignment = Alignment.CenterHorizontally) {
			// 농부 아이콘을 카트 밖으로 분리하여 크게 표시
			Text("👨‍🌾", fontSize = 64.sp)
			Spacer(modifier = Modifier.height(16.dp))

			PixelCard(containerColor = FarmColors.getNight(), borderColor = FarmColors.getMoon()) {
				// 상단: 이름과 진행 단계 표시
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				) {
					Text("농장지기 씨앗", color = FarmColors.getMoon(), fontSize = 14.sp, fontWeight = FontWeight.Bold)
					Text("${stepIdx + 1} / ${tutSteps.size}", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
				}

				Spacer(modifier = Modifier.height(12.dp))
				Text(
					text = animatedText,
					color = Color.White,
					fontSize = 14.sp,
					lineHeight = 20.sp,
					modifier = Modifier.fillMaxWidth().heightIn(min = 60.dp)
				)
				Spacer(modifier = Modifier.height(16.dp))

				Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
					PixelButton(
						text = if (stepIdx < tutSteps.size - 1) "다음 →" else "농장 시작하기 🌾",
						containerColor = FarmColors.getGrass(),
						onClick = {
							if (stepIdx < tutSteps.size - 1) stepIdx++ else onDismiss()
						},
						modifier = Modifier.weight(1f)
					)
					if (stepIdx < tutSteps.size - 1) {
						PixelButton(
							text = "Skip",
							containerColor = FarmColors.getDarkGround(),
							contentColor = FarmColors.getMoon(),
							onClick = onDismiss,
							modifier = Modifier.width(60.dp)
						)
					}
				}
			}
		}
	}
}

@Composable
fun SkySection(onMenuClick: () -> Unit = {}) {
	val infiniteTransition = rememberInfiniteTransition()
	val cloudOffset by infiniteTransition.animateFloat(
		initialValue = 0f,
		targetValue = 20f,
		animationSpec = infiniteRepeatable(
			animation = tween(2000, easing = LinearOutSlowInEasing),
			repeatMode = RepeatMode.Reverse
		)
	)

	Box(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp)
	) {
		// 타이틀 + 레벨 (중앙 정렬)
		Row(
			modifier = Modifier.align(Alignment.Center),
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				"🌾 주린이의 주식농장",
				color = FarmColors.getTextOnSky(),
				fontWeight = FontWeight.Bold,
				fontSize = 16.sp
			)

			Spacer(modifier = Modifier.width(8.dp))

			// 레벨 (타이틀 옆으로 이동)
			Surface(
				shape = PixelShape,
				color = FarmColors.getCloud().copy(alpha = 0.6f)
			) {
				Text(
					"Lv.3 🌱",
					modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
					fontSize = 11.sp,
					color = FarmColors.getTextOnSky()
				)
			}
		}

		// 좌측 요소 (구름)
		Surface(
			shape = PixelShape,
			color = FarmColors.getCloud(),
			modifier = Modifier
				.align(Alignment.CenterStart)
				.offset(y = cloudOffset.dp)
		) {
			Text(
				"☁️",
				modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
				fontSize = 12.sp
			)
		}

		// 우측 요소 (햄버거 버튼)
		Surface(
			shape = PixelShape,
			color = FarmColors.getCloud(),
			modifier = Modifier
				.align(Alignment.CenterEnd)
				.clickable { onMenuClick() }
		) {
			Text(
				"☰",
				modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
				fontSize = 18.sp,
				color = FarmColors.getTextOnSky(),
				fontWeight = FontWeight.Bold
			)
		}
	}
}

@Composable
fun BrokerTabs() {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 12.dp),
		horizontalArrangement = Arrangement.spacedBy(2.dp) // 간격 줄임
	) {
		PixelButton("토스농장", modifier = Modifier.weight(1f), containerColor = FarmColors.getGrass(), onClick = {})
		PixelButton("키움밭", modifier = Modifier.weight(1f), containerColor = FarmColors.getGround(), onClick = {})
		PixelButton("전체🌾", modifier = Modifier.weight(1f), containerColor = FarmColors.getGround(), onClick = {})
		PixelButton(
			"+",
			modifier = Modifier.width(40.dp),
			containerColor = FarmColors.getDarkGround(),
			contentColor = FarmColors.getGold(),
			onClick = {})
	}
}

@Composable
fun BottomMenuBar(onWarehouseClick: () -> Unit = {}, onDiaryClick: () -> Unit = {}) {
	Column(modifier = Modifier.fillMaxWidth()) {
		// 상단 구분선: 진한 잔디색 띠를 둘러 밭과 메뉴 영역을 명확히 구분 (땅의 단면 느낌)
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.height(4.dp)
				.background(FarmColors.getDarkGrass())
		)

		Surface(
			modifier = Modifier.fillMaxWidth(),
			color = FarmColors.getDarkGround(),
			shape = RectangleShape // 라운드 제거
		) {
			Row(
				modifier = Modifier.padding(vertical = 12.dp),
				horizontalArrangement = Arrangement.SpaceAround
			) {
				BottomMenuItem("🏠", "내 밭")
				BottomMenuItem("📦", "창고", onClick = onWarehouseClick)
				BottomMenuItem("📔", "농부일기", onClick = onDiaryClick)
				BottomMenuItem("🎒", "가방")
			}
		}
	}
}

@Composable
fun BottomMenuItem(icon: String, label: String, onClick: () -> Unit = {}) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier.clickable(onClick = onClick)
	) {
		Text(icon, fontSize = 20.sp)
		Spacer(modifier = Modifier.height(4.dp))
		Text(label, fontSize = 10.sp, color = FarmColors.getGold(), fontWeight = FontWeight.Bold)
	}
}

@Composable
fun SummaryBar() {
	Surface(
		modifier = Modifier
			.fillMaxWidth()
			.background(FarmColors.getGrass(), PixelShape),
		color = FarmColors.getGrass(),
		shape = PixelShape,
		border = BorderStroke(2.dp, FarmColors.getDarkGrass())
	) {
		Row(
			modifier = Modifier.padding(12.dp),
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			SummaryItem("총 투자금", "500,000원")
			SummaryItem("평가금액", "548,000원")
			SummaryItem("총 손익", "+48,000", color = FarmColors.getGold())
		}
	}
}

@Composable
fun SummaryItem(label: String, value: String, color: Color = Color.White) {
	Column(horizontalAlignment = Alignment.CenterHorizontally) {
		Text(label, fontSize = 10.sp, color = Color.White.copy(alpha = 0.7f))
		Text(value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = color)
	}
}

@Composable
fun FieldGrid(seeds: List<Seed>, onSeedClick: (Seed) -> Unit) {
	LazyVerticalGrid(
		columns = GridCells.Fixed(3),
		contentPadding = PaddingValues(bottom = 16.dp),
		horizontalArrangement = Arrangement.spacedBy(8.dp),
		verticalArrangement = Arrangement.spacedBy(8.dp)
	) {
		items(seeds) { seed ->
			Box(modifier = Modifier.fillMaxWidth().clickable { onSeedClick(seed) }) {
				PixelCard(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
					Text(seed.emoji, fontSize = 24.sp)
					Text(
						seed.name,
						fontSize = 10.sp,
						color = FarmColors.getGold(),
						fontWeight = FontWeight.Bold
					)
					Text(
						"${if (seed.pct > 0) "+" else ""}${seed.pct}%",
						fontSize = 11.sp,
						color = if (seed.pct >= 0) FarmColors.getLightGreen() else FarmColors.getSoftRed()
					)
				}
			}
		}
		item {
			PixelCard(
				modifier = Modifier.fillMaxWidth().aspectRatio(1f),
				containerColor = FarmColors.getDarkGround(),
				borderColor = FarmColors.getGround()
			) {
				Text("+", fontSize = 20.sp, color = FarmColors.getGround())
				Text("씨앗 심기", fontSize = 10.sp, color = FarmColors.getGround())
			}
		}
	}
}

@Composable
fun DetailOverlay(seed: Seed, onDismiss: () -> Unit) {
	// 임시 데이터 (HTML 프로토타입 기반)
	val buyingPrice = 38000
	val quantity = 5
	val totalBuying = buyingPrice * quantity
	val currentPrice = (buyingPrice * (1 + seed.pct / 100)).toInt() // 단순 계산
	val totalCurrent = currentPrice * quantity
	val profit = totalCurrent - totalBuying
	val profitSign = if (profit > 0) "+" else ""

	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(FarmColors.getGrass())
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = {}
			) // 뒷 배경 클릭 방지
			.statusBarsPadding()
			.padding(16.dp)
	) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.verticalScroll(rememberScrollState()) // 안전장치로 스크롤은 유지
		) {
			// 1. 헤더: 뒤로가기 버튼 + 이모지 + 타이틀 (한 줄로 통합하여 공간 절약)
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier.fillMaxWidth()
			) {
				PixelButton("←", onClick = onDismiss, modifier = Modifier.width(40.dp))
				Spacer(modifier = Modifier.width(16.dp))
				Text(seed.emoji, fontSize = 28.sp)
				Spacer(modifier = Modifier.width(8.dp))
				Text(seed.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
			}

			Spacer(modifier = Modifier.height(20.dp))

			// 2. 상세 정보: 2열 그리드 배치로 변경 (높이 절약)
			PixelCard(containerColor = FarmColors.getDarkGrass()) {
				Row(modifier = Modifier.fillMaxWidth()) {
					// 왼쪽 열
					Column(modifier = Modifier.weight(1f)) {
						DetailRow("매수가", "${buyingPrice}원")
						DetailRow("매수금액", "${totalBuying}원")
						DetailRow("평가금액", "${totalCurrent}원")
					}
					Spacer(modifier = Modifier.width(16.dp))
					// 오른쪽 열
					Column(modifier = Modifier.weight(1f)) {
						DetailRow("보유수량", "${quantity}주")
						DetailRow("현재가", "${currentPrice}원")
						DetailRow("매도가", "미정", color = Color.Gray)
					}
				}

				Spacer(modifier = Modifier.height(8.dp))
				// 구분선
				Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(FarmColors.getGround().copy(alpha = 0.5f)))
				Spacer(modifier = Modifier.height(8.dp))

				// 손익 정보 (중요하니까 크게)
				DetailRow(
					"손익",
					"$profitSign${profit}원 (${if (seed.pct > 0) "+" else ""}${seed.pct}%)",
					color = if (profit >= 0) FarmColors.getGold() else FarmColors.getSoftRed()
				)
			}

			Spacer(modifier = Modifier.height(16.dp))

			// 3. 액션 버튼
			Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
				PixelButton(
					"💧물타기",
					modifier = Modifier.weight(1f),
					containerColor = FarmColors.getWater(),
					contentColor = Color(0xFFB8D4FF),
					onClick = {})
				PixelButton(
					"🔥불타기",
					modifier = Modifier.weight(1f),
					containerColor = FarmColors.getFire(),
					contentColor = Color(0xFFFFD4B8),
					onClick = {})
				PixelButton(
					"✂️익절",
					modifier = Modifier.weight(1f),
					containerColor = FarmColors.getHarvest(),
					contentColor = Color(0xFFE8FFD0),
					onClick = {})
				PixelButton(
					"💀손절",
					modifier = Modifier.weight(1f),
					containerColor = FarmColors.getCut(),
					contentColor = Color(0xFFE0B8FF),
					onClick = {})
			}

			// 4. 농부 말풍선
			Spacer(modifier = Modifier.height(12.dp))
			PixelCard(containerColor = FarmColors.getDarkGrass(), borderColor = FarmColors.getDarkGrass()) {
				Row(
					modifier = Modifier.fillMaxWidth(),
					verticalAlignment = Alignment.Top,
					horizontalArrangement = Arrangement.spacedBy(12.dp)
				) {
					// 농부 아이콘 박스
					Surface(
						shape = RoundedCornerShape(4.dp),
						color = FarmColors.getGround(),
						border = BorderStroke(2.dp, FarmColors.getField()),
						modifier = Modifier.size(36.dp)
					) {
						Box(contentAlignment = Alignment.Center) {
							Text("👨‍🌾", fontSize = 20.sp)
						}
					}

					// 말풍선 텍스트
					Column {
						Text(
							text = if (profit >= 0) "수익 중이에요! 👏\n목표가까지 조금 더 기다려볼까요?"
							else "어라..? 비가 오네요 ☔\n힘내세요 농부님! 해는 다시 뜰 거예요.",
							color = FarmColors.getLightGreen(),
							fontSize = 12.sp,
							lineHeight = 18.sp
						)
					}
				}
			}
		}
	}
}

@Composable
fun DetailRow(label: String, value: String, color: Color = Color.White) {
	Row(
		modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp), // 간격 축소 (4dp -> 2dp)
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Text(label, fontSize = 12.sp, color = FarmColors.getLightGreen())
		Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
	}
}

@Composable
fun WarehouseOverlay(onDismiss: () -> Unit) {
	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(FarmColors.getDarkGround())
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = {}
			) // 뒷 배경 클릭 방지
			.statusBarsPadding()
			.padding(16.dp)
	) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.verticalScroll(rememberScrollState())
		) {
			// 헤더
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier.fillMaxWidth()
			) {
				PixelButton("←", onClick = onDismiss, modifier = Modifier.width(40.dp))
				Spacer(modifier = Modifier.width(16.dp))
				Text("📦 나의 창고", color = FarmColors.getGold(), fontWeight = FontWeight.Bold, fontSize = 20.sp)
			}

			Spacer(modifier = Modifier.height(20.dp))

			// 요약 카드
			Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
				PixelCard(modifier = Modifier.weight(1f), containerColor = FarmColors.getNight()) {
					Text("총 실현손익", fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f))
					Text("+77,000원", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = FarmColors.getGold())
				}
				PixelCard(modifier = Modifier.weight(1f), containerColor = FarmColors.getNight()) {
					Text("수확 / 손절", fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f))
					Text("4회 / 1회", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = FarmColors.getGold())
				}
			}

			Spacer(modifier = Modifier.height(16.dp))

			// 월별 실현손익 차트
			PixelCard(modifier = Modifier.fillMaxWidth(), containerColor = FarmColors.getNight()) {
				Text("월별 실현손익", fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f), modifier = Modifier.align(Alignment.Start))
				Spacer(modifier = Modifier.height(8.dp))
				
				val chartData = listOf("1월" to 32000, "2월" to -18000, "3월" to 63000)
				chartData.forEach { (month, profit) ->
					Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
						Text(month, fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f), modifier = Modifier.width(40.dp))
						Box(
							modifier = Modifier
								.weight(1f)
								.height(14.dp)
								.background(Color.Black, RoundedCornerShape(4.dp))
						) {
							val widthPct = (kotlin.math.abs(profit).toFloat() / 70000f).coerceIn(0.1f, 1f)
							Box(
								modifier = Modifier
									.fillMaxHeight()
									.fillMaxWidth(widthPct)
									.background(if (profit >= 0) FarmColors.getGrass() else FarmColors.getSoftRed(), RoundedCornerShape(4.dp))
									.padding(start = 4.dp),
								contentAlignment = Alignment.CenterStart
							) {
								Text("${if (profit > 0) "+" else ""}$profit", fontSize = 9.sp, color = Color.White, fontWeight = FontWeight.Bold)
							}
						}
					}
				}
			}

			Spacer(modifier = Modifier.height(16.dp))

			// 기록 리스트
			Text("✂️ 수확 기록", fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f), fontWeight = FontWeight.Bold)
			Spacer(modifier = Modifier.height(8.dp))
			WarehouseItem("삼성전자", "2026.02.10 · 보유 30일", "+32,000원", "+8.4%", FarmColors.getLightGreen())
			WarehouseItem("한화에어로", "2026.03.20 · 보유 25일", "+45,000원", "+11.8%", FarmColors.getLightGreen())
			
			Spacer(modifier = Modifier.height(8.dp))
			Text("💀 손절 기록", fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f), fontWeight = FontWeight.Bold)
			Spacer(modifier = Modifier.height(8.dp))
			WarehouseItem("카카오", "2026.02.28 · 보유 15일", "-18,000원", "-6.2%", FarmColors.getSoftRed())
		}
	}
}

@Composable
fun WarehouseItem(name: String, date: String, profit: String, pct: String, color: Color) {
	PixelCard(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), containerColor = FarmColors.getNight()) {
		Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
			Column {
				Text(name, fontSize = 12.sp, color = FarmColors.getGold(), fontWeight = FontWeight.Bold)
				Text(date, fontSize = 10.sp, color = Color.White.copy(alpha = 0.5f))
			}
			Column(horizontalAlignment = Alignment.End) {
				Text(profit, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = FarmColors.getGold())
				Text(pct, fontSize = 10.sp, color = color)
			}
		}
	}
}

@Composable
fun DiaryOverlay(onDismiss: () -> Unit) {
	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(FarmColors.getNight())
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = {}
			) // 뒷 배경 클릭 방지
			.statusBarsPadding()
			.padding(16.dp)
	) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.verticalScroll(rememberScrollState())
		) {
			// 헤더
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier.fillMaxWidth()
			) {
				PixelButton("←", onClick = onDismiss, modifier = Modifier.width(40.dp), containerColor = Color(0xFF1A4A6E), contentColor = FarmColors.getMoon())
				Spacer(modifier = Modifier.width(16.dp))
				Text("📔 농부의 일기", color = FarmColors.getMoon(), fontWeight = FontWeight.Bold, fontSize = 20.sp)
			}

			Spacer(modifier = Modifier.height(20.dp))

			// 탭
			Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
				PixelButton("오늘일기", modifier = Modifier.weight(1f), containerColor = Color(0xFF1A4A6E), contentColor = Color.White, onClick = {})
				PixelButton("관심종목", modifier = Modifier.weight(1f), containerColor = Color(0xFF0A1E2D), contentColor = FarmColors.getMoon(), onClick = {})
				PixelButton("공부노트", modifier = Modifier.weight(1f), containerColor = Color(0xFF0A1E2D), contentColor = FarmColors.getMoon(), onClick = {})
			}

			Spacer(modifier = Modifier.height(12.dp))

			// 입력창
			PixelCard(modifier = Modifier.fillMaxWidth(), containerColor = Color(0xFF0A1E2D), borderColor = Color(0xFF1A3A4D)) {
				Box(modifier = Modifier.fillMaxWidth().height(80.dp).padding(4.dp)) {
					Text("오늘 투자하면서 느낀 점, 관심 종목 메모를 적어보세요...", color = Color.White.copy(alpha = 0.3f), fontSize = 12.sp)
				}
			}
			Spacer(modifier = Modifier.height(8.dp))
			PixelButton("저장하기", modifier = Modifier.fillMaxWidth(), containerColor = Color(0xFF1A4A6E), contentColor = Color.White, onClick = {})

			Spacer(modifier = Modifier.height(16.dp))

			// 이전 일기 리스트
			DiaryEntry("2026.03.13 ⛅", "한화에어로 방산 수출 뉴스 있었음. 좀 더 지켜보기로 함. 아직 확신은 없지만 업황이 좋은 것 같다.")
			DiaryEntry("2026.03.10 ☀️", "삼성전자 수확 완료! 처음으로 수익 냈다. 다음엔 더 오래 기다려볼 것.")
		}
	}
}

@Composable
fun DiaryEntry(date: String, text: String) {
	PixelCard(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), containerColor = Color(0xFF0A1E2D), borderColor = Color(0xFF1A3A4D)) {
		Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
			Text(date, fontSize = 10.sp, color = FarmColors.getMoon())
			Spacer(modifier = Modifier.height(6.dp))
			Text(text, fontSize = 12.sp, color = Color(0xFFC8E8FF), lineHeight = 18.sp)
		}
	}
}
