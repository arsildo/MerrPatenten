package com.arsildo.merrpatenten.exam

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.arsildo.merrpatenten.Destinations
import com.arsildo.merrpatenten.R
import com.arsildo.merrpatenten.theme.Red
import com.arsildo.merrpatenten.utils.QUESTIONS_IN_EXAM
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ExamScreen(
    navController: NavController,
    viewModel: ExamViewModel = hiltViewModel()
) {

    val coroutineScope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState { QUESTIONS_IN_EXAM }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var questionMapVisible by remember { mutableStateOf(false) }
    var endExamVisible by remember { mutableStateOf(false) }

    Scaffold(
        contentWindowInsets = WindowInsets(top = 0, bottom = 0),
        contentColor = MaterialTheme.colorScheme.primary,
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            when (uiState.questions.isEmpty()) {
                true -> {
                    CircularProgressIndicator(
                        strokeCap = StrokeCap.Round,
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize()
                    )
                }

                false -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Legend(
                            pagerState = pagerState,
                            timer = uiState.timer,
                            endExamVisible = endExamVisible,
                            onMapClick = { questionMapVisible = true },
                            onShowEndExamButton = { endExamVisible = !endExamVisible }
                        )

                        Pager(
                            questions = uiState.questions,
                            pagerState = pagerState,
                            falseCheckedPages = viewModel.falseCheckedPositions,
                            trueCheckedPages = viewModel.trueCheckedPositions,
                            onCheckFalseAtPage = viewModel::checkFalseAtPosition,
                            onCheckTrueAtPage = viewModel::checkTrueAtPosition,
                            isCompleted = uiState.isCompleted,
                            responses = viewModel.mistakePositions
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateContentSize()
                                .navigationBarsPadding()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            PagerNavigation(
                                onPreviousPageClick = {
                                    if (pagerState.canScrollBackward) coroutineScope.launch {
                                        pagerState.animateScrollToPage(page = pagerState.currentPage - 1)
                                    }
                                },
                                onNextPageClick = {
                                    if (pagerState.canScrollForward) coroutineScope.launch {
                                        pagerState.animateScrollToPage(page = pagerState.currentPage + 1)
                                    }
                                }
                            )
                            if (endExamVisible) EndExamButton(
                                title = if (uiState.isCompleted) R.string.restartExam else R.string.endExam,
                                icon = if (uiState.isCompleted) Icons.Rounded.RestartAlt else Icons.Rounded.DoneAll,
                                containerColor = if (uiState.isCompleted) MaterialTheme.colorScheme.secondary else Red,
                                onClick = {
                                    if (uiState.isCompleted) {
                                        navController.navigate(Destinations.EXAM_ROUTE) {
                                            popUpTo(Destinations.EXAM_ROUTE) {
                                                inclusive = true
                                            }
                                        }
                                    } else {
                                        viewModel.concludeExam()
                                        questionMapVisible = false
                                    }
                                },
                                modifier = Modifier.align(Alignment.End)
                            )

                        }
                    }
                }

            }
        }
    }

    if (questionMapVisible) {
        Map(
            sheetState = sheetState,
            isCompleted = uiState.isCompleted,
            responses = viewModel.responseList,
            mistakes = viewModel.mistakePositions,
            errors = uiState.errors,
            onQuestionClicked = { page ->
                coroutineScope.launch { pagerState.animateScrollToPage(page) }
                questionMapVisible = false
            },
            onDismissRequest = { questionMapVisible = false }
        )

    }

    BackHandler {
        endExamVisible = !endExamVisible
    }

}