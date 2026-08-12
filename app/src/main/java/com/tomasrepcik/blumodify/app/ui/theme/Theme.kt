package com.tomasrepcik.blumodify.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = ColorWhiteCultured,
    secondary = ColorBlueIceberg,
    tertiary = ColorRedFleryRose,
    background = ColorBlack,
    surface = ColorBlack,
    onPrimary = ColorChineseBlack,
    onSecondary = ColorWhiteCultured,
    onTertiary = ColorWhiteCultured,
    onBackground = ColorWhiteCultured,
    onSurface = ColorChineseBlack,
    surfaceTint = ColorWhiteCultured
)

private val LightColorScheme = lightColorScheme(
    primary = ColorWhiteCultured,
    secondary = ColorBlueIceberg,
    tertiary = ColorRedFleryRose,
    background = ColorBlueIceberg,
    surface = ColorBlueIceberg,
    onPrimary = ColorChineseBlack,
    onSecondary = ColorWhiteCultured,
    onTertiary = ColorWhiteCultured,
    onBackground = ColorWhiteCultured,
    onSurface = ColorWhiteCultured,
    surfaceTint = ColorBlack
)

@Composable
fun BluModifyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    MaterialTheme(
        colorScheme = colorScheme, typography = customTypography, content = content
    )
}
