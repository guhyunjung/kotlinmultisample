package com.example.kotlinmultisample.app.ui.screen.farm

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 종목 상세 정보 오버레이
 * 특정 종목을 선택했을 때 나타나는 상세 화면입니다.
 * 매수가, 평가금액, 손익 등의 상세 정보와 매매 버튼(물타기, 불타기 등)을 포함합니다.
 */
@Composable
fun DetailOverlayNew(seed: FarmSeed, onDismiss: () -> Unit) {
	// 임시 데이터 (HTML 프로토타입 기반의 더미 계산)
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
			.background(FarmColors.getGrass()) // 상세 화면 배경은 잔디색
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = {}
			) // 뒷 배경 클릭 방지 (이벤트 소비)
			.statusBarsPadding()
			.padding(16.dp)
	) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.verticalScroll(rememberScrollState()) // 화면 높이가 작을 경우를 대비해 스크롤 가능하게 설정
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

			// 2. 상세 정보: 2열 그리드 배치로 변경 (화면 높이 절약을 위한 레이아웃 최적화)
			PixelCard(containerColor = FarmColors.getDarkGrass()) {
				Row(modifier = Modifier.fillMaxWidth()) {
					// 왼쪽 열
					Column(modifier = Modifier.weight(1f)) {
						DetailRowNew("매수가", "${buyingPrice}원")
						DetailRowNew("매수금액", "${totalBuying}원")
						DetailRowNew("평가금액", "${totalCurrent}원")
					}
					Spacer(modifier = Modifier.width(16.dp))
					// 오른쪽 열
					Column(modifier = Modifier.weight(1f)) {
						DetailRowNew("보유수량", "${quantity}주")
						DetailRowNew("현재가", "${currentPrice}원")
						DetailRowNew("매도가", "미정", color = Color.Gray)
					}
				}

				Spacer(modifier = Modifier.height(8.dp))
				// 구분선
				Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(FarmColors.getGround().copy(alpha = 0.5f)))
				Spacer(modifier = Modifier.height(8.dp))

				// 손익 정보 (가장 중요한 정보이므로 하단에 강조)
				DetailRowNew(
					"손익",
					"$profitSign${profit}원 (${if (seed.pct > 0) "+" else ""}${seed.pct}%)",
					color = if (profit >= 0) FarmColors.getGold() else FarmColors.getSoftRed()
				)
			}

			Spacer(modifier = Modifier.height(16.dp))

			// 3. 액션 버튼 (매수/매도 관련 재미있는 네이밍 사용)
			Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
				PixelButton(
					"💧물타기", // 추가 매수 (가격 하락 시)
					modifier = Modifier.weight(1f),
					containerColor = FarmColors.getWater(),
					contentColor = Color(0xFFB8D4FF),
					onClick = {})
				PixelButton(
					"🔥불타기", // 추가 매수 (가격 상승 시)
					modifier = Modifier.weight(1f),
					containerColor = FarmColors.getFire(),
					contentColor = Color(0xFFFFD4B8),
					onClick = {})
				PixelButton(
					"✂️익절", // 이익 실현 매도
					modifier = Modifier.weight(1f),
					containerColor = FarmColors.getHarvest(),
					contentColor = Color(0xFFE8FFD0),
					onClick = {})
				PixelButton(
					"💀손절", // 손실 확정 매도
					modifier = Modifier.weight(1f),
					containerColor = FarmColors.getCut(),
					contentColor = Color(0xFFE0B8FF),
					onClick = {})
			}

			// 4. 농부 말풍선: 현재 수익 상태에 따른 코멘트 표시
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

					// 말풍선 텍스트 (수익 여부에 따라 다른 메시지 출력)
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
fun DetailRowNew(label: String, value: String, color: Color = Color.White) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 4.dp),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically
	) {
		Text(label, fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
		Text(value, fontSize = 12.sp, color = color, fontWeight = FontWeight.Bold)
	}
}
