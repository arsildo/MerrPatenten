package com.arsildo.merrpatenten

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        startDestination = Destinations.LANDING_ROUTE,
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        composable(route = Destinations.LANDING_ROUTE) {
            /*MainScreen(navController)*/
            LandingScreen(navController = navController)
        }
        composable(route = Destinations.EXAM_ROUTE) {
            ExamScreen(navController)
        }
        composable(route = Destinations.STATISTICS_ROUTE) {
            StatisticsScreen(navController = navController)
        }
        composable(route = Destinations.PREFERENCES_ROUTE) {
            /* PreferencesScreen(navController)*/
            PreferencesScreen(navController = navController)
        }
    }

}