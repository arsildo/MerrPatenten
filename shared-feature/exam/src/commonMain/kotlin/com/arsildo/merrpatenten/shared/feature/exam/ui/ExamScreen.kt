package com.arsildo.merrpatenten.shared.feature.exam.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.QUESTIONS_IN_EXAM
import com.arsildo.merrpatenten.shared.core.designsystem.components.EndExamButton
import com.arsildo.merrpatenten.shared.core.designsystem.components.ExitExamButton
import com.arsildo.merrpatenten.shared.core.designsystem.components.RestartExamButton
import com.arsildo.merrpatenten.shared.core.designsystem.semanticColors
import com.arsildo.merrpatenten.shared.core.model.Question
import com.arsildo.merrpatenten.shared.feature.exam.ExamUiState
import com.arsildo.merrpatenten.shared.feature.exam.ExamViewModel
import com.arsildo.merrpatenten.shared.feature.exam.ui.components.ExamLegend
import com.arsildo.merrpatenten.shared.feature.exam.ui.components.ExamPager
import com.arsildo.merrpatenten.shared.feature.exam.ui.components.PagerNavigation
import com.arsildo.merrpatenten.shared.feature.exam.ui.components.UncompletedExamDialog
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun ExamRoute(
    category: String,
    viewModel: ExamViewModel = koinViewModel(parameters = { parametersOf(category) }),
    onImageDetailsClick: (Int) -> Unit,
    onOpenMap: () -> Unit,
    onExitExam: () -> Unit,
    onRestartExam: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val targetPage by viewModel.targetPage.collectAsStateWithLifecycle()

    ExamScreen(
        uiState = uiState,
        targetPage = targetPage,
        onPageNavigated = viewModel::onPageNavigated,
        onImageDetailsClick = onImageDetailsClick,
        onOpenMap = onOpenMap,
        onExitExam = onExitExam,
        onRestartExam = onRestartExam,
        onCheckTrueAtPage = viewModel::checkTrueAtPosition,
        onCheckFalseAtPage = viewModel::checkFalseAtPosition,
        onCompleteExam = viewModel::completeExam,
    )

    LaunchedEffect(uiState.isCompleted) {
        if (uiState.isCompleted) {
            onOpenMap()
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun ExamScreen(
    uiState: ExamUiState,
    targetPage: Int? = null,
    onPageNavigated: () -> Unit = {},
    onImageDetailsClick: (Int) -> Unit,
    onOpenMap: () -> Unit,
    onExitExam: () -> Unit,
    onRestartExam: () -> Unit,
    onCheckTrueAtPage: (Int) -> Unit,
    onCheckFalseAtPage: (Int) -> Unit,
    onCompleteExam: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { QUESTIONS_IN_EXAM })
    var questionsUnCompletedDialog by remember { mutableStateOf(false) }
    var endExamVisible by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(targetPage) {
        targetPage?.let { page ->
            pagerState.animateScrollToPage(page)
            onPageNavigated()
        }
    }

    Scaffold(
        modifier = modifier
            .focusRequester(focusRequester)
            .focusable()
            .onPreviewKeyEvent { event ->
                if (event.type != KeyEventType.KeyDown) return@onPreviewKeyEvent false
                when (event.key) {
                    Key.S, Key.One, Key.NumPad1 -> {
                        if (!uiState.isCompleted && pagerState.currentPage < uiState.questions.size) {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            onCheckTrueAtPage(pagerState.currentPage)
                            true
                        } else {
                            false
                        }
                    }

                    Key.G, Key.Two, Key.NumPad2 -> {
                        if (!uiState.isCompleted && pagerState.currentPage < uiState.questions.size) {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            onCheckFalseAtPage(pagerState.currentPage)
                            true
                        } else {
                            false
                        }
                    }

                    Key.DirectionLeft, Key.A, Key.PageUp -> {
                        if (pagerState.canScrollBackward) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(page = pagerState.currentPage - 1)
                            }
                            true
                        } else {
                            false
                        }
                    }

                    Key.DirectionRight, Key.D, Key.PageDown -> {
                        if (pagerState.canScrollForward) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(page = pagerState.currentPage + 1)
                            }
                            true
                        } else {
                            false
                        }
                    }

                    Key.Spacebar, Key.M -> {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onOpenMap()
                        true
                    }

                    Key.Z -> {
                        val currentQuestion = uiState.questions.getOrNull(pagerState.currentPage)
                        if (currentQuestion != null && currentQuestion.image > 0) {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onImageDetailsClick(currentQuestion.image)
                            true
                        } else {
                            false
                        }
                    }

                    Key.Enter, Key.NumPadEnter -> {
                        if (questionsUnCompletedDialog) {
                            questionsUnCompletedDialog = false
                            onCompleteExam()
                            onOpenMap()
                            true
                        } else if (!uiState.isCompleted) {
                            val completedCount = uiState.responseList.count { it.isNotBlank() }
                            if (completedCount != QUESTIONS_IN_EXAM) {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.GestureThresholdActivate)
                                questionsUnCompletedDialog = true
                            } else {
                                onCompleteExam()
                            }
                            true
                        } else if (endExamVisible) {
                            onRestartExam()
                            true
                        } else {
                            false
                        }
                    }

                    Key.Escape, Key.Back -> {
                        if (questionsUnCompletedDialog) {
                            questionsUnCompletedDialog = false
                            true
                        } else if (endExamVisible) {
                            endExamVisible = false
                            true
                        } else {
                            false
                        }
                    }

                    else -> false
                }
            },
        contentWindowInsets = WindowInsets(bottom = 0),
        contentColor = MaterialTheme.colorScheme.primary,
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
                    .padding(16.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                if (!uiState.immersiveMode) {
                    PagerNavigation(
                        onPreviousPageClick = {
                            if (pagerState.canScrollBackward) {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(page = pagerState.currentPage - 1)
                                }
                            }
                        },
                        onNextPageClick = {
                            if (pagerState.canScrollForward) {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(page = pagerState.currentPage + 1)
                                }
                            }
                        },
                    )
                }

                if (endExamVisible) {
                    if (uiState.isCompleted) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            RestartExamButton(
                                icon = Icons.Rounded.RestartAlt,
                                modifier = Modifier.fillMaxWidth(),
                                onClick = onRestartExam,
                            )
                            ExitExamButton(
                                icon = Icons.AutoMirrored.Rounded.ExitToApp,
                                onClick = onExitExam,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    } else {
                        EndExamButton(
                            icon = Icons.Rounded.DoneAll,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                val completedCount = uiState.responseList.count { it.isNotBlank() }
                                if (completedCount != QUESTIONS_IN_EXAM) {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.GestureThresholdActivate)
                                    questionsUnCompletedDialog = true
                                } else {
                                    onCompleteExam()
                                }
                            },
                        )
                    }
                }
            }
        },
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentAlignment = Alignment.Center,
        ) {
            if (uiState.questions.isEmpty()) {
                LoadingIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(),
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        ExamLegend(
                            pagerState = pagerState,
                            timer = { uiState.timer },
                            endExamVisible = endExamVisible,
                            onMapClick = onOpenMap,
                            onShowEndExamButton = { endExamVisible = !endExamVisible },
                        )

                        val completedCount = remember(uiState.responseList) {
                            uiState.responseList.count { it.isNotBlank() }
                        }
                        val progress = (completedCount.toFloat() / QUESTIONS_IN_EXAM.toFloat()).coerceIn(0f, 1f)
                        val animatedProgress by animateFloatAsState(
                            targetValue = progress,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioNoBouncy,
                                stiffness = Spring.StiffnessMediumLow,
                            ),
                            label = "ExamCompletionProgress",
                        )

                        LinearProgressIndicator(
                            progress = { animatedProgress },
                            color = MaterialTheme.semanticColors.successContainer,
                            drawStopIndicator = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        )
                    }

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

                    LaunchedEffect(pagerState.settledPage) {
                        endExamVisible = pagerState.settledPage == QUESTIONS_IN_EXAM - 1
                    }
                }
            }
        }
    }

    if (questionsUnCompletedDialog) {
        UncompletedExamDialog(
            onDismissRequest = { questionsUnCompletedDialog = false },
            onReviewUnanswered = {
                questionsUnCompletedDialog = false
                onOpenMap()
            },
            onFinishAnyway = {
                questionsUnCompletedDialog = false
                onCompleteExam()
                onOpenMap()
            },
            onExitExam = {
                questionsUnCompletedDialog = false
                onExitExam()
            },
        )
    }
}

