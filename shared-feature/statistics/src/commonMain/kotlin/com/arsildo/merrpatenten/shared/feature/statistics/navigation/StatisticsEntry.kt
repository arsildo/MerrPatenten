package com.arsildo.merrpatenten.shared.feature.statistics.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.arsildo.merrpatenten.shared.feature.statistics.ui.StatisticsRoute
import kotlinx.serialization.Serializable

@Serializable
object Statistics : NavKey

fun EntryProviderScope<NavKey>.statisticsEntry(
    backStack: NavBackStack<NavKey>,
    onBackPress: () -> Unit = { backStack.removeLastOrNull() },
    onChangePreferenceClick: () -> Unit
) {
    entry<Statistics> {
        StatisticsRoute(
            onBackPress = onBackPress,
            onChangePreferenceClick = onChangePreferenceClick
        )
    }
}

