package com.example.biofit.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.biofit.R

val SFProDisplay = FontFamily(Font(R.font.sf_pro_rounded_regular))

/*
val Typography = Typography(
    // Body Text Styles
    bodyLarge = TextStyle(
        fontFamily = SFProDisplay,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp, // Tăng readability
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = SFProDisplay,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = SFProDisplay,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.4.sp
    ),

    // Title Text Styles
    titleLarge = TextStyle(
        fontFamily = SFProDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = SFProDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = SFProDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.1.sp
    ),

    // Display Text Styles
    displayLarge = TextStyle(
        fontFamily = SFProDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
        lineHeight = 48.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = SFProDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = (-0.15).sp
    ),
    displaySmall = TextStyle(
        fontFamily = SFProDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.1).sp
    ),

    // Headline Text Styles
    headlineLarge = TextStyle(
        fontFamily = SFProDisplay,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = SFProDisplay,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.1.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = SFProDisplay,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.15.sp
    ),

    // Label Text Styles (Buttons, captions, etc.)
    labelLarge = TextStyle(
        fontFamily = SFProDisplay,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = SFProDisplay,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = SFProDisplay,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)*/

val Typography = Typography().copy(
    displayLarge = Typography().displayLarge.copy(fontFamily = SFProDisplay),
    displayMedium = Typography().displayMedium.copy(fontFamily = SFProDisplay),
    displaySmall = Typography().displaySmall.copy(fontFamily = SFProDisplay),

    headlineLarge = Typography().headlineLarge.copy(fontFamily = SFProDisplay),
    headlineMedium = Typography().headlineMedium.copy(fontFamily = SFProDisplay),
    headlineSmall = Typography().headlineSmall.copy(fontFamily = SFProDisplay),

    titleLarge = Typography().titleLarge.copy(fontFamily = SFProDisplay),
    titleMedium = Typography().titleMedium.copy(fontFamily = SFProDisplay),
    titleSmall = Typography().titleSmall.copy(fontFamily = SFProDisplay),

    bodyLarge = Typography().bodyLarge.copy(fontFamily = SFProDisplay),
    bodyMedium = Typography().bodyMedium.copy(fontFamily = SFProDisplay),
    bodySmall = Typography().bodySmall.copy(fontFamily = SFProDisplay),

    labelLarge = Typography().labelLarge.copy(fontFamily = SFProDisplay),
    labelMedium = Typography().labelMedium.copy(fontFamily = SFProDisplay),
    labelSmall = Typography().labelSmall.copy(fontFamily = SFProDisplay)
)