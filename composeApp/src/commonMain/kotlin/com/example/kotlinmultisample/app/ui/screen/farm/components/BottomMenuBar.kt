package com.example.kotlinmultisample.app.ui.screen.farm.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinmultisample.app.ui.screen.farm.model.FarmDestination

/**
 * 하단 메뉴바
 * 내 밭, 창고, 농부일기 등을 이동할 수 있는 네비게이션 바 역할
 */
@Composable
fun BottomMenuBarNew(
    currentDestination: FarmDestination,
    onTabSelected: (FarmDestination) -> Unit
) {
	Column(modifier = Modifier.fillMaxWidth()) {
		// 상단 구분선: 진한 잔디색 띠를 둘러 밭과 메뉴 영역을 명확히 구분 (땅의 단면 느낌 연출)
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.height(4.dp)
				.background(FarmColors.getDarkGrass())
		)

		Surface(
			modifier = Modifier.fillMaxWidth(),
			color = FarmColors.getDarkGround(),
			shape = RectangleShape // 하단 바는 라운드 없이 직사각형으로 바닥에 고정
		) {
			Row(
				modifier = Modifier.padding(vertical = 12.dp),
				horizontalArrangement = Arrangement.SpaceAround
			) {
				BottomMenuItemNew(
                    "🏠", 
                    "내 밭", 
                    isSelected = currentDestination == FarmDestination.Main, 
                    onClick = { onTabSelected(FarmDestination.Main) }
                )
				BottomMenuItemNew(
                    "📦", 
                    "창고", 
                    isSelected = currentDestination == FarmDestination.Warehouse, 
                    onClick = { onTabSelected(FarmDestination.Warehouse) }
                )
				BottomMenuItemNew(
                    "📔", 
                    "농부일기", 
                    isSelected = currentDestination == FarmDestination.Diary, 
                    onClick = { onTabSelected(FarmDestination.Diary) }
                )
				BottomMenuItemNew(
                    "🎒", 
                    "가방", 
                    isSelected = currentDestination == FarmDestination.Bag, 
                    onClick = { onTabSelected(FarmDestination.Bag) }
                )
				BottomMenuItemNew(
                    "🧮", 
                    "계산", 
                    isSelected = currentDestination == FarmDestination.Calculate, 
                    onClick = { onTabSelected(FarmDestination.Calculate) }
                )
			}
		}
	}
}

@Composable
fun BottomMenuItemNew(
    icon: String, 
    label: String, 
    isSelected: Boolean = false, 
    onClick: () -> Unit = {}
) {
    val contentColor = if (isSelected) FarmColors.getGold() else FarmColors.getGold().copy(alpha = 0.5f)
	
    Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier.clickable(onClick = onClick)
	) {
		Text(icon, fontSize = 20.sp)
		Spacer(modifier = Modifier.height(4.dp))
		Text(
            label, 
            fontSize = 10.sp, 
            color = contentColor, 
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
	}
}
