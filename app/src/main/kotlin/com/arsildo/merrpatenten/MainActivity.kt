package com.arsildo.merrpatenten

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arsildo.merrpatenten.preferences.PreferencesViewModel
import com.arsildo.merrpatenten.theme.MerrPatentenTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        installSplashScreen()

        setContent {

            val preferencesViewModel = koinViewModel<PreferencesViewModel>()
            val uiState by preferencesViewModel.uiState.collectAsStateWithLifecycle()

            MerrPatentenTheme(
                darkTheme = if (uiState.followSystemColors) isSystemInDarkTheme() else uiState.colorScheme,
                dynamicColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) uiState.dynamicColorScheme else false
            ) {
                MerrPatentetenGraph()
            }
        }
    }
}
