package com.arsildo.merrpatenten.shared.feature.exam.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.ERRORS_ALLOWED
import com.arsildo.merrpatenten.shared.core.designsystem.QUESTIONS_IN_EXAM
import com.arsildo.merrpatenten.shared.core.designsystem.components.ExitExamButton
import com.arsildo.merrpatenten.shared.core.designsystem.components.RestartExamButton
import com.arsildo.merrpatenten.shared.core.designsystem.icons.CircleCheck
import com.arsildo.merrpatenten.shared.core.designsystem.icons.CircleX
import com.arsildo.merrpatenten.shared.core.designsystem.icons.LogOut
import com.arsildo.merrpatenten.shared.core.designsystem.icons.RotateCcw
import com.arsildo.merrpatenten.shared.core.designsystem.semanticColors
import com.arsildo.merrpatenten.shared.feature.exam.ExamUiState
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ExamQuestionMap(
    modifier: Modifier = Modifier,
    uiState: ExamUiState,
    timerProvider: () -> String = { "" },
    currentPageProvider: (() -> Int)? = null,
    onQuestionClick: (page: Int) -> Unit,
    onExitExamClick: (() -> Unit)? = null,
    onRestartExamClick: (() -> Unit)? = null,
    minItemSize: Dp = 48.dp,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val hasPassed = uiState.errors <= ERRORS_ALLOWED
    val heroContent = if (hasPassed) MaterialTheme.semanticColors.onSuccessContainer else MaterialTheme.colorScheme.onErrorContainer
    val heroIcon = if (hasPassed) CircleCheck else CircleX
    val heroIconColor = if (hasPassed) MaterialTheme.semanticColors.success else MaterialTheme.colorScheme.error

    val completedCount = remember(uiState.responseList) {
        uiState.responseList.count { it.isNotBlank() }
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = minItemSize),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxSize(),
    ) {
        // Status or Overview Header
        item(
            key = "overview_header",
            span = { GridItemSpan(maxLineSpan) },
            contentType = if (uiState.isCompleted) "completed_header" else "progress_header",
        ) {
            if (uiState.isCompleted) {
                Card(
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = if (hasPassed) {
                            MaterialTheme.semanticColors.successContainer
                        } else {
                            MaterialTheme.colorScheme.errorContainer
                        },
                    ),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = heroIcon,
                            contentDescription = null,
                            tint = heroIconColor,
                            modifier = Modifier.size(32.dp),
                        )
                        Column {
                            val errorLabel = stringResource(
                                if (uiState.errors == 1) Res.string.error_singular else Res.string.errors_plural,
                            )
                            Text(
                                text = "${uiState.errors} $errorLabel",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = heroIconColor,
                            )
                            Text(
                                text = stringResource(if (hasPassed) Res.string.passed else Res.string.failed),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = heroContent,
                            )
                        }
                    }
                }
            } else {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column {
                            Text(
                                text = stringResource(Res.string.completed_question),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(
                                text = "$completedCount / ${uiState.questionCount}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                        QuestionMapTimerBadge(timer = timerProvider)
                    }
                }
            }
        }

        // Question Grid Items
        items(
            count = uiState.questionCount,
            key = { page -> page },
            contentType = { "question_cell" },
        ) { page ->
            val hasResponse = page < uiState.responseList.size && uiState.responseList[page].isNotEmpty()
            val isCorrect = page < uiState.mistakePositions.size && uiState.mistakePositions[page] == 0
            val isCurrent by remember(currentPageProvider, page) {
                derivedStateOf { currentPageProvider != null && currentPageProvider() == page }
            }

            val container = if (uiState.isCompleted) {
                if (isCorrect) MaterialTheme.semanticColors.successContainer else MaterialTheme.colorScheme.errorContainer
            } else if (hasResponse) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceContainerHigh
            }

            val content = if (uiState.isCompleted) {
                if (isCorrect) MaterialTheme.semanticColors.onSuccessContainer else MaterialTheme.colorScheme.onErrorContainer
            } else if (hasResponse) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }

            val shape = if (hasResponse) MaterialTheme.shapes.medium else MaterialTheme.shapes.small

            val activeBorder = if (isCurrent) {
                Modifier.border(
                    width = 2.5.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = shape,
                )
            } else {
                Modifier
            }

            QuestionResultGridItem(
                title = page,
                containerColor = container,
                contentColor = content,
                shape = shape,
                contentDescription = stringResource(Res.string.question_number_format, page + 1),
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentTick)
                    onQuestionClick(page)
                },
                modifier = activeBorder,
            )
        }

        // Action Buttons if available and completed
        if (uiState.isCompleted && (onRestartExamClick != null || onExitExamClick != null)) {
            item(
                key = "exam_actions",
                span = { GridItemSpan(maxLineSpan) },
                contentType = "exam_actions",
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    if (onRestartExamClick != null) {
                        RestartExamButton(
                            icon = RotateCcw,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = onRestartExamClick,
                        )
                    }
                    if (onExitExamClick != null) {
                        ExitExamButton(
                            icon = LogOut,
                            onClick = onExitExamClick,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuestionMapTimerBadge(timer: () -> String) {
    val timerText = timer()
    if (timerText.isNotEmpty()) {
        Badge(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ) {
            Text(
                text = timerText,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            )
        }
    }
}
