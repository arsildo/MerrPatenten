package com.arsildo.merrpatenten.shared.feature.exam.ui

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.HighlightOff
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arsildo.merrpatenten.shared.core.designsystem.ERRORS_ALLOWED
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.QUESTIONS_IN_EXAM
import com.arsildo.merrpatenten.shared.core.designsystem.components.ExitExamButton
import com.arsildo.merrpatenten.shared.core.designsystem.components.RestartExamButton
import com.arsildo.merrpatenten.shared.core.designsystem.semanticColors
import com.arsildo.merrpatenten.shared.feature.exam.ExamUiState
import com.arsildo.merrpatenten.shared.feature.exam.ExamViewModel
import com.arsildo.merrpatenten.shared.feature.exam.ui.components.QuestionResultGridItem
import com.arsildo.merrpatenten.shared.feature.exam.ui.components.ResultStatusIndicator
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun ExamResultBottomSheetRoute(
    viewModel: ExamViewModel = koinViewModel(),
    onQuestionClicked: (Int) -> Unit,
    onExitExam: () -> Unit,
    onRestartExam: () -> Unit,
    onDismiss: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ExamResultBottomSheet(
        uiState = uiState,
        onQuestionClicked = { page ->
            viewModel.navigateToPage(page)
            onQuestionClicked(page)
        },
        onExitExam = onExitExam,
        onRestartExam = onRestartExam,
        onDismiss = onDismiss,
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun ExamResultBottomSheet(
    uiState: ExamUiState,
    onQuestionClicked: (Int) -> Unit,
    onExitExam: () -> Unit,
    onRestartExam: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val hasPassed = uiState.errors <= ERRORS_ALLOWED
    val heroContent = if (hasPassed) MaterialTheme.semanticColors.onSuccessContainer else MaterialTheme.colorScheme.onErrorContainer
    val heroIcon = if (hasPassed) Icons.Rounded.CheckCircle else Icons.Rounded.HighlightOff
    val heroIconColor = if (hasPassed) MaterialTheme.semanticColors.success else MaterialTheme.colorScheme.error

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .focusable()
            .onPreviewKeyEvent { event ->
                if (event.type != KeyEventType.KeyDown) return@onPreviewKeyEvent false
                when (event.key) {
                    Key.Escape, Key.Back, Key.Spacebar, Key.M -> {
                        onDismiss()
                        true
                    }

                    else -> false
                }
            },
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            contentPadding = PaddingValues(16.dp) + WindowInsets.navigationBars.asPaddingValues(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (uiState.isCompleted) {
                item(span = { GridItemSpan(5) }) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
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
                                    if (uiState.errors ==
                                        1
                                    ) {
                                        Res.string.error_singular
                                    } else {
                                        Res.string.errors_plural
                                    },
                                )
                                Text(
                                    text = "${uiState.errors} $errorLabel",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = heroIconColor,
                                )
                                Text(
                                    text = stringResource(if (hasPassed) Res.string.passed else Res.string.failed),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                )
                                Text(
                                    text = stringResource(
                                        if (hasPassed) Res.string.exam_passed_subtitle else Res.string.exam_failed_subtitle,
                                    ),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = heroContent.copy(alpha = 0.8f),
                                )
                            }
                        }
                    }
                }
            }
            items(count = QUESTIONS_IN_EXAM) { page ->
                val hasResponse = page < uiState.responseList.size && uiState.responseList[page].isNotEmpty()
                val isCorrect = page < uiState.mistakePositions.size && uiState.mistakePositions[page] == 0

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

                val statusText = stringResource(if (isCorrect) Res.string.true_checkbox else Res.string.false_checkbox)
                val questionTitle = stringResource(Res.string.question_number_format, page + 1)

                QuestionResultGridItem(
                    title = page,
                    containerColor = container,
                    contentColor = content,
                    shape = if (hasResponse) MaterialShapes.Cookie6Sided.toShape() else MaterialShapes.Slanted.toShape(),
                    contentDescription = stringResource(Res.string.question_status_format, questionTitle, statusText),
                    onClick = { onQuestionClicked(page) },
                )
            }
            item(
                span = { GridItemSpan(5) },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (!uiState.isCompleted) {
                        ResultStatusIndicator(
                            title = stringResource(Res.string.completed_question),
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    } else {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            ResultStatusIndicator(
                                title = stringResource(Res.string.false_checkbox),
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                            )
                            ResultStatusIndicator(
                                title = stringResource(Res.string.true_checkbox),
                                containerColor = MaterialTheme.semanticColors.successContainer,
                                contentColor = MaterialTheme.semanticColors.onSuccessContainer,
                            )
                        }
                    }
                    IconButton(
                        onClick = onDismiss,
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.secondary,
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowDown,
                            contentDescription = stringResource(Res.string.close),
                            modifier = Modifier.size(28.dp),
                        )
                    }
                }
            }
            if (uiState.isCompleted) {
                item(
                    span = { GridItemSpan(5) },
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        RestartExamButton(
                            icon = Icons.Rounded.RestartAlt,
                            onClick = onRestartExam,
                            modifier = Modifier.fillMaxWidth(),
                        )
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

@Preview
@Composable
private fun ExamResultBottomSheetInProgressPreview() {
    MerrPatentenTheme {
        ExamResultBottomSheet(
            uiState = ExamUiState(
                isCompleted = false,
                responseList = List(QUESTIONS_IN_EXAM) { if (it < 10) "Saktë" else "" },
                mistakePositions = List(QUESTIONS_IN_EXAM) { 1 },
            ),
            onQuestionClicked = {},
            onExitExam = {},
            onRestartExam = {},
            onDismiss = {},
        )
    }
}

@Preview
@Composable
private fun ExamResultBottomSheetPassedPreview() {
    MerrPatentenTheme {
        ExamResultBottomSheet(
            uiState = ExamUiState(
                isCompleted = true,
                errors = 2,
                responseList = List(QUESTIONS_IN_EXAM) { "Saktë" },
                mistakePositions = List(QUESTIONS_IN_EXAM) { if (it < 2) 1 else 0 },
            ),
            onQuestionClicked = {},
            onExitExam = {},
            onRestartExam = {},
            onDismiss = {},
        )
    }
}

@Preview
@Composable
private fun ExamResultBottomSheetFailedPreview() {
    MerrPatentenTheme {
        ExamResultBottomSheet(
            uiState = ExamUiState(
                isCompleted = true,
                errors = 8,
                responseList = List(QUESTIONS_IN_EXAM) { "Saktë" },
                mistakePositions = List(QUESTIONS_IN_EXAM) { if (it < 8) 1 else 0 },
            ),
            onQuestionClicked = {},
            onExitExam = {},
            onRestartExam = {},
            onDismiss = {},
        )
    }
}

@Preview
@Composable
private fun ExamResultBottomSheetDarkPreview() {
    MerrPatentenTheme(darkTheme = true) {
        ExamResultBottomSheet(
            uiState = ExamUiState(
                isCompleted = true,
                errors = 2,
                responseList = List(QUESTIONS_IN_EXAM) { "Saktë" },
                mistakePositions = List(QUESTIONS_IN_EXAM) { if (it < 2) 1 else 0 },
            ),
            onQuestionClicked = {},
            onExitExam = {},
            onRestartExam = {},
            onDismiss = {},
        )
    }
}
