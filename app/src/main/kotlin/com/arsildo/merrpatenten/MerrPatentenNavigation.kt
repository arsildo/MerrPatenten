package com.arsildo.merrpatenten

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.arsildo.merrpatenten.NavigationArguments.IMAGE_ID
import com.arsildo.merrpatenten.Screens.EXAM_SCREEN
import com.arsildo.merrpatenten.Screens.DASHBOARD_SCREEN
import com.arsildo.merrpatenten.Screens.DISCLAIMER_DIALOG
import com.arsildo.merrpatenten.Screens.EXAM_IMAGE_DETAILS_SCREEN
import com.arsildo.merrpatenten.Screens.PREFERENCES_SCREEN
import com.arsildo.merrpatenten.Screens.STATISTICS_SCREEN

const val ROOT_GRAPH = "root"

private object Screens {
    const val DISCLAIMER_DIALOG = "disclaimer"
    const val DASHBOARD_SCREEN = "dashboard"
    const val EXAM_SCREEN = "exam"
    const val EXAM_IMAGE_DETAILS_SCREEN = "examQuestionDetails"

    const val STATISTICS_SCREEN = "statistics"
    const val PREFERENCES_SCREEN = "preferences"
}

object NavigationArguments {
    const val IMAGE_ID = "imageID"
}

object Destinations {
    const val DISCLAIMER_ROUTE = DISCLAIMER_DIALOG
    const val DASHBOARD_ROUTE = DASHBOARD_SCREEN
    const val EXAM_ROUTE = EXAM_SCREEN
    const val EXAM_IMAGE_DETAILS_ROUTE = "$EXAM_IMAGE_DETAILS_SCREEN/{$IMAGE_ID}"

    const val STATISTICS_ROUTE = STATISTICS_SCREEN
    const val PREFERENCES_ROUTE = PREFERENCES_SCREEN
}

class Navigate(private val navController: NavController) {
    fun navigateToQuestionImage(imageID: Int) {
        val route = "$EXAM_IMAGE_DETAILS_SCREEN/$imageID"
        navController.navigate(route = route){
            popUpTo(Destinations.EXAM_ROUTE){
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}