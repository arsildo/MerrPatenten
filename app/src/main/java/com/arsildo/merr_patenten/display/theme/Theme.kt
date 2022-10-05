package com.arsildo.merr_patenten.display.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val LightColorPalette = lightColors(
    primary = PrussianBlue,
    onPrimary = White,

    secondary = PrussianBlue,
    onSecondary = White,

    surface = White,
    onSurface = Black,

    background = OffWhite,
    onBackground = Black,
)

private val DarkColorPalette = darkColors(
    primary = KappelGreen,
    onPrimary = White,

    secondary = KappelGreen,
    onSecondary = White,

    surface = Black,
    onSurface = White,

    background = OffBlack,
    onBackground = Black,
)

@Composable
fun MerrPatentenTheme(
    themePreference: String,
    content: @Composable () -> Unit,
) {

    val systemUiController = rememberSystemUiController()
    val colors = when (themePreference) {
        "light_mode" -> {
            systemUiController.setNavigationBarColor(LightColorPalette.background)
            systemUiController.setStatusBarColor(LightColorPalette.background)
            LightColorPalette
        }

        "dark_mode" -> {
            systemUiController.setNavigationBarColor(DarkColorPalette.background)
            systemUiController.setStatusBarColor(DarkColorPalette.background)
            DarkColorPalette
        }

        else -> LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}