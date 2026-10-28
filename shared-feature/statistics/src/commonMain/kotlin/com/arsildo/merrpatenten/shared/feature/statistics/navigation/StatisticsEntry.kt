package com.arsildo.merrpatenten.shared.feature.statistics.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.arsildo.merrpatenten.shared.feature.statistics.ui.StatisticsRoute
import kotlinx.serialization.Serializable

@Serializable
object Statistics : NavKey

fun EntryProviderScope<NavKey>.statisticsEntry(onChangePreferenceClick: () -> Unit) {
    entry<Statistics> {
        StatisticsRoute(
            onChangePreferenceClick = onChangePreferenceClick,
        )
    }
}
