package com.arsildo.merrpatenten.shared.feature.exam.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.ERRORS_ALLOWED
import com.arsildo.merrpatenten.shared.core.designsystem.QUESTIONS_IN_EXAM
import com.arsildo.merrpatenten.shared.core.designsystem.components.ExitExamButton
import com.arsildo.merrpatenten.shared.core.designsystem.components.RestartExamButton
import com.arsildo.merrpatenten.shared.core.designsystem.semanticColors
import com.arsildo.merrpatenten.shared.feature.exam.ExamUiState
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun ExamQuestionMap(
    uiState: ExamUiState,
    currentPage: Int? = null,
    onQuestionClicked: (page: Int) -> Unit,
    onExitExam: (() -> Unit)? = null,
    onRestartExam: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    columns: Int = 5,
) {
    val hasPassed = uiState.errors <= ERRORS_ALLOWED
    val heroContent = if (hasPassed) MaterialTheme.semanticColors.onSuccessContainer else MaterialTheme.colorScheme.onErrorContainer
    val heroIcon = if (hasPassed) Icons.Rounded.CheckCircle else Icons.Rounded.HighlightOff
    val heroIconColor = if (hasPassed) MaterialTheme.semanticColors.success else MaterialTheme.colorScheme.error

    val completedCount = remember(uiState.responseList) {
        uiState.responseList.count { it.isNotBlank() }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxSize(),
    ) {
        // Status or Overview Header
        item(span = { GridItemSpan(columns) }) {
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
                                text = "$completedCount / $QUESTIONS_IN_EXAM",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                        if (uiState.timer.isNotEmpty()) {
                            Badge(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            ) {
                                Text(
                                    text = uiState.timer,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                )
                            }
                        }
                    }
                }
            }
        }

        // 40 Question Grid Items
        items(count = QUESTIONS_IN_EXAM) { page ->
            val hasResponse = page < uiState.responseList.size && uiState.responseList[page].isNotEmpty()
            val isCorrect = page < uiState.mistakePositions.size && uiState.mistakePositions[page] == 0
            val isCurrent = currentPage != null && currentPage == page

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
                onClick = { onQuestionClicked(page) },
                modifier = activeBorder,
            )
        }

        // Action Buttons if available and completed
        if (uiState.isCompleted && (onRestartExam != null || onExitExam != null)) {
            item(span = { GridItemSpan(columns) }) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    if (onRestartExam != null) {
                        RestartExamButton(
                            icon = Icons.Rounded.RestartAlt,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = onRestartExam,
                        )
                    }
                    if (onExitExam != null) {
                        ExitExamButton(
                            icon = Icons.AutoMirrored.Rounded.ExitToApp,
                            onClick = onExitExam,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}