@Preview
@Composable
private fun ExamScreenPreview() {
    MerrPatentenTheme {
        ExamScreen(
            uiState = ExamUiState(
                questions = listOf(
                    Question(
                        id = 1,
                        question = "Sinjali i paraqitur në figurë tregon një kthesë të rrezikshme majtas.",
                        image = 1,
                        answer = "Saktë",
                    ),
                ),
                trueCheckedPositions = List(QUESTIONS_IN_EXAM) { false },
                falseCheckedPositions = List(QUESTIONS_IN_EXAM) { false },
                responseList = List(QUESTIONS_IN_EXAM) { "" },
                mistakePositions = List(QUESTIONS_IN_EXAM) { 1 },
                timer = "39:42",
            ),
            onImageDetailsClick = {},
            onOpenMap = {},
            onExitExam = {},
            onRestartExam = {},
            onCheckTrueAtPage = {},
            onCheckFalseAtPage = {},
            onCompleteExam = {},
        )
    }
}

@Preview
@Composable
private fun ExamScreenLoadingPreview() {
    MerrPatentenTheme {
        ExamScreen(
            uiState = ExamUiState(questions = emptyList()),
            onImageDetailsClick = {},
            onOpenMap = {},
            onExitExam = {},
            onRestartExam = {},
            onCheckTrueAtPage = {},
            onCheckFalseAtPage = {},
            onCompleteExam = {},
        )
    }
}

