package com.arsildo.merrpatenten

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arsildo.merrpatenten.exam.ExamScreen
import com.arsildo.merrpatenten.landing.LandingScreen
import com.arsildo.merrpatenten.preferences.PreferencesScreen
import com.arsildo.merrpatenten.statistics.StatisticsScreen

@Composable
fun NavigationGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        route = ROOT_GRAPH,
        startDestination = Destinations.LANDING_ROUTE
    ) {
        composable(route = Destinations.LANDING_ROUTE) {
            LandingScreen(
                onStartExamClick = { navController.navigate(Destinations.EXAM_ROUTE) },
                onStatisticsClick = { navController.navigate(Destinations.STATISTICS_ROUTE) },
                onPreferencesClick = { navController.navigate(Destinations.PREFERENCES_ROUTE) }
            )
        }
        composable(route = Destinations.EXAM_ROUTE) {
            ExamScreen(navController)
        }
        composable(route = Destinations.STATISTICS_ROUTE) {
            StatisticsScreen(
                onBackPress = navController::navigateUp
            )
        }
        composable(route = Destinations.PREFERENCES_ROUTE) {
            PreferencesScreen(
                onBackPress = navController::navigateUp
            )
        }
    }
}
