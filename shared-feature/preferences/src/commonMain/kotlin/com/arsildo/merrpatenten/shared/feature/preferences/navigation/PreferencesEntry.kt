package com.arsildo.merrpatenten.shared.feature.preferences.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.arsildo.merrpatenten.shared.feature.preferences.ui.PreferencesRoute
import kotlinx.serialization.Serializable

@Serializable
object Preferences : NavKey

fun EntryProviderScope<NavKey>.preferencesEntry() {
    entry<Preferences> {
        PreferencesRoute()
    }
}
