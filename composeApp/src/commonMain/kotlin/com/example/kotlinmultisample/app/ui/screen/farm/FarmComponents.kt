package com.example.kotlinmultisample.app.ui.screen.farm

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinmultisample.app.presentation.settings.SettingsViewModel
import com.example.kotlinmultisample.app.presentation.settings.ThemeMode
import org.koin.compose.koinInject

/**
 * 주식농장 전용 컬러 팔레트
 */
object FarmColors {
	@Composable
	fun getSky() = if (isDark()) Color(0xFF0A1E2D) else Color(0xFF87CEEB)

	@Composable
	fun getGround() = if (isDark()) Color(0xFF4A3208) else Color(0xFF8B6914)

	@Composable
	fun getField() = if (isDark()) Color(0xFF2D1E0A) else Color(0xFF6B4F10)

	@Composable
	fun getDarkGround() = if (isDark()) Color(0xFF1A0E00) else Color(0xFF4A3208)

	@Composable
	fun getGrass() = if (isDark()) Color(0xFF1A2E12) else Color(0xFF5C8A3C)

	@Composable
	fun getDarkGrass() = if (isDark()) Color(0xFF0D1A08) else Color(0xFF3D6128)

	@Composable
	fun getGold() = if (isDark()) Color(0xFFFFD700) else Color(0xFFFFE87C)

	@Composable
	fun getLightGreen() = if (isDark()) Color(0xFF81C784) else Color(0xFFA8E6A0)

	@Composable
	fun getSoftRed() = if (isDark()) Color(0xFFE57373) else Color(0xFFFFB3B3)

	@Composable
	fun getCloud() = if (isDark()) Color(0xFF2D4A1E).copy(alpha = 0.6f) else Color(0xFFFFFFFF)

	@Composable
	fun getNight() = if (isDark()) Color(0xFF000000) else Color(0xFF0A1E2D)

	@Composable
	fun getMoon() = if (isDark()) Color(0xFFE0E0E0) else Color(0xFF87CEEB)

	@Composable
	fun getTextPrimary() = Color.White

	@Composable
	fun getTextOnSky() = if (isDark()) Color(0xFF87CEEB) else Color(0xFF1A4A2E)

	// Action Colors
	@Composable
	fun getWater() = if (isDark()) Color(0xFF1A3A5F) else Color(0xFF3A6ABF)

	@Composable
	fun getFire() = if (isDark()) Color(0xFF5F2A1A) else Color(0xFFBF5A3A)

	@Composable
	fun getHarvest() = getGrass()

	@Composable
	fun getCut() = if (isDark()) Color(0xFF3A1A4A) else Color(0xFF6B3A8A)

	@Composable
	private fun isDark(): Boolean {
		val settingsViewModel = koinInject<SettingsViewModel>()
		val themeMode by settingsViewModel.themeMode.collectAsState()
		return when (themeMode) {
			ThemeMode.SYSTEM -> isSystemInDarkTheme()
			ThemeMode.LIGHT -> false
			ThemeMode.DARK -> true
		}
	}
}

/**
 * 픽셀 느낌을 주기 위한 컷 코너 쉐이프 (계단식 모서리)
 * 사용자의 요청으로 조금 더 부드러운 라운드 처리
 */
val PixelShape: Shape = RoundedCornerShape(12.dp)

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
	onClick: () -> Unit
) {
	val actualContainerColor = containerColor ?: FarmColors.getGrass()
	val actualContentColor = contentColor ?: FarmColors.getGold()
	val actualBorderColor = borderColor ?: FarmColors.getDarkGrass()

	Box(
		modifier = modifier
			.height(40.dp)
			.background(actualContainerColor, PixelShape)
			.border(BorderStroke(2.dp, actualBorderColor), PixelShape)
			.clickable(onClick = onClick)
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
