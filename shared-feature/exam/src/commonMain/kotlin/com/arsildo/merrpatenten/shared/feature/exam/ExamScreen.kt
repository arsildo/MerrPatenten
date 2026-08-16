package com.arsildo.merrpatenten.shared.feature.exam

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.DEFAULT_ARGS_KEY
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.savedstate.savedState
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.QUESTIONS_IN_EXAM
import com.arsildo.merrpatenten.shared.core.designsystem.components.EndExamButton
import com.arsildo.merrpatenten.shared.core.designsystem.components.ExitExamButton
import com.arsildo.merrpatenten.shared.core.designsystem.components.RestartExamButton
import com.arsildo.merrpatenten.shared.core.designsystem.semanticColors
import com.arsildo.merrpatenten.shared.core.model.Question
import kotlinx.coroutines.launch
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun ExamRoute(
    viewModel: ExamViewModel = koinViewModel(),
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
    modifier: Modifier = Modifier,
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
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { QUESTIONS_IN_EXAM })
    var questionsUnCompletedDialog by remember { mutableStateOf(false) }
    var endExamVisible by remember { mutableStateOf(false) }

    LaunchedEffect(targetPage) {
        targetPage?.let { page ->
            pagerState.animateScrollToPage(page)
            onPageNavigated()
        }
    }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(bottom = 0),
        contentColor = MaterialTheme.colorScheme.primary,
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
                    .padding(16.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
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
                        }
                    )
                }

                if (endExamVisible) {
                    if (uiState.isCompleted) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            RestartExamButton(
                                icon = Icons.Rounded.RestartAlt,
                                modifier = Modifier.fillMaxWidth(),
                                onClick = onRestartExam
                            )
                            ExitExamButton(
                                icon = Icons.AutoMirrored.Rounded.ExitToApp,
                                onClick = onExitExam,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    } else {
                        EndExamButton(
                            icon = Icons.Rounded.DoneAll,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                val completedCount = uiState.responseList.count { it.isNotBlank() }
                                if (completedCount != QUESTIONS_IN_EXAM) {
                                    questionsUnCompletedDialog = true
                                } else {
                                    onCompleteExam()
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.questions.isEmpty()) {
                LoadingIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize()
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Legend(
                            pagerState = pagerState,
                            timer = { uiState.timer },
                            endExamVisible = endExamVisible,
                            onMapClick = onOpenMap,
                            onShowEndExamButton = { endExamVisible = !endExamVisible }
                        )

                        val completedCount = remember(uiState.responseList) {
                            uiState.responseList.count { it.isNotBlank() }
                        }
                        val progress = (completedCount.toFloat() / QUESTIONS_IN_EXAM.toFloat()).coerceIn(0f, 1f)
                        val animatedProgress by animateFloatAsState(
                            targetValue = progress,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioNoBouncy,
                                stiffness = Spring.StiffnessMediumLow
                            ),
                            label = "ExamCompletionProgress"
                        )

                        LinearProgressIndicator(
                            progress = { animatedProgress },
                            color = MaterialTheme.semanticColors.successContainer,
                            drawStopIndicator = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }

                    Pager(
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
        AlertDialog(
            onDismissRequest = { questionsUnCompletedDialog = false },
            shape = MaterialTheme.shapes.extraLarge,
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            icon = {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.errorContainer,
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Rounded.DoneAll,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            },
            title = {
                Text(
                    text = stringResource(Res.string.uncompleted_dialog_title),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = stringResource(Res.string.uncompleted_dialog_desc),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            questionsUnCompletedDialog = false
                            onOpenMap()
                        },
                        shapes = ButtonShapes(
                            shape = MaterialTheme.shapes.medium,
                            pressedShape = MaterialTheme.shapes.small
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(14.dp),
                        content = {
                            Text(
                                text = stringResource(Res.string.review_unanswered),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                            )
                        }
                    )
                    FilledTonalButton(
                        onClick = {
                            questionsUnCompletedDialog = false
                            onCompleteExam()
                            onOpenMap()
                        },
                        shapes = ButtonShapes(
                            shape = MaterialTheme.shapes.medium,
                            pressedShape = MaterialTheme.shapes.small
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(14.dp),
                        content = {
                            Text(
                                text = stringResource(Res.string.finish_anyway),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                            )
                        }
                    )
                    Button(
                        onClick = {
                            questionsUnCompletedDialog = false
                            onExitExam()
                        },
                        shapes = ButtonShapes(
                            shape = MaterialTheme.shapes.medium,
                            pressedShape = MaterialTheme.shapes.small
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ),
                        content = {
                            Text(
                                text = stringResource(Res.string.exit_exam_button),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                            )
                        }
                    )
                }
            }
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
                        answer = "Saktë"
                    )
                ),
                trueCheckedPositions = List(QUESTIONS_IN_EXAM) { false },
                falseCheckedPositions = List(QUESTIONS_IN_EXAM) { false },
                responseList = List(QUESTIONS_IN_EXAM) { "" },
                mistakePositions = List(QUESTIONS_IN_EXAM) { 1 },
                timer = "39:42"
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
                        answer = "Saktë"
                    )
                ),
                trueCheckedPositions = List(QUESTIONS_IN_EXAM) { false },
                falseCheckedPositions = List(QUESTIONS_IN_EXAM) { false },
                responseList = List(QUESTIONS_IN_EXAM) { "" },
                mistakePositions = List(QUESTIONS_IN_EXAM) { 1 },
                timer = "39:42"
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
                        answer = "Saktë"
                    )
                ),
                trueCheckedPositions = List(QUESTIONS_IN_EXAM) { true },
                falseCheckedPositions = List(QUESTIONS_IN_EXAM) { false },
                responseList = List(QUESTIONS_IN_EXAM) { "Saktë" },
                mistakePositions = List(QUESTIONS_IN_EXAM) { 0 },
                isCompleted = true,
                timer = "24:10"
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

