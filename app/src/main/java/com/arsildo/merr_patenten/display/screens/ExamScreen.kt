package com.arsildo.merr_patenten.display.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import com.arsildo.merr_patenten.display.screens.components.ExamNavigator
import com.arsildo.merr_patenten.display.screens.components.PagerMap
import com.arsildo.merr_patenten.display.screens.components.ScreenLayout
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExamScreen(
    isExamCompleted: MutableState<Boolean>,
    countDownTimer: MutableState<String>,
    onExamConcluded: () -> Unit,
) {

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val scope = rememberCoroutineScope()
    ScreenLayout(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        ExamNavigator(
            countDownTimer = countDownTimer,
        )
        Button(
            onClick = { scope.launch { sheetState.show() } }
        ) {
            Text(text = "End Exam ${isExamCompleted.value}")
        }

    }

    PagerMap(
        sheetState = sheetState,
        onHideSheet = {
            scope.launch { sheetState.hide() }
        }
    )
}