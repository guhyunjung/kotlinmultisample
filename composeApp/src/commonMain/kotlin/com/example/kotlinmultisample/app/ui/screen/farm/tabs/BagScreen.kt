package com.example.kotlinmultisample.app.ui.screen.farm.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.kotlinmultisample.app.ui.screen.farm.components.FarmColors

@Composable
fun BagScreen(onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FarmColors.getGround())
            .clickable(onClick = onDismiss), // 클릭 시 메인으로 돌아가도록 설정 (임시)
        contentAlignment = Alignment.Center
    ) {
        Text("가방 화면 (준비중)", fontSize = 24.sp, color = Color.White)
    }
}
