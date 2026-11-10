package com.arsildo.merrpatenten.shared.feature.imagedetails.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.arsildo.merrpatenten.shared.feature.imagedetails.ui.ZoomableExamImageRoute
import kotlinx.serialization.Serializable
import navigation.BottomSheetSceneStrategy

@Serializable
data class ZoomableImage(val imageId: Int) : NavKey

@OptIn(ExperimentalMaterial3Api::class)
fun EntryProviderScope<NavKey>.imageDetailsEntry(
    backStack: NavBackStack<NavKey>,
    onDismiss: () -> Unit = { backStack.removeLastOrNull() },
) {
    entry<ZoomableImage>(
        metadata = BottomSheetSceneStrategy.bottomSheet(skipPartiallyExpanded = true),
    ) { key ->
        ZoomableExamImageRoute(
            imageId = key.imageId,
            onDismiss = onDismiss,
        )
    }
}
