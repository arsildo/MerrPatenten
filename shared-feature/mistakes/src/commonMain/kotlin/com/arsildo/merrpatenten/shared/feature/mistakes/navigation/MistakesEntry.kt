package com.arsildo.merrpatenten.shared.feature.mistakes.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.arsildo.merrpatenten.shared.feature.mistakes.ui.MistakesPracticeRoute
import com.arsildo.merrpatenten.shared.feature.mistakes.ui.MistakesRoute
import kotlinx.serialization.Serializable

@Serializable
object Mistakes : NavKey

@Serializable
data class MistakesPractice(val category: String = "B") : NavKey

fun EntryProviderScope<NavKey>.mistakesEntry(
    backStack: NavBackStack<NavKey>,
    onBackPress: () -> Unit = { backStack.removeLastOrNull() },
    onImageDetailsClick: (index: Int) -> Unit = {},
) {
    entry<Mistakes> {
        MistakesRoute(
            onStartPractice = { category -> backStack.add(MistakesPractice(category)) },
            onImageDetailsClick = onImageDetailsClick,
        )
    }

    entry<MistakesPractice> { key ->
        MistakesPracticeRoute(
            category = key.category,
            onBackPress = onBackPress,
            onImageDetailsClick = onImageDetailsClick,
        )
    }
}
