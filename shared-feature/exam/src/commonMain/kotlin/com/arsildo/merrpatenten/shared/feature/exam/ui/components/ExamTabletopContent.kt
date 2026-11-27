package com.arsildo.merrpatenten.shared.feature.exam.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.components.EndExamButton
import com.arsildo.merrpatenten.shared.core.designsystem.components.ExitExamButton
import com.arsildo.merrpatenten.shared.core.designsystem.components.RestartExamButton
import com.arsildo.merrpatenten.shared.core.designsystem.formatQuestion
import com.arsildo.merrpatenten.shared.core.designsystem.icons.CheckCheck
import com.arsildo.merrpatenten.shared.core.designsystem.icons.CircleCheck
import com.arsildo.merrpatenten.shared.core.designsystem.icons.CircleX
import com.arsildo.merrpatenten.shared.core.designsystem.icons.LogOut
import com.arsildo.merrpatenten.shared.core.designsystem.icons.RotateCcw
import com.arsildo.merrpatenten.shared.core.designsystem.semanticColors
import com.arsildo.merrpatenten.shared.feature.exam.ExamUiState
import merrpatenten.shared_core.design_system.generated.resources.Res
import merrpatenten.shared_core.design_system.generated.resources.false_checkbox
import merrpatenten.shared_core.design_system.generated.resources.true_checkbox
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun ExamTabletopContent(
    uiState: ExamUiState,
    timerProvider: () -> String,
    pagerState: PagerState,
    progressProvider: () -> Float,
    endExamVisible: Boolean,
    onShowEndExam: () -> Unit,
    onOpenMap: () -> Unit,
    onImageDetailsClick: (index: Int) -> Unit,
    onToggleBookmark: (questionId: Int) -> Unit,
    onCheckFalseAtPage: (page: Int) -> Unit,
    onCheckTrueAtPage: (page: Int) -> Unit,
    onCompleteExam: () -> Unit,
    onRestartExam: () -> Unit,
    onExitExam: () -> Unit,
    onPreviousPageClick: () -> Unit,
    onNextPageClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
                    timer = timerProvider,
                    endExamVisible = endExamVisible,
                    onMapClick = onOpenMap,
                    onShowEndExamButton = onShowEndExam,
                )

                LinearProgressIndicator(
                    progress = progressProvider,
                    color = MaterialTheme.semanticColors.successContainer,
                    drawStopIndicator = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp),
                )

                HorizontalPager(
                    state = pagerState,
                    key = { index -> uiState.questions.getOrNull(index)?.id ?: index },
                    modifier = Modifier.fillMaxSize(),
                ) { pageIndex ->
                    val question = uiState.questions.getOrNull(pageIndex)
                    if (question != null) {
                        val isBookmarked = question.id in uiState.bookmarkedQuestionIds
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                QuestionImage(
                                    imageId = question.image,
                                    onClick = { onImageDetailsClick(question.image) },
                                    modifier = Modifier
                                        .heightIn(max = 180.dp)
                                        .align(Alignment.Center),
                                )
                                BookmarkToggleButton(
                                    isBookmarked = isBookmarked,
                                    onToggle = { onToggleBookmark(question.id) },
                                    modifier = Modifier.align(Alignment.TopEnd),
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            val formattedQuestion = remember(question.question) {
                                formatQuestion(question.question)
                            }
                            Text(
                                text = formattedQuestion,
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
                TabletopResponseControls(
                    pagerState = pagerState,
                    falseCheckedPositions = uiState.falseCheckedPositions,
                    trueCheckedPositions = uiState.trueCheckedPositions,
                    mistakePositions = uiState.mistakePositions,
                    isCompleted = uiState.isCompleted,
                    onCheckFalseAtPage = onCheckFalseAtPage,
                    onCheckTrueAtPage = onCheckTrueAtPage,
                )

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

                    AnimatedVisibility(endExamVisible || uiState.isCompleted) {
                        if (uiState.isCompleted) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                RestartExamButton(
                                    icon = RotateCcw,
                                    modifier = Modifier.weight(1f),
                                    onClick = onRestartExam,
                                )
                                ExitExamButton(
                                    icon = LogOut,
                                    modifier = Modifier.weight(1f),
                                    onClick = onExitExam,
                                )
                            }
                        } else {
                            EndExamButton(
                                icon = CheckCheck,
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

@Composable
private fun TabletopResponseControls(
    pagerState: PagerState,
    falseCheckedPositions: List<Boolean>,
    trueCheckedPositions: List<Boolean>,
    mistakePositions: List<Int>,
    isCompleted: Boolean,
    onCheckFalseAtPage: (page: Int) -> Unit,
    onCheckTrueAtPage: (page: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val page = pagerState.currentPage
    val falseChecked = page < falseCheckedPositions.size && falseCheckedPositions[page]
    val trueChecked = page < trueCheckedPositions.size && trueCheckedPositions[page]
    val isCorrect = page < mistakePositions.size && mistakePositions[page] == 0

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ExpressiveOptionCard(
                title = stringResource(Res.string.false_checkbox),
                iconSelected = CircleX,
                iconUnselected = CircleX,
                isChecked = falseChecked,
                enabled = !isCompleted,
                selectedContainerColor = MaterialTheme.colorScheme.errorContainer,
                selectedContentColor = MaterialTheme.colorScheme.onErrorContainer,
                selectedBorderColor = MaterialTheme.colorScheme.error,
                onClick = { onCheckFalseAtPage(page) },
                modifier = Modifier.weight(1f),
            )

            ExpressiveOptionCard(
                title = stringResource(Res.string.true_checkbox),
                iconSelected = CircleCheck,
                iconUnselected = CircleCheck,
                isChecked = trueChecked,
                enabled = !isCompleted,
                selectedContainerColor = MaterialTheme.semanticColors.successContainer,
                selectedContentColor = MaterialTheme.semanticColors.onSuccessContainer,
                selectedBorderColor = MaterialTheme.semanticColors.success,
                onClick = { onCheckTrueAtPage(page) },
                modifier = Modifier.weight(1f),
            )
        }

        if (isCompleted) {
            AnswerFeedbackIndicator(correctAnswer = isCorrect)
        }
    }
}
