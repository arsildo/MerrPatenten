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
        val splashScreen = installSplashScreen()
        splashScreen.setOnExitAnimationListener { provider ->
            val remaining = (provider.iconAnimationStartMillis + provider.iconAnimationDurationMillis - System.currentTimeMillis())
                .coerceIn(0L, 650L)
            provider.iconView.animate()
                .setStartDelay(remaining)
                .scaleX(1.25f)
                .scaleY(1.25f)
                .alpha(0f)
                .setDuration(220L)
                .start()
            provider.view.animate()
                .setStartDelay(remaining)
                .alpha(0f)
                .setDuration(220L)
                .withEndAction { provider.remove() }
                .start()
        }
        super.onCreate(savedInstanceState)
        ApplicationLocaleManager.setDefaultLocale()
        WindowCompat.enableEdgeToEdge(window)
        setContent {
            MerrPatentenApp()
        }
    }
}
