package com.example.kotlinmultisample.app.ui.screen.farm.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinmultisample.app.ui.screen.farm.theme.FarmColors
import com.example.kotlinmultisample.app.ui.screen.farm.theme.PixelShape

/**
 * 픽셀 아트 스타일의 카드 컴포넌트
 */
@Composable
fun PixelCard(
	modifier: Modifier = Modifier,
	containerColor: Color? = null,
	borderColor: Color? = null,
	content: @Composable ColumnScope.() -> Unit
) {
	val actualContainerColor = containerColor ?: FarmColors.getField()
	val actualBorderColor = borderColor ?: FarmColors.getDarkGround()

	Surface(
		modifier = modifier
			.border(BorderStroke(2.dp, actualBorderColor), PixelShape),
		color = actualContainerColor,
		shape = PixelShape
	) {
		Column(
			modifier = Modifier.padding(8.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			content()
		}
	}
}

/**
 * 픽셀 아트 스타일의 버튼 컴포넌트
 */
@Composable
fun PixelButton(
	text: String,
	modifier: Modifier = Modifier,
	containerColor: Color? = null,
	contentColor: Color? = null,
	borderColor: Color? = null,
	onClick: (() -> Unit)? = null
) {
	val actualContainerColor = containerColor ?: FarmColors.getGrass()
	val actualContentColor = contentColor ?: FarmColors.getGold()
	val actualBorderColor = borderColor ?: FarmColors.getDarkGrass()

	val clickableModifier = if (onClick != null) {
		modifier.clickable(onClick = onClick)
	} else {
		modifier
	}

	Box(
		modifier = clickableModifier
			.height(40.dp)
			.background(actualContainerColor, PixelShape)
			.border(BorderStroke(2.dp, actualBorderColor), PixelShape)
			.padding(horizontal = 4.dp), // 내부 패딩 축소하여 공간 확보
		contentAlignment = Alignment.Center
	) {
		var multiplier by remember { mutableStateOf(1f) }

		Text(
			text = text,
			color = actualContentColor,
			fontSize = (12 * multiplier).sp,
			fontWeight = FontWeight.Bold,
			maxLines = 1,
			softWrap = false,
			overflow = TextOverflow.Visible,
			textAlign = TextAlign.Center,
			onTextLayout = { textLayoutResult ->
				if (textLayoutResult.hasVisualOverflow && multiplier > 0.5f) {
					multiplier *= 0.9f
				}
			}
		)
	}
}

@Preview
@Composable
fun PixelCardPreview() {
	Column(
		modifier = Modifier.padding(16.dp),
		verticalArrangement = Arrangement.spacedBy(16.dp)
	) {
		PixelCard {
			Text("기본 카드", fontSize = 14.sp, fontWeight = FontWeight.Bold)
			Spacer(modifier = Modifier.height(8.dp))
			Text("내용이 들어갑니다.")
		}
		
		PixelCard(containerColor = FarmColors.getNight(), borderColor = FarmColors.getMoon()) {
			Text("밤하늘 카드", color = FarmColors.getMoon(), fontSize = 14.sp)
		}
	}
}

@Preview
@Composable
fun PixelButtonPreview() {
	Column(
		modifier = Modifier.padding(16.dp),
		verticalArrangement = Arrangement.spacedBy(8.dp)
	) {
		PixelButton("기본 버튼", onClick = {})
		PixelButton("강조 버튼", containerColor = FarmColors.getFire(), contentColor = Color.White, onClick = {})
		PixelButton("비활성 느낌", containerColor = Color.Gray, borderColor = Color.DarkGray, onClick = {})
	}
}
