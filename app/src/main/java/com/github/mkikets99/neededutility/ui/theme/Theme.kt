package com.github.mkikets99.neededutility.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary = Color(0xFF00696E),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF6FF6FC),
    onPrimaryContainer = Color(0xFF002022),
    secondary = Color(0xFF4A6365),
    secondaryContainer = Color(0xFFCCE8EA),
    onSecondaryContainer = Color(0xFF051F21),
    tertiary = Color(0xFF4B607C),
    tertiaryContainer = Color(0xFFD3E4FF),
    onTertiaryContainer = Color(0xFF041C35),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF4CDADF),
    onPrimary = Color(0xFF00373A),
    primaryContainer = Color(0xFF004F53),
    onPrimaryContainer = Color(0xFF6FF6FC),
    secondary = Color(0xFFB0CCCE),
    secondaryContainer = Color(0xFF324B4D),
    onSecondaryContainer = Color(0xFFCCE8EA),
    tertiary = Color(0xFFB3C8E8),
    tertiaryContainer = Color(0xFF334862),
    onTertiaryContainer = Color(0xFFD3E4FF),
)

/**
 * Material 3 theme with optional Material You dynamic colors (Android 12+)
 * and a light/dark palette fallback for older devices or disabled dynamic color.
 */
@Composable
fun NeededUtilityTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
    )
}
