package com.arsildo.merr_patenten

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arsildo.merr_patenten.preferences.PreferencesViewModel
import com.arsildo.merr_patenten.theme.MerrPatentenTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        installSplashScreen()

        setContent {

            val preferencesViewModel = hiltViewModel<PreferencesViewModel>()
            val uiState by preferencesViewModel.uiState.collectAsStateWithLifecycle()

            MerrPatentenTheme(
                darkTheme = if (uiState.followSystemColors) isSystemInDarkTheme() else uiState.colorScheme,
                dynamicColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) uiState.dynamicColorScheme else false
            ) {
                NavigationGraph()
            }
        }
    }
}
