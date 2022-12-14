package com.arsildo.merr_patenten.display.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.arsildo.merr_patenten.display.activities.ExamViewModel
import com.arsildo.merr_patenten.display.screens.components.ConcludeButton
import com.arsildo.merr_patenten.display.screens.components.ExamNavigator
import com.arsildo.merr_patenten.display.screens.components.Pager
import com.arsildo.merr_patenten.display.screens.components.PagerMap
import com.arsildo.merr_patenten.display.screens.components.PagerNavigation
import com.arsildo.merr_patenten.display.screens.components.ScreenLayout
import com.arsildo.merr_patenten.logic.cache.ExamResult
import com.arsildo.merr_patenten.logic.cache.UserPreferences
import com.arsildo.merr_patenten.logic.navigation.Destinations
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun ExamScreen(navController: NavHostController) {

    val viewModel: ExamViewModel = viewModel()
    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    val concludeButton = remember { mutableStateOf(false) }

    val pagerState = rememberPagerState()
    ScreenLayout(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        ExamNavigator(
            currentPage = pagerState.currentPage,
            countDownTimer = remember(viewModel) { { viewModel.countDownTimer.value } },
            concludeButton = concludeButton.value,
            onToggleClicked = { concludeButton.value = !concludeButton.value },
            onMapClicked = { scope.launch { sheetState.show() } }
        )

        val trueCheckedPositions = remember { viewModel.trueCheckedPositions }
        val falseCheckedPositions = remember { viewModel.falseCheckedPositions }
        Pager(
            pagerState = pagerState,
            isExamCompleted = viewModel.isExamCompleted.value,
            list = viewModel.generatedQuestions,
            mistakePositions = viewModel.mistakePositions,
            trueCheckedPositions = trueCheckedPositions,
            falseCheckedPositions = falseCheckedPositions,
            checkFalseAt = { viewModel.checkFalseAtPosition(it) },
            checkTrueAt = { viewModel.checkTrueAtPosition(it) }
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = tween(
                        delayMillis = 64,
                        durationMillis = 128,
                        easing = LinearEasing
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PagerNavigation(
                onPreviousClicked = {
                    if (pagerState.currentPage > 0) {
                        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                    }
                },
                onNextClicked = {
                    if (pagerState.currentPage < 39) {
                        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    }
                }
            )
            val dataStore = UserPreferences(LocalContext.current)
            val previousExamResults =
                dataStore.getExamResults.collectAsState(initial = listOf())
            val ifCacheResults =
                dataStore.getExamStatsPreference.collectAsState(initial = true).value
            if (concludeButton.value) {
                ConcludeButton(
                    isExamCompleted = viewModel.isExamCompleted.value,
                    onClick = {
                        if (!viewModel.isExamCompleted.value) {
                            viewModel.isExamCompleted.value = true
                            viewModel.countErrors()
                            scope.launch { sheetState.show() }
                            if (ifCacheResults) {
                                scope.launch {
                                    dataStore.saveExamResults(
                                        previousExamResults.value.plus(
                                            ExamResult(
                                                viewModel.errors.value,
                                                viewModel.countDownTimer.value
                                            )
                                        ).takeLast(10)
                                    )
                                }
                            }
                        } else scope.launch { sheetState.show() }

                    },
                    onCloseDestination = { navController.popBackStack() },
                    onRestartDestination = {
                        navController.navigate(Destinations.Exam.route) {
                            popUpTo(Destinations.Exam.route) { inclusive = true }
                        }
                    }
                )
            }
            LaunchedEffect(viewModel.isExamCompleted.value) {
                viewModel.startCountDown {
                    if (!sheetState.isVisible) scope.launch { sheetState.show() }
                    if (ifCacheResults) {
                        scope.launch {
                            dataStore.saveExamResults(
                                previousExamResults.value.plus(
                                    ExamResult(
                                        viewModel.errors.value,
                                        viewModel.countDownTimer.value
                                    )
                                ).takeLast(10)
                            )
                        }
                    }
                }
            }
        }
    }
    PagerMap(
        isExamCompleted = viewModel.isExamCompleted.value,
        errors = viewModel.errors.value,
        sheetState = sheetState,
        responseList = viewModel.responseList,
        mistakePositions = viewModel.mistakePositions,
        onPositionClicked = {
            scope.launch {
                sheetState.hide()
                pagerState.animateScrollToPage(it)
            }
        },
        onHideSheet = { scope.launch { sheetState.hide() } }

    )
    BackHandler {
        if (!viewModel.isExamCompleted.value && !sheetState.isVisible)
            concludeButton.value = !concludeButton.value
        else if (!sheetState.isVisible) navController.popBackStack()
        if (sheetState.isVisible) scope.launch {
            concludeButton.value = false
            sheetState.hide()
        }
    }
}