package com.arsildo.merr_patenten.display.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = KappelGreen,
    onPrimary = White,

    secondary = PrussianBlue,
    onSecondary = White,

    surface = White,
    onSurface = Black,

    background = OffBlack,
    onBackground = Black,
)

private val LightColorPalette = lightColors(
    primary = KappelGreen,
    onPrimary = White,

    secondary = PrussianBlue,
    onSecondary = White,

    surface = White,
    onSurface = Black,

    background = OffWhite,
    onBackground = Black,
)

@Composable
fun MerrPatentenTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val systemUiController = rememberSystemUiController()
    val colors = if (darkTheme) {
        systemUiController.setNavigationBarColor(LightColorPalette.background)
        systemUiController.setStatusBarColor(LightColorPalette.background)
        LightColorPalette // BLACK THEME todo
    } else {
        systemUiController.setNavigationBarColor(LightColorPalette.background)
        systemUiController.setStatusBarColor(LightColorPalette.background)
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}