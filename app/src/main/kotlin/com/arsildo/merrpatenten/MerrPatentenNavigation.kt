package com.arsildo.merrpatenten

import com.arsildo.merrpatenten.Screens.EXAM_SCREEN
import com.arsildo.merrpatenten.Screens.DASHBOARD_SCREEN
import com.arsildo.merrpatenten.Screens.DISCLAIMER_DIALOG
import com.arsildo.merrpatenten.Screens.PREFERENCES_SCREEN
import com.arsildo.merrpatenten.Screens.STATISTICS_SCREEN

const val ROOT_GRAPH = "root"

private object Screens {
    const val DISCLAIMER_DIALOG = "disclaimer"
    const val DASHBOARD_SCREEN = "dashboard"
    const val EXAM_SCREEN = "exam"

    const val STATISTICS_SCREEN = "statistics"
    const val PREFERENCES_SCREEN = "preferences"
}

object Destinations {
    const val DISCLAIMER_ROUTE = DISCLAIMER_DIALOG
    const val DASHBOARD_ROUTE = DASHBOARD_SCREEN
    const val EXAM_ROUTE = EXAM_SCREEN

    const val STATISTICS_ROUTE = STATISTICS_SCREEN
    const val PREFERENCES_ROUTE = PREFERENCES_SCREEN
}