package com.example.kotlinmultisample.app.presentation.calculator

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * 주식 및 코인 물타기 계산기 화면 프리뷰 함수입니다.
 * Android 스튜디오 등에서 미리보기를 제공합니다.
 */
@Composable
@Preview
fun AverageDownCalculatorScreenPreview() {
    MaterialTheme {
        AverageDownCalculatorScreen()
    }
}

/**
 * 주식/코인 물타기(평단가) 계산기 화면 컴포저블입니다.
 *
 * 사용자가 현재 보유 중인 자산의 평단가와 수량,
 * 추가 매수할 자산의 가격과 수량을 입력하면
 * 최종 평단가와 총 매수 금액을 실시간으로 계산하여 보여줍니다.
 */
@Composable
fun AverageDownCalculatorScreen() {
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

	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp)
			.verticalScroll(rememberScrollState()),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(
			text = "물타기 계산기",
			style = MaterialTheme.typography.headlineMedium,
			fontWeight = FontWeight.Bold,
			modifier = Modifier.padding(bottom = 24.dp)
		)

		// 1. 보유 주식 (Current Holding)
		InputSection(
			title = "현재 보유 내역",
			priceValue = holdPrice,
			onPriceChange = { holdPrice = it },
			qtyValue = holdQuantity,
			onQtyChange = { holdQuantity = it }
		)

		Spacer(modifier = Modifier.height(24.dp))

		// 2. 추가 매수 (Additional Buy)
		InputSection(
			title = "추가 매수 예정",
			priceValue = addPrice,
			onPriceChange = { addPrice = it },
			qtyValue = addQuantity,
			onQtyChange = { addQuantity = it }
		)

		Spacer(modifier = Modifier.height(32.dp))

		// 3. 결과 (Result)
		ResultCard(
			averagePrice = averagePrice,
			totalQuantity = totalQuantity,
			totalAmount = totalAmount
		)
	}
}

@Composable
fun InputSection(
	title: String,
	priceValue: String,
	onPriceChange: (String) -> Unit,
	qtyValue: String,
	onQtyChange: (String) -> Unit
) {
	Card(
		elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
		modifier = Modifier.fillMaxWidth()
	) {
		Column(modifier = Modifier.padding(16.dp)) {
			Text(
				text = title,
				style = MaterialTheme.typography.titleMedium,
				fontWeight = FontWeight.Bold,
				modifier = Modifier.padding(bottom = 12.dp)
			)

			Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
				OutlinedTextField(
					value = priceValue,
					onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) onPriceChange(it) },
					label = { Text("단가 (Price)") },
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
					modifier = Modifier.weight(1f),
					singleLine = true
				)

				OutlinedTextField(
					value = qtyValue,
					onValueChange = { if (it.all { char -> char.isDigit() }) onQtyChange(it) },
					label = { Text("수량 (Qty)") },
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
					modifier = Modifier.weight(1f),
					singleLine = true
				)
			}
		}
	}
}

@Composable
fun ResultCard(
	averagePrice: Double,
	totalQuantity: Long,
	totalAmount: Double
) {
	Card(
		colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
		modifier = Modifier.fillMaxWidth()
	) {
		Column(
			modifier = Modifier.padding(20.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Text(
				text = "최종 평단가 (New Average)",
				style = MaterialTheme.typography.labelLarge
			)
			Text(
				text = formatDecimal(averagePrice),
				style = MaterialTheme.typography.displaySmall,
				fontWeight = FontWeight.Bold,
				color = MaterialTheme.colorScheme.onPrimaryContainer,
				modifier = Modifier.padding(vertical = 8.dp)
			)

			HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				Column(horizontalAlignment = Alignment.Start) {
					Text("총 수량", style = MaterialTheme.typography.bodyMedium)
					Text(
						formatInteger(totalQuantity),
						style = MaterialTheme.typography.titleMedium,
						fontWeight = FontWeight.SemiBold
					)
				}

				Column(horizontalAlignment = Alignment.End) {
					Text("총 매수 금액", style = MaterialTheme.typography.bodyMedium)
					Text(
						formatDecimal(totalAmount),
						style = MaterialTheme.typography.titleMedium,
						fontWeight = FontWeight.SemiBold
					)
				}
			}
		}
	}
}

// 간단한 파싱 확장 함수
fun String.toBiggerDecimal(): Double? {
	return this.toDoubleOrNull()
}

// KMP 호환용 간단 포맷터 (Simple formatter for KMP compatibility)
fun formatDecimal(value: Double): String {
	if (value.isNaN()) return "0.00"
	val integerPart = value.toLong()
	val fraction = value - integerPart
	val fractionPart = (fraction * 100).toInt() // 소수점 2자리 간소화

	// 0.05 -> 5, 0.5 -> 50
	// 정확한 반올림 로직 등은 복잡하므로 단순 절삭/표시용
	val fractionStr = if (fractionPart < 10) "0$fractionPart" else fractionPart.toString()

	return "${formatInteger(integerPart)}.$fractionStr"
}

fun formatInteger(value: Long): String {
	return value.toString().reversed().chunked(3).joinToString(",").reversed()
}
