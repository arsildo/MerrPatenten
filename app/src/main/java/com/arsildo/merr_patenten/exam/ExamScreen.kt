package com.arsildo.merr_patenten.exam

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Checklist
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.arsildo.merr_patenten.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ExamScreen(
    navController: NavController,
    viewModel: ExamViewModel = hiltViewModel()
) {

    val coroutineScope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var questionMapVisible by remember { mutableStateOf(false) }
    var endExamVisible by remember { mutableStateOf(false) }

    Scaffold(
        contentWindowInsets = WindowInsets(top = 0, bottom = 0),
        contentColor = MaterialTheme.colorScheme.primary
    ) { contentPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.questions.isEmpty()) CircularProgressIndicator(strokeCap = StrokeCap.Round)
            else {
                Legend(
                    page = pagerState.currentPage,
                    timer = uiState.timer,
                    endExamVisible = endExamVisible,
                    onMapClick = { questionMapVisible = true },
                    onShowEndExamButton = {
                        endExamVisible = !endExamVisible
                        viewModel.endExam()
                    }
                )

                Pager(
                    questions = uiState.questions,
                    pagerState = pagerState,
                    isExamCompleted = uiState.isCompleted
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
                    if (endExamVisible) ExtendedFloatingActionButton(
                        text = { Text(text = stringResource(id = R.string.endExam)) },
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Checklist,
                                contentDescription = null
                            )
                        },
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        elevation = FloatingActionButtonDefaults.loweredElevation(),
                        shape = MaterialTheme.shapes.extraLarge,
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.End),
                    )

                }

            }
        }
    }

    if (questionMapVisible) {

        Map(
            sheetState = sheetState,
            onQuestionClicked = { page ->
                coroutineScope.launch { pagerState.animateScrollToPage(page) }
                questionMapVisible = false
            },
            onDismissRequest = { questionMapVisible = false }
        )

    }

}