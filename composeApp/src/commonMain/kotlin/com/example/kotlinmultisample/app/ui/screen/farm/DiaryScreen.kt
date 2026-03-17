package com.example.kotlinmultisample.app.ui.screen.farm

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 농부의 일기 (메모장) 오버레이
 * 투자 아이디어, 매매 원칙 등을 기록하는 공간입니다.
 * 어두운 밤하늘 색상 테마를 사용합니다.
 */
@Composable
fun DiaryScreen(onDismiss: () -> Unit) {
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
			// 헤더
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier.fillMaxWidth()
			) {
				PixelButton(
					"←",
					onClick = onDismiss,
					modifier = Modifier.width(40.dp),
					containerColor = Color(0xFF1A4A6E),
					contentColor = FarmColors.getMoon()
				)
				Spacer(modifier = Modifier.width(16.dp))
				Text("📔 농부의 일기", color = FarmColors.getMoon(), fontWeight = FontWeight.Bold, fontSize = 20.sp)
			}

			Spacer(modifier = Modifier.height(20.dp))

			// 일기 카테고리 탭
			Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
				PixelButton(
					"오늘일기",
					modifier = Modifier.weight(1f),
					containerColor = Color(0xFF1A4A6E),
					contentColor = Color.White,
					onClick = {})
				PixelButton(
					"관심종목",
					modifier = Modifier.weight(1f),
					containerColor = Color(0xFF0A1E2D),
					contentColor = FarmColors.getMoon(),
					onClick = {})
				PixelButton(
					"공부노트",
					modifier = Modifier.weight(1f),
					containerColor = Color(0xFF0A1E2D),
					contentColor = FarmColors.getMoon(),
					onClick = {})
			}

			Spacer(modifier = Modifier.height(12.dp))

			// 새 일기 작성 영역
			PixelCard(
				modifier = Modifier.fillMaxWidth(),
				containerColor = Color(0xFF0A1E2D),
				borderColor = Color(0xFF1A3A4D)
			) {
				Box(modifier = Modifier.fillMaxWidth().height(80.dp).padding(4.dp)) {
					Text("오늘 투자하면서 느낀 점, 관심 종목 메모를 적어보세요...", color = Color.White.copy(alpha = 0.3f), fontSize = 12.sp)
				}
			}
			Spacer(modifier = Modifier.height(8.dp))
			PixelButton(
				"저장하기",
				modifier = Modifier.fillMaxWidth(),
				containerColor = Color(0xFF1A4A6E),
				contentColor = Color.White,
				onClick = {})

			Spacer(modifier = Modifier.height(16.dp))

			// 이전 일기 리스트 표시
			DiaryEntry("2026.03.13 ⛅", "한화에어로 방산 수출 뉴스 있었음. 좀 더 지켜보기로 함. 아직 확신은 없지만 업황이 좋은 것 같다.")
			DiaryEntry("2026.03.10 ☀️", "삼성전자 수확 완료! 처음으로 수익 냈다. 다음엔 더 오래 기다려볼 것.")
		}
	}
}

@Composable
fun DiaryEntry(date: String, text: String) {
	PixelCard(
		modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
		containerColor = Color(0xFF0A1E2D),
		borderColor = Color(0xFF1A3A4D)
	) {
		Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
			Text(date, fontSize = 10.sp, color = FarmColors.getMoon())
			Spacer(modifier = Modifier.height(6.dp))
			Text(text, fontSize = 12.sp, color = Color(0xFFC8E8FF), lineHeight = 18.sp)
		}
	}
}
