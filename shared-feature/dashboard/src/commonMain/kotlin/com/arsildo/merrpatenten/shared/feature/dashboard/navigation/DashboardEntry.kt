package com.arsildo.merrpatenten.shared.feature.dashboard.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.arsildo.merrpatenten.shared.feature.dashboard.ui.DashboardRoute
import com.arsildo.merrpatenten.shared.feature.dashboard.ui.components.DisclaimerBottomSheet
import kotlinx.serialization.Serializable
import navigation.BottomSheetSceneStrategy

@Serializable
object Dashboard : NavKey

@Serializable
object Disclaimer : NavKey

@OptIn(ExperimentalMaterial3Api::class)
fun EntryProviderScope<NavKey>.dashboardEntry(
    backStack: NavBackStack<NavKey>,
    onStartExamClick: (category: String) -> Unit,
    onStartBlitzExamClick: (category: String) -> Unit,
    onStatisticsClick: () -> Unit,
    onPreferencesClick: () -> Unit,
    onCatalogClick: () -> Unit,
    onMistakesClick: () -> Unit,
) {
    entry<Dashboard> {
        DashboardRoute(
            onStartExamClick = onStartExamClick,
            onStartBlitzExamClick = onStartBlitzExamClick,
            onStatisticsClick = onStatisticsClick,
            onPreferencesClick = onPreferencesClick,
            onCatalogClick = onCatalogClick,
            onMistakesClick = onMistakesClick,
            onDisclaimerClick = { backStack.add(Disclaimer) },
        )
    }

    entry<Disclaimer>(
        metadata = BottomSheetSceneStrategy.bottomSheet(skipPartiallyExpanded = true),
    ) {
        DisclaimerBottomSheet(
            onDismiss = dropUnlessResumed(block = backStack::removeLastOrNull),
        )
    }
}
