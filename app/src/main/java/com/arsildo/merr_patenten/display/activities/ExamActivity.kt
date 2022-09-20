package com.arsildo.merr_patenten.display.activities

import androidx.compose.runtime.Composable
import com.arsildo.merr_patenten.display.screens.ExamScreen
import com.arsildo.merr_patenten.logic.constants.BaseActivity

class ExamActivity : BaseActivity() {

    @Composable
    override fun Content() {
        ExamScreen()
    }
}