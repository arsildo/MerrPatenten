package com.arsildo.merr_patenten.display.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200,
    background = White // TODO CHANGE
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200,
    background = White
)

@Composable
fun MerrPatentenTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val systemUiController = rememberSystemUiController()
    val colors = if (darkTheme) {
        systemUiController.setNavigationBarColor(MaterialTheme.colors.background)
        systemUiController.setStatusBarColor(MaterialTheme.colors.background)
        DarkColorPalette
    } else {
        systemUiController.setNavigationBarColor(MaterialTheme.colors.background)
        systemUiController.setStatusBarColor(MaterialTheme.colors.background)
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}