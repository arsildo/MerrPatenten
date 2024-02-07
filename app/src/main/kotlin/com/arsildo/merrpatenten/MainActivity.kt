package com.arsildo.merrpatenten

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.arsildo.merrpatenten.dashboard.DashboardScreen
import com.arsildo.merrpatenten.disclaimer.DisclaimerDialog
import com.arsildo.merrpatenten.exam.ExamScreen
import com.arsildo.merrpatenten.exam.imagedetails.ZoomableExamImage
import com.arsildo.merrpatenten.preferences.PreferencesScreen
import com.arsildo.merrpatenten.preferences.PreferencesViewModel
import com.arsildo.merrpatenten.statistics.StatisticsScreen
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
                dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && uiState.dynamicColorScheme
            ) {
                MerrPatentenNavigationGraph()
            }
        }
    }
}

@Composable
fun MerrPatentenNavigationGraph() {
    val navController = rememberNavController()
    var showDisclaimerOnce by remember { mutableStateOf(false) }
    NavHost(
        navController = navController,
        route = ROOT_GRAPH,
        startDestination = Destinations.DASHBOARD_ROUTE
    ) {
        dialog(route = Destinations.DISCLAIMER_ROUTE) {
            DisclaimerDialog(
                onDismissRequest = navController::navigateUp
            )
        }
        composable(
            route = Destinations.DASHBOARD_ROUTE,
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
            DashboardScreen(
                onStartExamClick = { navController.navigate(Destinations.EXAM_ROUTE) },
                onStatisticsClick = { navController.navigate(Destinations.STATISTICS_ROUTE) },
                onPreferencesClick = { navController.navigate(Destinations.PREFERENCES_ROUTE) }
            )
            LaunchedEffect(Unit) {
                if (!showDisclaimerOnce) {
                    showDisclaimerOnce = true
                    navController.navigate(Destinations.DISCLAIMER_ROUTE)
                }
            }
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
            ExamScreen(
                navController = navController,
                onImageDetailsClick = Navigate(navController)::navigateToQuestionImage
            )
        }
        dialog(
            route = Destinations.EXAM_IMAGE_DETAILS_ROUTE,
            arguments = listOf(
                navArgument(NavigationArguments.IMAGE_ID) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            ZoomableExamImage(
                imageID = backStackEntry.arguments?.getInt(NavigationArguments.IMAGE_ID)!!,
                onDismiss = navController::navigateUp
            )
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

