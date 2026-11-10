package com.arsildo.merrpatenten.shared.feature.exam.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.arsildo.merrpatenten.shared.feature.exam.ui.ExamResultBottomSheetRoute
import com.arsildo.merrpatenten.shared.feature.exam.ui.ExamRoute
import com.arsildo.merrpatenten.shared.feature.exam.ui.ExitExamBottomSheetRoute
import kotlinx.serialization.Serializable
import navigation.BottomSheetSceneStrategy
import navigation.SharedViewModelStoreNavEntryDecorator
import navigation.toContentKey

@Serializable
enum class ExamType {
    STANDARD,
    BLITZ,
}

@Serializable
data class Exam(val category: String = "B", val type: ExamType = ExamType.STANDARD) : NavKey

@Serializable
data class ExamResultBottomSheet(val category: String = "B", val type: ExamType = ExamType.STANDARD) : NavKey

@Serializable
data class ExitExamBottomSheet(val category: String = "B", val type: ExamType = ExamType.STANDARD) : NavKey

@OptIn(ExperimentalMaterial3Api::class)
fun EntryProviderScope<NavKey>.examEntry(
    backStack: NavBackStack<NavKey>,
    onImageDetailsClick: (index: Int) -> Unit,
    onExitExam: () -> Unit,
    onRestartExam: (category: String, type: ExamType) -> Unit,
) {
    entry<Exam>(
        clazzContentKey = { key -> key.toContentKey() },
    ) { examKey ->
        ExamRoute(
            category = examKey.category,
            type = examKey.type,
            onImageDetailsClick = onImageDetailsClick,
            onOpenMap = { backStack.add(ExamResultBottomSheet(category = examKey.category, type = examKey.type)) },
            onOpenExitExam = { backStack.add(ExitExamBottomSheet(category = examKey.category, type = examKey.type)) },
            onExitExam = onExitExam,
            onRestartExam = { onRestartExam(examKey.category, examKey.type) },
        )
    }

    entry<ExamResultBottomSheet>(
        clazzContentKey = { key -> key.toContentKey() },
        metadata = { key ->
            SharedViewModelStoreNavEntryDecorator.parent(
                contentKey = Exam(category = key.category, type = key.type).toContentKey(),
            ) + BottomSheetSceneStrategy.bottomSheet(skipPartiallyExpanded = true)
        },
    ) { sheetKey ->
        ExamResultBottomSheetRoute(
            category = sheetKey.category,
            type = sheetKey.type,
            onQuestionClicked = {
                backStack.removeLastOrNull()
            },
            onExitExam = {
                backStack.removeLastOrNull()
                onExitExam()
            },
            onRestartExam = {
                backStack.removeLastOrNull()
                onRestartExam(sheetKey.category, sheetKey.type)
            },
            onDismiss = dropUnlessResumed(block = backStack::removeLastOrNull),
        )
    }

    entry<ExitExamBottomSheet>(
        clazzContentKey = { key -> key.toContentKey() },
        metadata = { key ->
            SharedViewModelStoreNavEntryDecorator.parent(
                contentKey = Exam(category = key.category, type = key.type).toContentKey(),
            ) + BottomSheetSceneStrategy.bottomSheet(skipPartiallyExpanded = true)
        },
    ) { sheetKey ->
        ExitExamBottomSheetRoute(
            category = sheetKey.category,
            type = sheetKey.type,
            onReviewUnanswered = {
                backStack.removeLastOrNull()
                backStack.add(ExamResultBottomSheet(category = sheetKey.category, type = sheetKey.type))
            },
            onFinishAnyway = {
                backStack.removeLastOrNull()
                backStack.add(ExamResultBottomSheet(category = sheetKey.category, type = sheetKey.type))
            },
            onExitExam = {
                backStack.removeLastOrNull()
                onExitExam()
            },
            onDismiss = dropUnlessResumed(block = backStack::removeLastOrNull),
        )
    }
}
