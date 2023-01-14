package com.arsildo.merr_patenten.display.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.arsildo.merr_patenten.logic.cache.UserPreferences
import com.arsildo.merr_patenten.logic.navigation.Destinations
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun ExamScreen(navController: NavHostController) {
    val viewModel: ExamViewModel = viewModel()

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var isExamCompleted by viewModel.isExamCompleted
    val errors by viewModel.errors
    val generatedQuestions = viewModel.generatedQuestions
    val mistakePositions = viewModel.mistakePositions
    val responseList = viewModel.responseList

    val trueCheckedPositions = viewModel.trueCheckedPositions
    val falseCheckedPositions = viewModel.falseCheckedPositions

    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    fun showBottomSheet() = coroutineScope.launch { bottomSheetState.show() }
    fun hideBottomSheet() = coroutineScope.launch { bottomSheetState.hide() }

    var concludeButton by remember { mutableStateOf(false) }

    val pagerState = rememberPagerState()
    ScreenLayout(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        ExamNavigator(
            currentPage = pagerState.currentPage,
            countDownTimer = remember(viewModel) { { viewModel.countDownTimer.value } },
            concludeButton = concludeButton,
            onToggleClicked = { concludeButton = !concludeButton },
            onMapClicked = ::showBottomSheet
        )

        Pager(
            pagerState = pagerState,
            isExamCompleted = isExamCompleted,
            list = generatedQuestions,
            mistakePositions = mistakePositions,
            trueCheckedPositions = trueCheckedPositions,
            falseCheckedPositions = falseCheckedPositions,
            checkFalseAt = viewModel::checkFalseAtPosition,
            checkTrueAt = viewModel::checkTrueAtPosition
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            fun animateToNextPage() = coroutineScope.launch {
                if (pagerState.currentPage < 39)
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }

            fun animateToPreviousPage() = coroutineScope.launch {
                if (pagerState.currentPage > 0)
                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
            }

            PagerNavigation(
                onPreviousClicked = ::animateToPreviousPage,
                onNextClicked = ::animateToNextPage
            )


            val rememberResultsEnabled by UserPreferences(context)
                .getExamStatsPreference.collectAsState(true)
            if (concludeButton) {
                ConcludeButton(
                    isExamCompleted = isExamCompleted,
                    onClick = {
                        if (isExamCompleted) showBottomSheet()
                        else {
                            isExamCompleted = true
                            viewModel.countErrors()
                            showBottomSheet()
                            viewModel.saveExamResult(rememberResultsEnabled, context)
                        }

                    },
                    onCloseDestination = navController::popBackStack,
                    onRestartDestination = {
                        navController.navigate(Destinations.Exam.route) {
                            popUpTo(Destinations.Exam.route) { inclusive = true }
                        }
                    }
                )
            }
            LaunchedEffect(isExamCompleted) {
                viewModel.startCountDown(
                    onCountDownEnded = {
                        if (!bottomSheetState.isVisible) showBottomSheet()
                        viewModel.saveExamResult(rememberResultsEnabled, context)
                    }
                )
            }
        }
    }

    fun animateToSelectedPage(page: Int) = coroutineScope.launch {
        bottomSheetState.hide()
        pagerState.animateScrollToPage(page)
    }

    PagerMap(
        isExamCompleted = isExamCompleted,
        errors = errors,
        sheetState = bottomSheetState,
        responseList = responseList,
        mistakePositions = mistakePositions,
        onPositionClicked = ::animateToSelectedPage,
        onHideSheet = ::hideBottomSheet
    )


    BackHandler {
        if (!isExamCompleted && !bottomSheetState.isVisible) concludeButton = !concludeButton
        else if (!bottomSheetState.isVisible) navController.popBackStack()
        if (bottomSheetState.isVisible) coroutineScope.launch {
            concludeButton = false
            hideBottomSheet()
        }
    }

}