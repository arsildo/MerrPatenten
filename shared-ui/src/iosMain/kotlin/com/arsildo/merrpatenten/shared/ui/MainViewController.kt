package com.arsildo.merrpatenten.shared.ui

import androidx.compose.ui.window.ComposeUIViewController
import com.arsildo.merrpatenten.shared.ui.di.initKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    initKoin()
    return ComposeUIViewController {
        MerrPatentenApp()
    }
}

