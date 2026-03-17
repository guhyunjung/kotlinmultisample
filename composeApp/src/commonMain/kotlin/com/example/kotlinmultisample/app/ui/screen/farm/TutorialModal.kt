package com.example.kotlinmultisample.app.ui.screen.farm

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
import kotlinx.coroutines.delay

/**
 * 튜토리얼 오버레이 컴포저블
 * 초보 사용자를 위한 가이드 내용을 단계별로 보여줍니다.
 * 타이핑 효과와 단계 이동 기능을 포함합니다.
 */
@Composable
fun TutorialModal(onDismiss: () -> Unit) {
	// 튜토리얼 단계별 텍스트 리스트
	val tutSteps = listOf(
		"안녕하세요! 저는 농장지기 씨앗이에요. 여러분의 첫 투자 여정을 함께할게요!",
		"주식이란 회사의 아주 작은 조각이에요. 삼성전자 1주를 사면 삼성의 주인이 되는 거예요!",
		"한 달 커피값으로 주식 1주를 살 수 있어요. 커피는 마시면 없어지지만 주식은 남아요!",
		"단, 모든 투자의 책임은 농부인 당신에게 있어요. 자, 이제 씨앗을 사러 가볼까요?"
	)
	var stepIdx by remember { mutableStateOf(0) }
	val currentText = tutSteps[stepIdx]

	// 텍스트 타이핑 효과 애니메이션 로직
	var animatedText by remember { mutableStateOf("") }
	LaunchedEffect(stepIdx) {
		animatedText = ""
		currentText.forEach { char ->
			animatedText += char
			delay(50) // 글자당 50ms 딜레이
		}
	}

	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(Color.Black.copy(alpha = 0.7f)) // 배경 어둡게 처리
			.statusBarsPadding()
			.padding(24.dp),
		contentAlignment = Alignment.Center
	) {
		Column(horizontalAlignment = Alignment.CenterHorizontally) {
			// 농부 아이콘을 카트 밖으로 분리하여 크게 표시하여 주목도 향상
			Text("👨‍🌾", fontSize = 64.sp)
			Spacer(modifier = Modifier.height(16.dp))

			// 대화창 스타일의 카드
			PixelCard(containerColor = FarmColors.getNight(), borderColor = FarmColors.getMoon()) {
				// 상단: 이름과 진행 단계 표시 (예: 1/4)
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				) {
					Text("농장지기 씨앗", color = FarmColors.getMoon(), fontSize = 14.sp, fontWeight = FontWeight.Bold)
					Text("${stepIdx + 1} / ${tutSteps.size}", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
				}

				Spacer(modifier = Modifier.height(12.dp))
				// 타이핑되는 텍스트 표시 영역
				Text(
					text = animatedText,
					color = Color.White,
					fontSize = 14.sp,
					lineHeight = 20.sp,
					modifier = Modifier.fillMaxWidth().heightIn(min = 60.dp) // 최소 높이 고정으로 깜빡임 방지
				)
				Spacer(modifier = Modifier.height(16.dp))

				// 하단 버튼 영역 (다음 / 건너뛰기)
				Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
					PixelButton(
						text = if (stepIdx < tutSteps.size - 1) "다음 →" else "농장 시작하기 🌾",
						containerColor = FarmColors.getGrass(),
						onClick = {
							if (stepIdx < tutSteps.size - 1) stepIdx++ else onDismiss()
						},
						modifier = Modifier.weight(1f)
					)
					// 마지막 단계가 아닐 때만 Skip 버튼 표시
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


