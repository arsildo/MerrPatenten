package com.arsildo.merrpatenten.shared.feature.dashboard.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.arsildo.merrpatenten.shared.feature.dashboard.DashboardRoute
import kotlinx.serialization.Serializable

@Serializable
object Dashboard : NavKey

@Serializable
object Disclaimer : NavKey

fun EntryProviderScope<NavKey>.dashboardEntry(
    backStack: NavBackStack<NavKey>,
    onStartExamClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    onPreferencesClick: () -> Unit,
) {
    entry<Dashboard> {
        DashboardRoute(
            onStartExamClick = onStartExamClick,
            onStatisticsClick = onStatisticsClick,
            onPreferencesClick = onPreferencesClick
        )
    }
}

