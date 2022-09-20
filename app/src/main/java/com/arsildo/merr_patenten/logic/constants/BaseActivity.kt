package com.arsildo.merr_patenten.logic.constants

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.arsildo.merr_patenten.display.theme.MerrPatentenTheme

abstract class BaseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MerrPatentenTheme() {
                Content()
            }
        }
    }

    @Composable
    abstract fun Content()
}