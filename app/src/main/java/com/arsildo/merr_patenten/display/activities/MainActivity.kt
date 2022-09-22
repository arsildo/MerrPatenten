package com.arsildo.merr_patenten.display.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arsildo.merr_patenten.display.screens.ExamScreen
import com.arsildo.merr_patenten.display.screens.MainScreen
import com.arsildo.merr_patenten.display.theme.MerrPatentenTheme
import com.arsildo.merr_patenten.logic.navigation.Destinations

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MerrPatentenTheme() {
                val navController = rememberNavController()
                NavigationGraph(navController = navController)
            }
        }
    }

    @Composable
    fun NavigationGraph(navController: NavHostController) {
        NavHost(
            navController = navController,
            startDestination = Destinations.Main.route
        ) {
            composable(route = Destinations.Main.route) {
                MainScreen(navController)
            }
            composable(route = Destinations.Exam.route){
                ExamScreen()
            }
        }

    }
}
