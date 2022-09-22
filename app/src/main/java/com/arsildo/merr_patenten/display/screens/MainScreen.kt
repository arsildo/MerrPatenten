package com.arsildo.merr_patenten.display.screens

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.arsildo.merr_patenten.display.screens.components.ScreenLayout
import com.arsildo.merr_patenten.logic.navigation.Destinations

@Composable
fun MainScreen(
    navController: NavController,
) {
    ScreenLayout {
        Button(
            onClick = { navController.navigate(route = Destinations.Exam.route) }
        ) {
            Text(text = "Navigate")
        }
    }
    //TODO Fill Display
}