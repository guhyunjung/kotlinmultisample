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
 * 창고 오버레이 (매매 기록 확인)
 * 실현 손익과 월별 차트, 과거 매매 이력을 보여주는 화면입니다.
 */
@Composable
fun WarehouseOverlayNew(onDismiss: () -> Unit) {
	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(FarmColors.getDarkGround()) // 어두운 땅 색상 배경
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = {}
			)
			.statusBarsPadding()
			.padding(16.dp)
	) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.verticalScroll(rememberScrollState())
		) {
			// 헤더 영역
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier.fillMaxWidth()
			) {
				PixelButton("←", onClick = onDismiss, modifier = Modifier.width(40.dp))
				Spacer(modifier = Modifier.width(16.dp))
				Text("📦 나의 창고", color = FarmColors.getGold(), fontWeight = FontWeight.Bold, fontSize = 20.sp)
			}

			Spacer(modifier = Modifier.height(20.dp))

			// 상단 요약 카드 (총 실현손익, 승률 등)
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

			// 월별 실현손익 차트 (바 차트 형태)
			PixelCard(modifier = Modifier.fillMaxWidth(), containerColor = FarmColors.getNight()) {
				Text(
					"월별 실현손익",
					fontSize = 11.sp,
					color = Color.White.copy(alpha = 0.6f),
					modifier = Modifier.align(Alignment.Start)
				)
				Spacer(modifier = Modifier.height(8.dp))

				// 더미 차트 데이터
				val chartData = listOf("1월" to 32000, "2월" to -18000, "3월" to 63000)
				chartData.forEach { (month, profit) ->
					Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
						Text(month, fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f), modifier = Modifier.width(40.dp))
						// 바 차트 막대 (손익 규모에 따라 너비, 색상 조절)
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
									.background(
										if (profit >= 0) FarmColors.getGrass() else FarmColors.getSoftRed(),
										RoundedCornerShape(4.dp)
									)
									.padding(start = 4.dp),
								contentAlignment = Alignment.CenterStart
							) {
								Text(
									"${if (profit > 0) "+" else ""}$profit",
									fontSize = 9.sp,
									color = Color.White,
									fontWeight = FontWeight.Bold
								)
							}
						}
					}
				}
			}

			Spacer(modifier = Modifier.height(16.dp))

			// 과거 거래 기록 리스트
			Text("✂️ 수확 기록", fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f), fontWeight = FontWeight.Bold)
			Spacer(modifier = Modifier.height(8.dp))
			WarehouseItemNew("삼성전자", "2026.02.10 · 보유 30일", "+32,000원", "+8.4%", FarmColors.getLightGreen())
			WarehouseItemNew("한화에어로", "2026.03.20 · 보유 25일", "+45,000원", "+11.8%", FarmColors.getLightGreen())

			Spacer(modifier = Modifier.height(8.dp))
			Text("💀 손절 기록", fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f), fontWeight = FontWeight.Bold)
			Spacer(modifier = Modifier.height(8.dp))
			WarehouseItemNew("카카오", "2026.02.28 · 보유 15일", "-18,000원", "-6.2%", FarmColors.getSoftRed())
		}
	}
}

@Composable
fun WarehouseItemNew(name: String, date: String, profit: String, pct: String, color: Color) {
	PixelCard(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), containerColor = FarmColors.getNight()) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
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
