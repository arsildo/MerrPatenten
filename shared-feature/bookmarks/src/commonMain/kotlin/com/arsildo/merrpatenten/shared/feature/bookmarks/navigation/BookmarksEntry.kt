package com.arsildo.merrpatenten.shared.feature.bookmarks.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.arsildo.merrpatenten.shared.feature.bookmarks.ui.BookmarksPracticeRoute
import com.arsildo.merrpatenten.shared.feature.bookmarks.ui.BookmarksRoute
import com.arsildo.merrpatenten.shared.feature.bookmarks.ui.components.BookmarksPracticeResultBottomSheetRoute
import com.arsildo.merrpatenten.shared.feature.bookmarks.ui.components.ClearBookmarksBottomSheetRoute
import kotlinx.serialization.Serializable
import navigation.BottomSheetSceneStrategy
import navigation.SharedViewModelStoreNavEntryDecorator
import navigation.toContentKey

@Serializable
object Bookmarks : NavKey

@Serializable
object ClearBookmarksBottomSheet : NavKey

@Serializable
data class BookmarksPractice(val category: String = "B") : NavKey

@Serializable
data class BookmarksPracticeResultBottomSheet(val category: String = "B") : NavKey

@OptIn(ExperimentalMaterial3Api::class)
fun EntryProviderScope<NavKey>.bookmarksEntry(
    backStack: NavBackStack<NavKey>,
    onBackPress: () -> Unit = { backStack.removeLastOrNull() },
    onImageDetailsClick: (index: Int) -> Unit = {},
) {
    entry<Bookmarks>(
        clazzContentKey = { key -> key.toContentKey() },
    ) {
        BookmarksRoute(
            onBackPress = onBackPress,
            onStartPractice = { category -> backStack.add(BookmarksPractice(category)) },
            onImageDetailsClick = onImageDetailsClick,
            onClearCategoryClick = dropUnlessResumed { backStack.add(ClearBookmarksBottomSheet) },
        )
    }

    entry<ClearBookmarksBottomSheet>(
        clazzContentKey = { key -> key.toContentKey() },
        metadata = SharedViewModelStoreNavEntryDecorator.parent(
            contentKey = Bookmarks.toContentKey(),
        ) + BottomSheetSceneStrategy.bottomSheet(skipPartiallyExpanded = true),
    ) {
        ClearBookmarksBottomSheetRoute(
            onConfirm = { backStack.removeLastOrNull() },
            onDismiss = { backStack.removeLastOrNull() },
        )
    }

    entry<BookmarksPractice>(
        clazzContentKey = { key -> key.toContentKey() },
    ) { key ->
        BookmarksPracticeRoute(
            category = key.category,
            onBackPress = onBackPress,
            onPracticeFinished = dropUnlessResumed { backStack.add(BookmarksPracticeResultBottomSheet(key.category)) },
            onImageDetailsClick = onImageDetailsClick,
        )
    }

    entry<BookmarksPracticeResultBottomSheet>(
        clazzContentKey = { key -> key.toContentKey() },
        metadata = { key ->
            SharedViewModelStoreNavEntryDecorator.parent(
                contentKey = BookmarksPractice(category = key.category).toContentKey(),
            ) + BottomSheetSceneStrategy.bottomSheet(skipPartiallyExpanded = true)
        },
    ) { key ->
        BookmarksPracticeResultBottomSheetRoute(
            category = key.category,
            onConfirm = {
                backStack.removeLastOrNull()
                onBackPress()
            },
            onDismiss = {
                backStack.removeLastOrNull()
                onBackPress()
            },
        )
    }
}
