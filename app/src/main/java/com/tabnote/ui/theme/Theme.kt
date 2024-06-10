package com.tabnote.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDarkTheme,
    onPrimary = BackGroundDarkTheme,
    secondary = ExtendedFontColorDarkTheme,
    onSecondary = ExtendedColorDarkTheme,
    tertiary = Color.White,
    onSecondaryContainer = Color.White,
    background = BackGroundDarkTheme,
    surface = SurfaceDarkTheme,
    onSurfaceVariant = Color.LightGray,
    surfaceTint = SurfaceTintDarkTheme
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    secondary = ExtendedFontColor,
    onSecondary = ExtendedColor,
    tertiary = Color.Black,
    onSecondaryContainer = Color.Black,
    background = BackGround,
    surface = Surface,
    onSurfaceVariant = Color.Gray,
    surfaceTint = SurfaceTint,

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun TabNoteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = Color.Transparent.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}