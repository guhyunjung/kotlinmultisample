package com.example.kotlinmultisample.app.ui.screen.farm

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalculateScreen(onDismiss: () -> Unit) {
	// 현재 보유 중인 단가 및 수량 (상태 보존)
	var holdPrice by rememberSaveable { mutableStateOf("") }
	var holdQuantity by rememberSaveable { mutableStateOf("") }

	// 추가 매수할 단가 및 수량 (상태 보존)
	var addPrice by rememberSaveable { mutableStateOf("") }
	var addQuantity by rememberSaveable { mutableStateOf("") }

	// 총 보유 수량 계산 (현재 보유 + 추가 매수)
	val totalQuantity = (holdQuantity.toLongOrNull() ?: 0) + (addQuantity.toLongOrNull() ?: 0)

	// 현재 총 매수 금액
	val currentAmount = (holdPrice.toBiggerDecimal() ?: 0.0) * (holdQuantity.toLongOrNull() ?: 0)
	// 추가 매수 금액
	val addAmount = (addPrice.toBiggerDecimal() ?: 0.0) * (addQuantity.toLongOrNull() ?: 0)
	// 최종 총 매수 금액
	val totalAmount = currentAmount + addAmount

	// 최종 평단가 계산 (총 금액 / 총 수량)
	val averagePrice = if (totalQuantity > 0) totalAmount / totalQuantity else 0.0

	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(FarmColors.getNight()) // 밤하늘 배경색
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
				Text("🧮 물타기/불타기", color = FarmColors.getGold(), fontWeight = FontWeight.Bold, fontSize = 20.sp)
			}

			Spacer(modifier = Modifier.height(24.dp))

			// 1. 보유 주식 (Current Holding)
			Text("현재 보유 농산물", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
			Spacer(modifier = Modifier.height(8.dp))
			PixelCard(containerColor = Color(0xFF0A1E2D), borderColor = Color(0xFF1A3A4D)) {
				Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
					// Material TextField 사용 (스타일 커스텀)
					CalculatorTextField(
						value = holdPrice,
						onValueChange = { holdPrice = it },
						label = "평단가(₩)",
						modifier = Modifier.weight(1f)
					)
					CalculatorTextField(
						value = holdQuantity,
						onValueChange = { holdQuantity = it },
						label = "수량(주)",
						modifier = Modifier.weight(1f)
					)
				}
			}

			Spacer(modifier = Modifier.height(24.dp))

			// 2. 추가 매수 (Additional Buy)
			Text("추가 매수(물타기/불타기)", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
			Spacer(modifier = Modifier.height(8.dp))
			PixelCard(containerColor = Color(0xFF0A1E2D), borderColor = Color(0xFF1A3A4D)) {
				Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
					CalculatorTextField(
						value = addPrice,
						onValueChange = { addPrice = it },
						label = "매수단가(₩)",
						modifier = Modifier.weight(1f)
					)
					CalculatorTextField(
						value = addQuantity,
						onValueChange = { addQuantity = it },
						label = "매수수량(주)",
						modifier = Modifier.weight(1f)
					)
				}
			}

			Spacer(modifier = Modifier.height(32.dp))

			// 3. 결과 (Result)
			PixelCard(containerColor = FarmColors.getGrass(), borderColor = FarmColors.getDarkGrass()) {
				Column(
					modifier = Modifier.fillMaxWidth().padding(8.dp),
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					Text(
						text = "예상 최종 평단가",
						style = MaterialTheme.typography.labelLarge,
						color = Color.White.copy(alpha = 0.8f)
					)
					Spacer(modifier = Modifier.height(4.dp))
					Text(
						text = "${formatDecimal(averagePrice)}원",
						style = MaterialTheme.typography.displaySmall,
						fontWeight = FontWeight.Bold,
						color = Color.White
					)

					Spacer(modifier = Modifier.height(16.dp))
					Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.3f)))
					Spacer(modifier = Modifier.height(16.dp))

					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.SpaceBetween
					) {
						Column(horizontalAlignment = Alignment.Start) {
							Text("총 수량", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f))
							Text(
								"${formatInteger(totalQuantity)}주",
								style = MaterialTheme.typography.titleMedium,
								fontWeight = FontWeight.SemiBold,
								color = FarmColors.getGold()
							)
						}

						Column(horizontalAlignment = Alignment.End) {
							Text("총 매수 금액", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f))
							Text(
								"${formatDecimal(totalAmount)}원",
								style = MaterialTheme.typography.titleMedium,
								fontWeight = FontWeight.SemiBold,
								color = FarmColors.getGold()
							)
						}
					}
				}
			}
		}
	}
}

@Composable
fun CalculatorTextField(
	value: String,
	onValueChange: (String) -> Unit,
	label: String,
	modifier: Modifier = Modifier
) {
	OutlinedTextField(
		value = value,
		onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) onValueChange(it) },
		label = { Text(label, color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp) },
		modifier = modifier,
		singleLine = true,
		colors = OutlinedTextFieldDefaults.colors(
			focusedBorderColor = FarmColors.getGold(),
			unfocusedBorderColor = Color(0xFF1A3A4D),
			focusedTextColor = Color.White,
			unfocusedTextColor = Color.White,
			cursorColor = FarmColors.getGold(),
			focusedLabelColor = FarmColors.getGold(),
			unfocusedLabelColor = Color.White.copy(alpha = 0.5f)
		)
	)
}


