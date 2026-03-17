package com.example.kotlinmultisample.app.ui.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinmultisample.composeapp.generated.resources.Res
import kotlinmultisample.composeapp.generated.resources.gemini_4
import kotlinmultisample.composeapp.generated.resources.gemini_5
import kotlinmultisample.composeapp.generated.resources.gemini_6
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource

/**
 * 앱 초기 실행 시 보여줄 스플래시 화면
 *
 * 앱 "준비" 로직(데이터 로딩 등)이 완료될 때까지 표시됩니다.
 * Compose Animation을 사용하여 부드러운 등장 효과를 줍니다.
 */
@Composable
fun SplashScreen() {
	// 애니메이션 상태
	var startAnimation by remember { mutableStateOf(false) }

	// Gemini 캐릭터 애니메이션 프레임 인덱스 (0, 1, 2 반복)
	var frameIndex by remember { mutableStateOf(0) }

	// 프레임 애니메이션 반복 재생 루프
	LaunchedEffect(Unit) {
		startAnimation = true // 등장 애니메이션 시작
		while (true) {
			delay(200) // 0.5초마다 이미지 변경 (4 -> 5 -> 6 -> 4 ...)
			frameIndex = (frameIndex + 1) % 3
		}
	}

	// 등장 애니메이션 (Scale + Alpha)
	val alphaAnim by animateFloatAsState(
		targetValue = if (startAnimation) 1f else 0f,
		animationSpec = tween(durationMillis = 1000)
	)

	val scaleAnim by animateFloatAsState(
		targetValue = if (startAnimation) 1f else 0.5f,
		animationSpec = spring(
			dampingRatio = Spring.DampingRatioMediumBouncy,
			stiffness = Spring.StiffnessLow
		)
	)

	// 배경 그라데이션 애니메이션 (선택 사항: 펄싱 효과)
	val infiniteTransition = rememberInfiniteTransition()
	val pulseScale by infiniteTransition.animateFloat(
		initialValue = 1f,
		targetValue = 1.05f,
		animationSpec = infiniteRepeatable(
			animation = tween(1000, easing = LinearEasing),
			repeatMode = RepeatMode.Reverse
		)
	)

	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(
				Brush.verticalGradient(
					colors = listOf(
						MaterialTheme.colorScheme.primaryContainer,
						MaterialTheme.colorScheme.background
					)
				)
			),
		contentAlignment = Alignment.Center
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		) {
			// Gemini 캐릭터 애니메이션
			val currentImage = when(frameIndex) {
				0 -> Res.drawable.gemini_4
				1 -> Res.drawable.gemini_5
				else -> Res.drawable.gemini_6
			}

			Image(
				painter = painterResource(currentImage),
				contentDescription = "App Logo Animation",
				modifier = Modifier
					.size(200.dp)
					.scale(scaleAnim) // 등장 시 뽕 하고 나타나는 효과
					.alpha(alphaAnim)
			)

			Spacer(modifier = Modifier.height(24.dp))

			// 앱 타이틀
			Text(
				text = "주린이의 주식농장",
				style = MaterialTheme.typography.headlineMedium,
				fontWeight = FontWeight.Bold,
				color = MaterialTheme.colorScheme.onBackground,
				modifier = Modifier
					.alpha(alphaAnim)
					.offset(y = if (startAnimation) 0.dp else 50.dp) // 아래에서 위로 올라옴
			)

			Spacer(modifier = Modifier.height(8.dp))

			Text(
				text = "UFO 🛸",
				style = MaterialTheme.typography.bodyLarge,
				color = MaterialTheme.colorScheme.secondary,
				modifier = Modifier
					.alpha(alphaAnim)
			)
		}
	}
}
