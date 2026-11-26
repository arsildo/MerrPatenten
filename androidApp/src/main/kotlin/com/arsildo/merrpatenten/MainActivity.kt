package com.arsildo.merrpatenten

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.arsildo.merrpatenten.shared.core.designsystem.ApplicationLocaleManager
import com.arsildo.merrpatenten.shared.ui.MerrPatentenApp

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        ApplicationLocaleManager.setDefaultLocale()
        WindowCompat.enableEdgeToEdge(window)
        setContent {
            MerrPatentenApp()
        }
    }
}
