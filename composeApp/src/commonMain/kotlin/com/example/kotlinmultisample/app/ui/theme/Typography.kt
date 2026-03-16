package com.example.kotlinmultisample.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import kotlinmultisample.composeapp.generated.resources.*
import org.jetbrains.compose.resources.Font

@Composable
fun getTypography(): Typography {
    val pretendardFamily = FontFamily(
        Font(Res.font.Pretendard_Regular, FontWeight.Normal),
        Font(Res.font.Pretendard_Bold, FontWeight.Bold),
        Font(Res.font.Pretendard_Medium, FontWeight.Medium),
        Font(Res.font.Pretendard_SemiBold, FontWeight.SemiBold),
    )

    val defaultTypography = Typography()
    return Typography(
        displayLarge = defaultTypography.displayLarge.copy(fontFamily = pretendardFamily, fontWeight = FontWeight.Normal),
        displayMedium = defaultTypography.displayMedium.copy(fontFamily = pretendardFamily, fontWeight = FontWeight.Normal),
        displaySmall = defaultTypography.displaySmall.copy(fontFamily = pretendardFamily, fontWeight = FontWeight.Normal),
        headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = pretendardFamily, fontWeight = FontWeight.SemiBold),
        headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = pretendardFamily, fontWeight = FontWeight.SemiBold),
        headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = pretendardFamily, fontWeight = FontWeight.SemiBold),
        titleLarge = defaultTypography.titleLarge.copy(fontFamily = pretendardFamily, fontWeight = FontWeight.Bold),
        titleMedium = defaultTypography.titleMedium.copy(fontFamily = pretendardFamily, fontWeight = FontWeight.Medium),
        titleSmall = defaultTypography.titleSmall.copy(fontFamily = pretendardFamily, fontWeight = FontWeight.Medium),
        bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = pretendardFamily, fontWeight = FontWeight.Normal),
        bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = pretendardFamily, fontWeight = FontWeight.Normal),
        bodySmall = defaultTypography.bodySmall.copy(fontFamily = pretendardFamily, fontWeight = FontWeight.Normal),
        labelLarge = defaultTypography.labelLarge.copy(fontFamily = pretendardFamily, fontWeight = FontWeight.Medium),
        labelMedium = defaultTypography.labelMedium.copy(fontFamily = pretendardFamily, fontWeight = FontWeight.Medium),
        labelSmall = defaultTypography.labelSmall.copy(fontFamily = pretendardFamily, fontWeight = FontWeight.Medium)
    )
}
