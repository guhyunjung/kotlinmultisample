package com.example.kotlinmultisample.app.ui.screen.farm.dialogs

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinmultisample.app.ui.screen.farm.components.FarmColors
import com.example.kotlinmultisample.app.ui.screen.farm.components.PixelButton
import com.example.kotlinmultisample.app.ui.screen.farm.components.PixelCard
import com.example.kotlinmultisample.app.ui.screen.farm.model.FarmSeed
import kotlin.math.abs

/**
 * 종목 상세 정보 오버레이
 * 특정 종목을 선택했을 때 나타나는 상세 화면입니다.
 * 매수가, 평가금액, 손익 등의 상세 정보와 매매 버튼(물타기, 불타기 등)을 포함합니다.
 */
@Composable
fun DetailOverlay(seed: FarmSeed, onDismiss: () -> Unit) {
	// FarmSeed 모델에서 데이터 가져오기
	val buyingPrice = formatNumber(seed.buyingPrice.toLong())
	val quantity = formatNumber(seed.quantity.toLong())
	val totalBuying = formatNumber(seed.totalBuyingValue.toLong())
	val currentPrice = formatNumber(seed.currentPrice.toLong())
	val totalCurrent = formatNumber(seed.totalCurrentValue.toLong())
	val profitVal = seed.profit.toLong()
	val profit = formatNumber(profitVal)
	val profitSign = if (profitVal > 0) "+" else ""

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

			// 2. 상세 정보: 1열 리스트 배치로 변경 (가독성 향상을 위해 큼직하게 표시)
			PixelCard(containerColor = FarmColors.getDarkGrass()) {
				Column(modifier = Modifier.fillMaxWidth()) {
					DetailRowNew("매수가", "${buyingPrice}원")
					DetailRowNew("보유수량", "${quantity}주")
					DetailRowNew("현재가", "${currentPrice}원")
					
					Spacer(modifier = Modifier.height(8.dp))
					Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(FarmColors.getGround().copy(alpha = 0.5f)))
					Spacer(modifier = Modifier.height(8.dp))

					DetailRowNew("매수금액", "${totalBuying}원")
					DetailRowNew("평가금액", "${totalCurrent}원")
					DetailRowNew("매도가", "미정", color = Color.Gray)
				}

				Spacer(modifier = Modifier.height(8.dp))
				// 구분선
				Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(FarmColors.getGround().copy(alpha = 0.5f)))
				Spacer(modifier = Modifier.height(8.dp))

				// 손익 정보 (가장 중요한 정보이므로 하단에 강조)
				DetailRowNew(
					"손익",
					"$profitSign${profit}원 (${if (seed.pct > 0) "+" else ""}${seed.pct}%)",
					color = if (profitVal >= 0) FarmColors.getGold() else FarmColors.getSoftRed(),
					isBold = true
				)
			}

			Spacer(modifier = Modifier.height(16.dp))

			// 3. 액션 버튼 (매수/매도 관련 재미있는 네이밍 사용, 2줄 배치)
			Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
				// 첫 번째 줄: 매수 관련 (물타기, 불타기)
				Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
				}
				
				// 두 번째 줄: 매도 관련 (익절, 손절)
				Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
							text = if (profitVal >= 0) "수익 중이에요! 👏\n목표가까지 조금 더 기다려볼까요?"
							else "어라..? 비가 오네요 ☔\n힘내세요 농부님! 해는 다시 뜰 거예요.",
							color = FarmColors.getLightGreen(),
							fontSize = 14.sp,
							lineHeight = 18.sp
						)
					}
				}
			}
		}
	}
}

@Composable
fun DetailRowNew(label: String, value: String, color: Color = Color.White, isBold: Boolean = false) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 8.dp), // 간격 넓힘
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically
	) {
		Text(label, fontSize = 16.sp, color = Color.White.copy(alpha = 0.7f)) // 폰트 키움
		Text(
			value, 
			fontSize = if (isBold) 20.sp else 18.sp, // 값 폰트 더 키움
			color = color, 
			fontWeight = FontWeight.Bold
		)
	}
}

// 천단위 콤마 포맷터 (KMP 호환)
fun formatNumber(amount: Long): String {
	val str = abs(amount).toString()
	val formatted = str.reversed().chunked(3).joinToString(",").reversed()
	return if (amount < 0) "-$formatted" else formatted
}
