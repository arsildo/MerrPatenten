package com.arsildo.merrpatenten.shared.feature.exam

import androidx.compose.animation.animateContentSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.QUESTIONS_IN_EXAM
import com.arsildo.merrpatenten.shared.core.designsystem.Red
import com.arsildo.merrpatenten.shared.core.designsystem.components.EndExamButton
import com.arsildo.merrpatenten.shared.core.designsystem.components.ExitExamButton
import com.arsildo.merrpatenten.shared.core.designsystem.components.RestartExamButton
import com.arsildo.merrpatenten.shared.core.model.Question
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ExamRoute(
    onImageDetailsClick: (Int) -> Unit,
    onOpenMap: () -> Unit,
    onExitExam: () -> Unit,
    onRestartExam: () -> Unit,
    viewModel: ExamViewModel = koinViewModel(),
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
}

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
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.questions.isEmpty()) {
                CircularProgressIndicator(
                    strokeCap = StrokeCap.Round,
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
                    Legend(
                        pagerState = pagerState,
                        timer = { uiState.timer },
                        endExamVisible = endExamVisible,
                        onMapClick = onOpenMap,
                        onShowEndExamButton = { endExamVisible = !endExamVisible }
                    )

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
                    )

                    LaunchedEffect(pagerState.settledPage) {
                        endExamVisible = pagerState.settledPage == QUESTIONS_IN_EXAM - 1
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize()
                            .padding(16.dp),
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
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    ExitExamButton(
                                        icon = Icons.AutoMirrored.Rounded.ExitToApp,
                                        onClick = onExitExam,
                                        modifier = Modifier.fillMaxWidth(0.4f)
                                    )
                                    RestartExamButton(
                                        icon = Icons.Rounded.RestartAlt,
                                        modifier = Modifier.fillMaxWidth(),
                                        onClick = onRestartExam
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
            }
        }
    }

    if (questionsUnCompletedDialog) {
        AlertDialog(
            onDismissRequest = { questionsUnCompletedDialog = false },
            tonalElevation = 0.dp,
            title = {
                Text(text = "Kujdes!")
            },
            text = {
                Text(text = "Ju nuk i keni plotësuar të gjithë pyetjet...")
            },
            confirmButton = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = {
                            questionsUnCompletedDialog = false
                            onOpenMap()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp),
                        content = {
                            Text(text = "Shiko pyetjet e papërgjigjura")
                        }
                    )
                    Button(
                        onClick = {
                            questionsUnCompletedDialog = false
                            onCompleteExam()
                            onOpenMap()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp),
                        content = {
                            Text(text = "Përfundo provimin")
                        }
                    )
                    Button(
                        onClick = {
                            questionsUnCompletedDialog = false
                            onExitExam()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Red,
                            contentColor = Color.White
                        ),
                        content = {
                            Text(text = "Dil nga provimi")
                        }
                    )
                }
            }
        )
    }

    LaunchedEffect(uiState.isCompleted) {
        if (uiState.isCompleted) {
            onOpenMap()
        }
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