@Preview
@Composable
private fun ExamScreenDarkPreview() {
    MerrPatentenTheme(darkTheme = true) {
        ExamScreen(
            uiState = ExamUiState(
                questions = listOf(
                    Question(
                        id = 1,
                        question = "Sinjali i paraqitur në figurë tregon një kthesë të rrezikshme majtas.",
                        image = 1,
                        answer = "Saktë",
                    ),
                ),
                trueCheckedPositions = List(QUESTIONS_IN_EXAM) { false },
                falseCheckedPositions = List(QUESTIONS_IN_EXAM) { false },
                responseList = List(QUESTIONS_IN_EXAM) { "" },
                mistakePositions = List(QUESTIONS_IN_EXAM) { 1 },
                timer = "39:42",
            ),
            onImageDetailsClick = {},
            onOpenMap = {},
            onExitExam = {},
            onRestartExam = {},
            onCheckTrueAtPage = {},
            onCheckFalseAtPage = {},
            onCompleteExam = {},
        )
    }
}

@Preview
@Composable
private fun ExamScreenCompletedPreview() {
    MerrPatentenTheme {
        ExamScreen(
            uiState = ExamUiState(
                questions = listOf(
                    Question(
                        id = 1,
                        question = "Sinjali i paraqitur në figurë tregon një kthesë të rrezikshme majtas.",
                        image = 1,
                        answer = "Saktë",
                    ),
                ),
                trueCheckedPositions = List(QUESTIONS_IN_EXAM) { true },
                falseCheckedPositions = List(QUESTIONS_IN_EXAM) { false },
                responseList = List(QUESTIONS_IN_EXAM) { "Saktë" },
                mistakePositions = List(QUESTIONS_IN_EXAM) { 0 },
                isCompleted = true,
                timer = "24:10",
            ),
            onImageDetailsClick = {},
            onOpenMap = {},
            onExitExam = {},
            onRestartExam = {},
            onCheckTrueAtPage = {},
            onCheckFalseAtPage = {},
            onCompleteExam = {},
        )
    }
}
