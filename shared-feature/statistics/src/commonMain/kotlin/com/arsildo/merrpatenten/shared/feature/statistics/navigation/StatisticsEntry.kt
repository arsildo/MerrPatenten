package com.arsildo.merrpatenten.shared.feature.statistics.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.arsildo.merrpatenten.shared.feature.statistics.ui.StatisticsRoute
import com.arsildo.merrpatenten.shared.feature.statistics.ui.components.DeleteResultsBottomSheetRoute
import kotlinx.serialization.Serializable
import navigation.BottomSheetSceneStrategy
import navigation.SharedViewModelStoreNavEntryDecorator
import navigation.toContentKey

@Serializable
object Statistics : NavKey

@Serializable
object DeleteResultsBottomSheet : NavKey

@OptIn(ExperimentalMaterial3Api::class)
fun EntryProviderScope<NavKey>.statisticsEntry(backStack: NavBackStack<NavKey>, onChangePreferenceClick: () -> Unit) {
    entry<Statistics>(
        clazzContentKey = { key -> key.toContentKey() },
    ) {
        StatisticsRoute(
            onChangePreferenceClick = onChangePreferenceClick,
            onDeleteResultsClick = { backStack.add(DeleteResultsBottomSheet) },
        )
    }

    entry<DeleteResultsBottomSheet>(
        clazzContentKey = { key -> key.toContentKey() },
        metadata = SharedViewModelStoreNavEntryDecorator.parent(
            contentKey = Statistics.toContentKey(),
        ) + BottomSheetSceneStrategy.bottomSheet(skipPartiallyExpanded = true),
    ) {
        DeleteResultsBottomSheetRoute(
            onConfirm = { backStack.removeLastOrNull() },
            onDismiss = dropUnlessResumed(block = backStack::removeLastOrNull),
        )
    }
}
