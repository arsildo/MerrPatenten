package com.arsildo.merr_patenten.display.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arsildo.merr_patenten.display.activities.ExamViewModel
import com.arsildo.merr_patenten.display.screens.components.ExamNavigator
import com.arsildo.merr_patenten.display.screens.components.PagerMap
import com.arsildo.merr_patenten.display.screens.components.ScreenLayout
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExamScreen() {

    val viewModel: ExamViewModel = viewModel()

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    ScreenLayout(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        ExamNavigator(
            countDownTimer = viewModel.countDownTimer,
            onClick = { scope.launch { sheetState.show() } }
        )
        Button(
            onClick = {}
        ) {
            Text(text = "Click")
        }
    }

    PagerMap(
        isExamCompleted = viewModel.isExamCompleted,
        sheetState = sheetState,
        onPositionClicked = {},
        onHideSheet = {
            scope.launch { sheetState.hide() }
        }
    )
}