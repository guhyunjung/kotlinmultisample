package com.example.kotlinmultisample.app.ui.screen.farm.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
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
		if (LocalInspectionMode.current) return false // Preview 모드일 경우 기본값(Light) 반환

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
