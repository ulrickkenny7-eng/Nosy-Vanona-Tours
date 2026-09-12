package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
    darkColorScheme(
        primary = PrimaryTeal,
        secondary = SecondaryCoral,
        tertiary = TertiaryGold,
        background = Color(0xFF121212),
        surface = Color(0xFF1E1E1E),
        onPrimary = SurfaceWhite,
        onSecondary = SurfaceWhite,
        onBackground = SurfaceWhite,
        onSurface = SurfaceWhite
    )

private val LightColorScheme =
    lightColorScheme(
        primary = PrimaryTeal,
        secondary = SecondaryCoral,
        tertiary = TertiaryGold,
        background = BackgroundSand,
        surface = SurfaceWhite,
        onPrimary = SurfaceWhite,
        onSecondary = SurfaceWhite,
        onBackground = TextDark,
        onSurface = TextDark
    )

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }
            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
