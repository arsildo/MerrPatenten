package com.arsildo.merrpatenten.shared.feature.preferences.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.arsildo.merrpatenten.shared.feature.preferences.ui.PreferencesRoute
import com.arsildo.merrpatenten.shared.feature.preferences.ui.components.LanguageSelectionBottomSheet
import kotlinx.serialization.Serializable
import navigation.BottomSheetSceneStrategy

@Serializable
object Preferences : NavKey

@Serializable
object LanguageSelection : NavKey

@OptIn(ExperimentalMaterial3Api::class)
fun EntryProviderScope<NavKey>.preferencesEntry(backStack: NavBackStack<NavKey>) {
    entry<Preferences> {
        PreferencesRoute(
            onLanguageClick = { backStack.add(LanguageSelection) },
        )
    }

    entry<LanguageSelection>(
        metadata = BottomSheetSceneStrategy.bottomSheet(skipPartiallyExpanded = true),
    ) {
        LanguageSelectionBottomSheet()
    }
}
