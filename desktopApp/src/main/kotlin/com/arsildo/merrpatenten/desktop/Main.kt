package com.arsildo.merrpatenten.desktop

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arsildo.merrpatenten.shared.core.designsystem.ApplicationLocaleManager
import com.arsildo.merrpatenten.shared.ui.MerrPatentenApp
import com.arsildo.merrpatenten.shared.ui.di.initKoin

fun main() {
    ApplicationLocaleManager.setDefaultLocale()
    initKoin()
    application {
        Window(
            state = rememberWindowState(
                size = DpSize(width = 440.dp, height = 900.dp)
            ),
            onCloseRequest = ::exitApplication,
            resizable = true,
            title = "Merr Patenten"
        ) {
            MerrPatentenApp()
        }
    }
}
