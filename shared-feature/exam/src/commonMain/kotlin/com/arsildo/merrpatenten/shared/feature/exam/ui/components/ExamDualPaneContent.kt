package com.arsildo.merrpatenten.shared.feature.exam.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.adaptive.rememberAppAdaptiveInfo
import com.arsildo.merrpatenten.shared.core.designsystem.components.EndExamButton
import com.arsildo.merrpatenten.shared.core.designsystem.components.ExitExamButton
import com.arsildo.merrpatenten.shared.core.designsystem.components.RestartExamButton
import com.arsildo.merrpatenten.shared.core.designsystem.semanticColors
import com.arsildo.merrpatenten.shared.feature.exam.ExamUiState
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun ExamDualPaneContent(
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
    onPageSelected: (page: Int) -> Unit,
    onPreviousPageClick: () -> Unit,
    onNextPageClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val adaptiveInfo = rememberAppAdaptiveInfo()
    val (primaryWeight, secondaryWeight) = adaptiveInfo.dualPaneSplitWeights(defaultPrimaryWeight = 0.58f)

    Row(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Left Workstation Pane: Question Pager & Controls (58% width or 50% in BOOK posture)
        Surface(
            modifier = Modifier
                .weight(primaryWeight)
                .fillMaxHeight(),
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 1.dp,
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                // Header (Timer + Progress)
                Column(modifier = Modifier.fillMaxWidth()) {
                    ExamLegend(
                        pagerState = pagerState,
                        timer = { uiState.timer },
                        endExamVisible = endExamVisible,
                        onMapClick = onOpenMap,
                        onShowEndExamButton = onShowEndExam,
                        showMapButton = false,
                    )

                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        color = MaterialTheme.semanticColors.successContainer,
                        drawStopIndicator = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                    )
                }

                // Question Pager Content
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                ) {
                    ExamPager(
                        questions = uiState.questions,
                        pagerState = pagerState,
                        falseCheckedPages = uiState.falseCheckedPositions,
                        trueCheckedPages = uiState.trueCheckedPositions,
                        onImageClick = onImageDetailsClick,
                        onCheckFalseAtPage = onCheckFalseAtPage,
                        onCheckTrueAtPage = onCheckTrueAtPage,
                        isCompleted = uiState.isCompleted,
                        responses = uiState.mistakePositions,
                        questionTextSize = uiState.questionTextSize,
                    )
                }

                // Bottom Controls for Question Navigation & Exam Termination
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
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

        // Right Workstation Pane: Interactive 40-Question Map (42% width or 50% in BOOK posture)
        Surface(
            modifier = Modifier
                .weight(secondaryWeight)
                .fillMaxHeight(),
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.surfaceContainerLow,
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(Res.string.question_map),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                )

                ExamQuestionMap(
                    uiState = uiState,
                    currentPage = pagerState.currentPage,
                    onQuestionClicked = onPageSelected,
                    onRestartExam = null,
                    onExitExam = null,
                    columns = 5,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
