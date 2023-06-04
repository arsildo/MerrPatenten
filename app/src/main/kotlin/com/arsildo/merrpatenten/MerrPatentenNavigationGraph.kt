package com.arsildo.merrpatenten

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
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
        composable(
            route = Destinations.LANDING_ROUTE,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween()
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween()
                )
            }
        ) {
            LandingScreen(
                onStartExamClick = { navController.navigate(Destinations.EXAM_ROUTE) },
                onStatisticsClick = { navController.navigate(Destinations.STATISTICS_ROUTE) },
                onPreferencesClick = { navController.navigate(Destinations.PREFERENCES_ROUTE) }
            )
        }
        composable(
            route = Destinations.EXAM_ROUTE,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween()
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween()
                )
            }
        ) {
            ExamScreen(navController)
        }
        composable(
            route = Destinations.STATISTICS_ROUTE,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween()
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween()
                )
            }
        ) {
            StatisticsScreen(
                onBackPress = navController::navigateUp,
                onChangePreferenceClick = { navController.navigate(Destinations.PREFERENCES_ROUTE) }
            )
        }
        composable(
            route = Destinations.PREFERENCES_ROUTE,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween()
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween()
                )
            }
        ) {
            PreferencesScreen(
                onBackPress = navController::navigateUp
            )
        }
    }
}
