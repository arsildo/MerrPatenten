package com.arsildo.merrpatenten.shared.feature.catalog.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.arsildo.merrpatenten.shared.feature.catalog.ui.CatalogRoute
import com.arsildo.merrpatenten.shared.feature.catalog.ui.SignDetailRoute
import kotlinx.serialization.Serializable

@Serializable
object Catalog : NavKey

@Serializable
data class SignDetail(val signId: Int) : NavKey

fun EntryProviderScope<NavKey>.catalogEntry(
    backStack: NavBackStack<NavKey>,
    onBackPress: () -> Unit = { backStack.removeLastOrNull() },
    onImageDetailsClick: (Int) -> Unit = {},
) {
    entry<Catalog> {
        CatalogRoute(
            onBackPress = onBackPress,
            onSignClick = { signId -> backStack.add(SignDetail(signId)) },
        )
    }

    entry<SignDetail> { key ->
        SignDetailRoute(
            signId = key.signId,
            onBackPress = onBackPress,
            onImageDetailsClick = onImageDetailsClick,
        )
    }
}
