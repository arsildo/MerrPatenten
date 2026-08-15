package com.arsildo.merrpatenten

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.arsildo.merrpatenten.shared.core.designsystem.ApplicationLocaleManager
import com.arsildo.merrpatenten.shared.ui.MerrPatentenApp

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        ApplicationLocaleManager.setDefaultLocale()
        enableEdgeToEdge()
        setContent {
            MerrPatentenApp()
        }
    }
}
