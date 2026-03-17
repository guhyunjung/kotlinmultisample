package com.example.kotlinmultisample.app.ui.screen.farm.components

import com.example.kotlinmultisample.app.ui.screen.farm.model.SummaryData

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinmultisample.app.ui.screen.farm.util.formatDecimal

/**
 * 상단 요약 바
 * 총 투자금, 평가금액, 총 손익 등을 한눈에 보여주는 컴포넌트
 */
@Composable
fun SummaryBarNew(data: SummaryData = SummaryData()) {
	val profitSign = if (data.totalProfit > 0) "+" else ""
	val profitColor = if (data.totalProfit >= 0) FarmColors.getGold() else FarmColors.getSoftRed()

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
			SummaryItemNew("총 투자금", "${formatDecimal(data.totalInvest)}원")
			SummaryItemNew("평가금액", "${formatDecimal(data.totalCurrent)}원")
			SummaryItemNew("총 손익", "$profitSign${formatDecimal(data.totalProfit)}", color = profitColor)
		}
	}
}

@Composable
fun SummaryItemNew(label: String, value: String, color: Color = Color.White) {
	Column(horizontalAlignment = Alignment.CenterHorizontally) {
		Text(label, fontSize = 10.sp, color = Color.White.copy(alpha = 0.7f))
		Text(value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = color)
	}
}
