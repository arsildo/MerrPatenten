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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.arsildo.merrpatenten.Destinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(
    navController: NavController,
    viewModel: LandingViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { /*empty title*/ },
                actions = {
                    IconButton(
                        onClick = { navController.navigate(Destinations.PREFERENCES_ROUTE) },
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Menu,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    actionIconContentColor = MaterialTheme.colorScheme.tertiary,
                ),
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = "Statistics") },
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.AutoGraph,
                        contentDescription = null
                    )
                },
                onClick = { navController.navigate(Destinations.STATISTICS_ROUTE) },
                contentColor = MaterialTheme.colorScheme.onTertiary,
                containerColor = MaterialTheme.colorScheme.tertiary,
                elevation = FloatingActionButtonDefaults.loweredElevation()
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
                    onClick = { navController.navigate(Destinations.EXAM_ROUTE) }
                )
            }
        }
    }

    val context = LocalContext.current
    val onBackPressedTwiceEnabled by viewModel.confirmAppExit.collectAsStateWithLifecycle(
        initialValue = true
    )
    var timeWhenPressed by remember { mutableStateOf(0L) }
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