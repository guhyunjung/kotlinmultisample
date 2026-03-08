package com.example.kotlinmultisample.app.ui.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material3.Icon
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

    // 등장 애니메이션 (Scale + Alpha)
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )

    val scaleAnim = animateFloatAsState(
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

    // 진입 시 애니메이션 시작
    LaunchedEffect(Unit) {
        startAnimation = true
    }

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
            // 지구본 아이콘
            Icon(
                imageVector = Icons.Rounded.Public,
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .scale(scaleAnim.value * if (startAnimation) pulseScale else 1f) // 등장 후 펄싱
                    .alpha(alphaAnim.value),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 앱 타이틀
            Text(
                text = "Kotlin Multiplatform",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .alpha(alphaAnim.value)
                    .offset(y = if (startAnimation) 0.dp else 50.dp) // 아래에서 위로 올라옴
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "World Explorer",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .alpha(alphaAnim.value)
            )
        }
    }
}

