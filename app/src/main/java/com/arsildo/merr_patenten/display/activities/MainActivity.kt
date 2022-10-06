package com.arsildo.merr_patenten.display.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arsildo.merr_patenten.display.screens.ExamScreen
import com.arsildo.merr_patenten.display.screens.MainScreen
import com.arsildo.merr_patenten.display.screens.PreferencesScreen
import com.arsildo.merr_patenten.display.theme.MerrPatentenTheme
import com.arsildo.merr_patenten.logic.cache.UserPreferences
import com.arsildo.merr_patenten.logic.navigation.Destinations

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val dataStore = UserPreferences(LocalContext.current)
            val theme = dataStore.getThemePreference.collectAsState(initial = "light_mode").value
            MerrPatentenTheme(themePreference = theme) {
                val navController = rememberNavController()
                NavigationGraph(navController = navController)
            }
        }
    }

    @Composable
    fun NavigationGraph(
        navController: NavHostController,
    ) {
        NavHost(
            navController = navController,
            startDestination = Destinations.Main.route,
            modifier = Modifier.background(MaterialTheme.colors.background)
        ) {
            composable(route = Destinations.Main.route) {
                MainScreen(navController)
            }
            composable(route = Destinations.Exam.route) {
                ExamScreen(navController)
            }
            composable(route = Destinations.Preferences.route) {
                PreferencesScreen(navController)
            }
        }

    }
}
