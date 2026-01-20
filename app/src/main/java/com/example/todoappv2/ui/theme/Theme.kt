package com.example.todoappv2.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = BlueOnPrimary,
    secondary = BlueSecondary,
    onSecondary = BlueOnSecondary,

    background = BackgroundLight,
    surface = SurfaceLight,

    onBackground = TextPrimary,
    onSurface = TextPrimary,

    error = ErrorRed
)

private val DarkColorScheme = darkColorScheme(
    primary = BlueSecondary,
    onPrimary = BlueOnPrimary,
    secondary = BluePrimary,
    onSecondary = BlueOnSecondary,

    background = BackgroundDark,
    surface = SurfaceDark,

    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,

    error = ErrorRed
)

@Composable
fun TodoAppV2Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}