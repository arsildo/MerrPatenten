package com.arsildo.merr_patenten.logic.navigation

sealed class Destinations(val route: String) {
    object Main : Destinations(route = "main_screen")
    object Exam : Destinations(route = "exam_screen")
    object Preferences : Destinations(route = "preferences_screen")
    object Statistics : Destinations(route = "statistics_screen")
}
