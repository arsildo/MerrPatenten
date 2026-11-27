package com.arsildo.merrpatenten.shared.feature.exam.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
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
    onExitExamClick: () -> Unit,
    onRestartExamClick: (category: String, type: ExamType) -> Unit,
) {
    entry<Exam>(
        clazzContentKey = { key -> key.toContentKey() },
    ) { examKey ->
        ExamRoute(
            category = examKey.category,
            type = examKey.type,
            onImageDetailsClick = onImageDetailsClick,
            onOpenMapClick = { backStack.add(ExamResultBottomSheet(category = examKey.category, type = examKey.type)) },
            onOpenExitExamClick = { backStack.add(ExitExamBottomSheet(category = examKey.category, type = examKey.type)) },
            onExitExamClick = onExitExamClick,
            onRestartExamClick = { onRestartExamClick(examKey.category, examKey.type) },
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
            onQuestionClick = {
                backStack.removeLastOrNull()
            },
            onExitExamClick = {
                backStack.removeLastOrNull()
                onExitExamClick()
            },
            onRestartExamClick = {
                backStack.removeLastOrNull()
                onRestartExamClick(sheetKey.category, sheetKey.type)
            },
            onDismiss = { backStack.removeLastOrNull() },
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
            onReviewUnansweredClick = {
                backStack.removeLastOrNull()
                backStack.add(ExamResultBottomSheet(category = sheetKey.category, type = sheetKey.type))
            },
            onFinishAnywayClick = {
                backStack.removeLastOrNull()
                backStack.add(ExamResultBottomSheet(category = sheetKey.category, type = sheetKey.type))
            },
            onExitExamClick = {
                backStack.removeLastOrNull()
                onExitExamClick()
            },
            onDismiss = { backStack.removeLastOrNull() },
        )
    }
}
