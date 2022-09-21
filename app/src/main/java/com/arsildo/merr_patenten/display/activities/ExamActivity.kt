package com.arsildo.merr_patenten.display.activities

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arsildo.merr_patenten.display.screens.ExamScreen
import com.arsildo.merr_patenten.logic.constants.BaseActivity

class ExamActivity : BaseActivity() {

    @Composable
    override fun Content() {
        val viewModel: ExamViewModel = viewModel()
        ExamScreen(
            isExamCompleted = viewModel.isExamCompleted,
            countDownTimer = viewModel.countDownTimer,
            onExamConcluded = { viewModel.isExamCompleted.value = true }
        )

    }
}