package com.arsildo.merrpatenten.landing

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoGraph
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arsildo.merrpatenten.theme.Red

@Composable
fun LandingScreen(
    onStartExamClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    onPreferencesClick: () -> Unit,
    viewModel: LandingViewModel = hiltViewModel()
) {
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = "Statistics") },
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.AutoGraph,
                        contentDescription = null
                    )
                },
                onClick = onStatisticsClick,
                contentColor = Color.White,
                containerColor = Red,
                elevation = FloatingActionButtonDefaults.loweredElevation(),
                shape = MaterialTheme.shapes.extraLarge
            )
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column {
                ExamTypeCard(
                    onClick = onStartExamClick
                )
            }
            IconButton(
                onClick = onPreferencesClick,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = null
                )
            }
        }
    }

    val context = LocalContext.current
    val onBackPressedTwiceEnabled by viewModel.confirmAppExit.collectAsStateWithLifecycle(
        initialValue = true
    )
    var timeWhenPressed by remember { mutableLongStateOf(0L) }
    BackHandler {
        if (onBackPressedTwiceEnabled) onBackPressedTwice(
            timeWhenPressed = timeWhenPressed,
            context = context,
            updateTimeWhenPressed = { timeWhenPressed = System.currentTimeMillis() }
        ) else endApplication(context = context)
    }
}

private fun onBackPressedTwice(
    timeWhenPressed: Long,
    context: Context,
    updateTimeWhenPressed: () -> Unit
) {
    val activityContext = context as Activity
    if (timeWhenPressed + 2_000 > System.currentTimeMillis()) activityContext.finish()
    else Toast.makeText(context, "Swipe back once more to leave the app.", Toast.LENGTH_LONG).show()
    updateTimeWhenPressed()
}

private fun endApplication(context: Context) {
    val activityContext = context as Activity
    activityContext.finish()
}