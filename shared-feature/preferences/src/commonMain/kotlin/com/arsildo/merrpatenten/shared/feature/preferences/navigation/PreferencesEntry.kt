package com.arsildo.merrpatenten.shared.feature.preferences.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.arsildo.merrpatenten.shared.feature.preferences.PreferencesRoute
import kotlinx.serialization.Serializable

@Serializable
object Preferences : NavKey

fun EntryProviderScope<NavKey>.preferencesEntry(
    backStack: NavBackStack<NavKey>,
    onBackPress: () -> Unit = { backStack.removeLastOrNull() }
) {
    entry<Preferences> {
        PreferencesRoute(
            onBackPress = onBackPress
        )
    }
}

