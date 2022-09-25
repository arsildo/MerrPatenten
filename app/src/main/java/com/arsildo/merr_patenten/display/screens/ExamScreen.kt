package com.arsildo.merr_patenten.display.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arsildo.merr_patenten.display.activities.ExamViewModel
import com.arsildo.merr_patenten.display.screens.components.ConcludeButton
import com.arsildo.merr_patenten.display.screens.components.ExamNavigator
import com.arsildo.merr_patenten.display.screens.components.Pager
import com.arsildo.merr_patenten.display.screens.components.PagerMap
import com.arsildo.merr_patenten.display.screens.components.ScreenLayout
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun ExamScreen() {

    val viewModel: ExamViewModel = viewModel()
    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    var concludeButton by remember { mutableStateOf(false) }

    val pagerState = rememberPagerState()
    ScreenLayout(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        ExamNavigator(
            currentPage = pagerState.currentPage,
            countDownTimer = viewModel.countDownTimer.value,
            onToggleClicked = { concludeButton = !concludeButton },
            onMapClicked = { scope.launch { sheetState.show() } }
        )

        Pager(
            pagerState = pagerState,
            list = viewModel.generatedQuestions
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = tween(
                        delayMillis = 128,
                        durationMillis = 256,
                        easing = LinearEasing
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Navigation Buttons")
            if (concludeButton) {
                ConcludeButton(
                    isExamCompleted = viewModel.isExamCompleted,
                    onClick = {
                        viewModel.isExamCompleted.value = true
                    }
                )
            }
        }
    }

    PagerMap(
        isExamCompleted = viewModel.isExamCompleted,
        sheetState = sheetState,
        onPositionClicked = {
            scope.launch {
                sheetState.hide()
                pagerState.animateScrollToPage(it)
            }
        },
        onHideSheet = {
            scope.launch { sheetState.hide() }
        }
    )
}