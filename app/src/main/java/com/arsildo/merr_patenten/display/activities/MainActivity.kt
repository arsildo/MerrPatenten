package com.arsildo.merr_patenten.display.activities

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arsildo.merr_patenten.display.screens.MainScreen
import com.arsildo.merr_patenten.logic.constants.BaseActivity
import com.arsildo.merr_patenten.logic.navigation.Destinations

class MainActivity : BaseActivity() {

    @Composable
    override fun Content() {
        val navController = rememberNavController()
        NavigationGraph(navController = navController)
    }

    @Composable
    fun NavigationGraph(navController: NavHostController) {
        NavHost(
            navController = navController,
            startDestination = Destinations.Main.route
        ) {
            composable(route = Destinations.Main.route) {
                MainScreen()
            }
        }

    }
}
