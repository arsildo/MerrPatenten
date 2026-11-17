package com.arsildo.merrpatenten.shared.feature.mistakes.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.arsildo.merrpatenten.shared.feature.mistakes.ui.MistakesPracticeRoute
import com.arsildo.merrpatenten.shared.feature.mistakes.ui.MistakesRoute
import com.arsildo.merrpatenten.shared.feature.mistakes.ui.components.ClearMistakesBottomSheetRoute
import com.arsildo.merrpatenten.shared.feature.mistakes.ui.components.MistakesPracticeResultBottomSheetRoute
import kotlinx.serialization.Serializable
import navigation.BottomSheetSceneStrategy
import navigation.SharedViewModelStoreNavEntryDecorator
import navigation.toContentKey

@Serializable
object Mistakes : NavKey

@Serializable
object ClearMistakesBottomSheet : NavKey

@Serializable
data class MistakesPractice(val category: String = "B") : NavKey

@Serializable
data class MistakesPracticeResultBottomSheet(val category: String = "B") : NavKey

@OptIn(ExperimentalMaterial3Api::class)
fun EntryProviderScope<NavKey>.mistakesEntry(
    backStack: NavBackStack<NavKey>,
    onBackPress: () -> Unit = { backStack.removeLastOrNull() },
    onImageDetailsClick: (index: Int) -> Unit = {},
) {
    entry<Mistakes>(
        clazzContentKey = { key -> key.toContentKey() },
    ) {
        MistakesRoute(
            onStartPractice = { category -> backStack.add(MistakesPractice(category)) },
            onImageDetailsClick = onImageDetailsClick,
            onClearCategoryClick = { backStack.add(ClearMistakesBottomSheet) },
        )
    }

    entry<ClearMistakesBottomSheet>(
        clazzContentKey = { key -> key.toContentKey() },
        metadata = SharedViewModelStoreNavEntryDecorator.parent(
            contentKey = Mistakes.toContentKey(),
        ) + BottomSheetSceneStrategy.bottomSheet(skipPartiallyExpanded = true),
    ) {
        ClearMistakesBottomSheetRoute(
            onConfirm = { backStack.removeLastOrNull() },
            onDismiss = dropUnlessResumed(block = backStack::removeLastOrNull),
        )
    }

    entry<MistakesPractice>(
        clazzContentKey = { key -> key.toContentKey() },
    ) { key ->
        MistakesPracticeRoute(
            category = key.category,
            onBackPress = onBackPress,
            onPracticeFinished = { backStack.add(MistakesPracticeResultBottomSheet(key.category)) },
            onImageDetailsClick = onImageDetailsClick,
        )
    }

    entry<MistakesPracticeResultBottomSheet>(
        clazzContentKey = { key -> key.toContentKey() },
        metadata = { key ->
            SharedViewModelStoreNavEntryDecorator.parent(
                contentKey = MistakesPractice(category = key.category).toContentKey(),
            ) + BottomSheetSceneStrategy.bottomSheet(skipPartiallyExpanded = true)
        },
    ) { key ->
        MistakesPracticeResultBottomSheetRoute(
            category = key.category,
            onConfirm = {
                backStack.removeLastOrNull()
                onBackPress()
            },
            onDismiss = dropUnlessResumed {
                backStack.removeLastOrNull()
                onBackPress()
            },
        )
    }
}
