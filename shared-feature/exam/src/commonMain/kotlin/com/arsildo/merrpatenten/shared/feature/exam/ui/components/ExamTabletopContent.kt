package com.arsildo.merrpatenten.shared.feature.exam.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.components.EndExamButton
import com.arsildo.merrpatenten.shared.core.designsystem.components.ExitExamButton
import com.arsildo.merrpatenten.shared.core.designsystem.components.RestartExamButton
import com.arsildo.merrpatenten.shared.core.designsystem.formatQuestion
import com.arsildo.merrpatenten.shared.core.designsystem.getImageResource
import com.arsildo.merrpatenten.shared.core.designsystem.semanticColors
import com.arsildo.merrpatenten.shared.feature.exam.ExamUiState
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun ExamTabletopContent(
    uiState: ExamUiState,
    pagerState: PagerState,
    animatedProgress: Float,
    endExamVisible: Boolean,
    onShowEndExam: () -> Unit,
    onOpenMap: () -> Unit,
    onImageDetailsClick: (index: Int) -> Unit,
    onCheckFalseAtPage: (page: Int) -> Unit,
    onCheckTrueAtPage: (page: Int) -> Unit,
    onCompleteExam: () -> Unit,
    onRestartExam: () -> Unit,
    onExitExam: () -> Unit,
    onPreviousPageClick: () -> Unit,
    onNextPageClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val page = pagerState.currentPage
    val falseChecked = page < uiState.falseCheckedPositions.size && uiState.falseCheckedPositions[page]
    val trueChecked = page < uiState.trueCheckedPositions.size && uiState.trueCheckedPositions[page]
    val isCorrect = page < uiState.mistakePositions.size && uiState.mistakePositions[page] == 0

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        // Upper Display Half: Upright Screen (Timer, Progress, Question Image & Text)
        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                ExamLegend(
                    pagerState = pagerState,
                    timer = { uiState.timer },
                    endExamVisible = endExamVisible,
                    onMapClick = onOpenMap,
                    onShowEndExamButton = onShowEndExam,
                )

                LinearProgressIndicator(
                    progress = { animatedProgress },
                    color = MaterialTheme.semanticColors.successContainer,
                    drawStopIndicator = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp),
                )

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                ) { pageIndex ->
                    val question = uiState.questions.getOrNull(pageIndex)
                    if (question != null) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            QuestionImage(
                                image = getImageResource(question.image),
                                onClick = { onImageDetailsClick(question.image) },
                                modifier = Modifier.heightIn(max = 180.dp),
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = formatQuestion(question.question),
                                style = uiState.questionTextSize.textStyle,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }
                }
            }
        }

        HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.outlineVariant)

        // Lower Base Half: Flat Touch Control Pad (True/False + Navigation)
        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceContainerLow,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                // True / False Response Cards
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ExpressiveOptionCard(
                            title = stringResource(Res.string.false_checkbox),
                            iconSelected = Icons.Rounded.Cancel,
                            iconUnselected = Icons.Outlined.Cancel,
                            isChecked = falseChecked,
                            enabled = !uiState.isCompleted,
                            selectedContainerColor = MaterialTheme.colorScheme.errorContainer,
                            selectedContentColor = MaterialTheme.colorScheme.onErrorContainer,
                            selectedBorderColor = MaterialTheme.colorScheme.error,
                            onClick = { onCheckFalseAtPage(page) },
                            modifier = Modifier.weight(1f),
                        )

                        ExpressiveOptionCard(
                            title = stringResource(Res.string.true_checkbox),
                            iconSelected = Icons.Rounded.CheckCircle,
                            iconUnselected = Icons.Outlined.CheckCircle,
                            isChecked = trueChecked,
                            enabled = !uiState.isCompleted,
                            selectedContainerColor = MaterialTheme.semanticColors.successContainer,
                            selectedContentColor = MaterialTheme.semanticColors.onSuccessContainer,
                            selectedBorderColor = MaterialTheme.semanticColors.success,
                            onClick = { onCheckTrueAtPage(page) },
                            modifier = Modifier.weight(1f),
                        )
                    }

                    if (uiState.isCompleted) {
                        AnswerFeedbackIndicator(correctAnswer = isCorrect)
                    }
                }

                // Controls: Previous / Next & Action Buttons
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    if (!uiState.immersiveMode) {
                        PagerNavigation(
                            onPreviousPageClick = onPreviousPageClick,
                            onNextPageClick = onNextPageClick,
                        )
                    }

                    if (endExamVisible || uiState.isCompleted) {
                        if (uiState.isCompleted) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                RestartExamButton(
                                    icon = Icons.Rounded.RestartAlt,
                                    modifier = Modifier.weight(1f),
                                    onClick = onRestartExam,
                                )
                                ExitExamButton(
                                    icon = Icons.AutoMirrored.Rounded.ExitToApp,
                                    modifier = Modifier.weight(1f),
                                    onClick = onExitExam,
                                )
                            }
                        } else {
                            EndExamButton(
                                icon = Icons.Rounded.DoneAll,
                                modifier = Modifier.fillMaxWidth(),
                                onClick = onCompleteExam,
                            )
                        }
                    }
                }
            }
        }
    }
}
