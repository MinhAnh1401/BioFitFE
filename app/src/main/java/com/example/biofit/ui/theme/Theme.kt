package com.example.biofit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    inversePrimary = DarkInversePrimary,

    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,

    error = DarkError,
    onError = DarkOnError,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,
    inversePrimary = LightInversePrimary,

    secondary = LightSecondary,
    onSecondary = LightOnSecondary,
    secondaryContainer = LightSecondaryContainer,
    onSecondaryContainer = LightOnSecondaryContainer,

    error = LightError,
    onError = LightOnError,
    errorContainer = LightErrorContainer,
    onErrorContainer = LightOnErrorContainer
)

@Composable
fun getAdaptiveTypography(): androidx.compose.material3.Typography {
    val configuration = LocalConfiguration.current

    val baseTypography = Typography()

    val typography = when {
        configuration.screenWidthDp < 360 -> baseTypography // Small screen
        configuration.screenWidthDp in 360..480 -> baseTypography // Medium screen
        else -> baseTypography // Large screen
    }

    return typography.copy(
        displayLarge = typography.displayLarge.copy(fontFamily = SFProDisplay),
        displayMedium = typography.displayMedium.copy(fontFamily = SFProDisplay),
        displaySmall = typography.displaySmall.copy(fontFamily = SFProDisplay),

        headlineLarge = typography.headlineLarge.copy(fontFamily = SFProDisplay),
        headlineMedium = typography.headlineMedium.copy(fontFamily = SFProDisplay),
        headlineSmall = typography.headlineSmall.copy(fontFamily = SFProDisplay),

        titleLarge = typography.titleLarge.copy(fontFamily = SFProDisplay),
        titleMedium = typography.titleMedium.copy(fontFamily = SFProDisplay),
        titleSmall = typography.titleSmall.copy(fontFamily = SFProDisplay),

        bodyLarge = typography.bodyLarge.copy(fontFamily = SFProDisplay),
        bodyMedium = typography.bodyMedium.copy(fontFamily = SFProDisplay),
        bodySmall = typography.bodySmall.copy(fontFamily = SFProDisplay),

        labelLarge = typography.labelLarge.copy(fontFamily = SFProDisplay),
        labelMedium = typography.labelMedium.copy(fontFamily = SFProDisplay),
        labelSmall = typography.labelSmall.copy(fontFamily = SFProDisplay)
    )
}

@Composable
fun BioFitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}