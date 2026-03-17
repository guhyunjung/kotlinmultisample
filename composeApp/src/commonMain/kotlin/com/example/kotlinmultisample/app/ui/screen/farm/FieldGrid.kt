package com.example.kotlinmultisample.app.ui.screen.farm

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 주식 종목(씨앗)들이 심어져 있는 밭을 표현하는 그리드
 */
@Composable
fun FieldGridNew(
    seeds: List<FarmSeed>,
    onSeedClick: (FarmSeed) -> Unit,
    onAddSeedClick: () -> Unit
) {
	LazyVerticalGrid(
		columns = GridCells.Fixed(3), // 3열 그리드
		contentPadding = PaddingValues(bottom = 16.dp),
		horizontalArrangement = Arrangement.spacedBy(8.dp),
		verticalArrangement = Arrangement.spacedBy(8.dp)
	) {
		items(seeds) { seed ->
			Box(modifier = Modifier.fillMaxWidth().clickable { onSeedClick(seed) }) {
				// 각 종목을 픽셀 카드 형태로 표현
				PixelCard(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
					Text(seed.emoji, fontSize = 24.sp)
					Text(
						seed.name,
						fontSize = 10.sp,
						color = FarmColors.getGold(),
						fontWeight = FontWeight.Bold
					)
					// 수익률에 따라 색상 분기 (양수: 초록, 음수: 빨강)
					Text(
						"${if (seed.pct > 0) "+" else ""}${seed.pct}%",
						fontSize = 11.sp,
						color = if (seed.pct >= 0) FarmColors.getLightGreen() else FarmColors.getSoftRed()
					)
				}
			}
		}
		
        // 종목 추가 버튼 (최대 9개까지만 생성 가능)
        if (seeds.size < 9) {
            item {
                Box(modifier = Modifier.fillMaxWidth().clickable { onAddSeedClick() }) {
                    PixelCard(
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                        containerColor = FarmColors.getDarkGround(),
                        borderColor = FarmColors.getGround()
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("+", fontSize = 24.sp, color = FarmColors.getLightGreen())
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("씨앗 심기", fontSize = 12.sp, color = FarmColors.getLightGreen())
                        }
                    }
                }
            }
        }
	}
}
