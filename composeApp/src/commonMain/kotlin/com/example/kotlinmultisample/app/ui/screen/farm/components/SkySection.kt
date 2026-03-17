package com.example.kotlinmultisample.app.ui.screen.farm.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 상단 하늘 영역 컴포저블
 *
 * - 움직이는 구름 애니메이션 (Pixel Art 스타일)
 * - 사용자 레벨 및 앱 타이틀 표시
 * - 좌우로 왕복하는 구름 효과를 구현합니다.
 *
 * @param onMenuClick 우측 상단 메뉴 아이콘(햄버거) 클릭 시 동작하는 콜백
 */
@Composable
fun SkySection(onMenuClick: () -> Unit = {}) {
    // 구름 좌우 반복 이동 애니메이션 (2초 간격으로 20dp 범위 왕복)
    // val infiniteTransition = rememberInfiniteTransition()
    // val cloudOffset by infiniteTransition.animateFloat(
    //     initialValue = 0f,
    //     targetValue = 20f,
    //     animationSpec = infiniteRepeatable(
    //         animation = tween(2000, easing = LinearOutSlowInEasing), // 2초간 이동 (천천히 출발 후 빠르게)
    //         repeatMode = RepeatMode.Reverse // 왕복 반복
    //     )
    // )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // 타이틀 + 레벨 (중앙 정렬)
        Row(
            modifier = Modifier.align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "🌾 주린이의 주식농장",
                color = FarmColors.getTextOnSky(),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.width(8.dp))

            // 레벨 표시 (타이틀 옆으로 배치하여 시인성 확보)
            Surface(
                shape = PixelShape,
                color = FarmColors.getCloud().copy(alpha = 0.6f)
            ) {
                Text(
                    "Lv.3 🌱",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontSize = 11.sp,
                    color = FarmColors.getTextOnSky()
                )
            }
        }

        // 좌측 요소: 둥둥 떠다니는 구름 아이콘 (애니메이션 적용)
        // Surface(
        //     shape = PixelShape,
        //     color = FarmColors.getCloud(),
        //     modifier = Modifier
        //         .align(Alignment.CenterStart)
        //         .offset(y = cloudOffset.dp) // Y축으로 움직여 둥둥 떠다니는 느낌 연출
        // ) {
        //     Text(
        //         "☁️",
        //         modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        //         fontSize = 12.sp
        //     )
        // }

        // 우측 요소: 햄버거 메뉴 버튼 (누르면 설정 메뉴 등으로 이동)
        Surface(
            shape = PixelShape,
            color = FarmColors.getCloud(),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable { onMenuClick() }
        ) {
            Text(
                "☰",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                fontSize = 12.sp
            )
        }
    }
}
