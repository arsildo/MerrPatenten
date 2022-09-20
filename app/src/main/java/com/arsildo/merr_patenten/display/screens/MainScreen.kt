package com.arsildo.merr_patenten.display.screens

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.arsildo.merr_patenten.display.activities.ExamActivity
import com.arsildo.merr_patenten.display.screens.components.ScreenLayout
import com.arsildo.merr_patenten.logic.extensions.launchActivity

@Composable
fun MainScreen() {
    ScreenLayout {
        val context = LocalContext.current
        Button(
            onClick = { context.launchActivity<ExamActivity>() }
        ) {
            Text(text = "Navigate")
        }
    }
    //TODO Fill Display
}