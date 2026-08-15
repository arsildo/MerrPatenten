package com.arsildo.merrpatenten.shared.feature.exam.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.arsildo.merrpatenten.shared.feature.exam.ExamResultBottomSheetRoute
import com.arsildo.merrpatenten.shared.feature.exam.ExamRoute
import kotlinx.serialization.Serializable
import navigation.BottomSheetSceneStrategy
import navigation.SharedViewModelStoreNavEntryDecorator
import navigation.toContentKey

@Serializable
object Exam : NavKey

@Serializable
object ExamResultBottomSheet : NavKey

@OptIn(ExperimentalMaterial3Api::class)
fun EntryProviderScope<NavKey>.examEntry(
    backStack: NavBackStack<NavKey>,
    onImageDetailsClick: (Int) -> Unit,
    onExitExam: () -> Unit,
    onRestartExam: () -> Unit,
) {
    entry<Exam>(
        clazzContentKey = { key -> key.toContentKey() }
    ) {
        ExamRoute(
            onImageDetailsClick = onImageDetailsClick,
            onOpenMap = { backStack.add(ExamResultBottomSheet) },
            onExitExam = onExitExam,
            onRestartExam = onRestartExam
        )
    }

    entry<ExamResultBottomSheet>(
        metadata = SharedViewModelStoreNavEntryDecorator.parent(
            contentKey = Exam.toContentKey()
        ) + BottomSheetSceneStrategy.bottomSheet(skipPartiallyExpanded = true)
    ) {
        ExamResultBottomSheetRoute(
            onQuestionClicked = {
                backStack.removeLastOrNull()
            },
            onExitExam = {
                backStack.removeLastOrNull()
                onExitExam()
            },
            onRestartExam = {
                backStack.removeLastOrNull()
                onRestartExam()
            },
            onDismiss = dropUnlessResumed(block = backStack::removeLastOrNull)
        )
    }
}

