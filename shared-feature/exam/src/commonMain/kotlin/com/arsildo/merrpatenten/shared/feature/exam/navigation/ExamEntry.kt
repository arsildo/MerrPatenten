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
data class Exam(val category: String = "B") : NavKey

@Serializable
data class ExamResultBottomSheet(val category: String = "B") : NavKey

@OptIn(ExperimentalMaterial3Api::class)
fun EntryProviderScope<NavKey>.examEntry(
    backStack: NavBackStack<NavKey>,
    onImageDetailsClick: (Int) -> Unit,
    onExitExam: () -> Unit,
    onRestartExam: (String) -> Unit,
) {
    entry<Exam>(
        clazzContentKey = { key -> key.toContentKey() }
    ) { examKey ->
        ExamRoute(
            category = examKey.category,
            onImageDetailsClick = onImageDetailsClick,
            onOpenMap = { backStack.add(ExamResultBottomSheet(examKey.category)) },
            onExitExam = onExitExam,
            onRestartExam = { onRestartExam(examKey.category) }
        )
    }

    entry<ExamResultBottomSheet>(
        clazzContentKey = { key -> key.toContentKey() },
        metadata = { key ->
            SharedViewModelStoreNavEntryDecorator.parent(
                contentKey = Exam(key.category).toContentKey()
            ) + BottomSheetSceneStrategy.bottomSheet(skipPartiallyExpanded = true)
        }
    ) { sheetKey ->
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
                onRestartExam(sheetKey.category)
            },
            onDismiss = dropUnlessResumed(block = backStack::removeLastOrNull)
        )
    }
}

