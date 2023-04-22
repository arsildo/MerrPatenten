package com.arsildo.merr_patenten

import androidx.compose.foundation.background
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arsildo.merr_patenten.landing.LandingScreen
import com.arsildo.merr_patenten.preferences.PreferencesScreen

@Composable
fun NavigationGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        route = ROOT_GRAPH,
        startDestination = Destinations.LANDING_ROUTE,
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        composable(route = Destinations.LANDING_ROUTE) {
            /*MainScreen(navController)*/
            LandingScreen(navController = navController)
        }
        composable(route = Destinations.EXAM_ROUTE) {
            /*ExamScreen(navController)*/
            Text(text = "Test")
        }
        composable(route = Destinations.STATISTICS_ROUTE) {
            /*StatisticsScreen(navController)*/
        }
        composable(route = Destinations.PREFERENCES_ROUTE) {
            /* PreferencesScreen(navController)*/
            PreferencesScreen(navController = navController)
        }
    }

}