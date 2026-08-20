package com.arsildo.merrpatenten.shared.feature.imagedetails.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.arsildo.merrpatenten.shared.feature.imagedetails.ui.ZoomableExamImageRoute
import kotlinx.serialization.Serializable

@Serializable
data class ZoomableImage(val imageId: Int) : NavKey

fun EntryProviderScope<NavKey>.imageDetailsEntry(
    backStack: NavBackStack<NavKey>,
    onDismiss: () -> Unit = { backStack.removeLastOrNull() },
) {
    entry<ZoomableImage> { key ->
        ZoomableExamImageRoute(
            imageId = key.imageId,
            onDismiss = onDismiss,
        )
    }
}
