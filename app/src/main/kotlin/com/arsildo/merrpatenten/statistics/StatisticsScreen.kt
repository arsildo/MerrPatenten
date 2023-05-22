package com.arsildo.merrpatenten.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    navController: NavController,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Statistics") },
                navigationIcon = {
                    IconButton(
                        onClick = navController::popBackStack,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = navController::popBackStack,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.DeleteSweep,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onTertiary,
                    actionIconContentColor = MaterialTheme.colorScheme.onTertiary,
                    titleContentColor = MaterialTheme.colorScheme.onTertiary,
                ),
            )
        },
        contentColor = MaterialTheme.colorScheme.secondary
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
        ) {
            val results = listOf(
                Results(
                    errors = 3,
                    time = "38:21"
                ),
                Results(
                    errors = 1,
                    time = "22:21"
                ),
                Results(
                    errors = 10,
                    time = "27:32"
                ),
                Results(
                    errors = 3,
                    time = "16:21"
                ),
                Results(
                    errors = 3,
                    time = "11:21"
                ),
                Results(
                    errors = 13,
                    time = "16:21"
                ),
                Results(
                    errors = 13,
                    time = "16:21"
                ),
                Results(
                    errors = 9,
                    time = "16:21"
                ),
                Results(
                    errors = 8,
                    time = "12:11"
                ),
            )
            if (results.isNotEmpty()) ResultStoringDisabled(enabled = false)
            else Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AverageMistakes(previousExamResults = results)
                PerformanceGraph(results = results)
                ResultList(results = results)
            }
        }
    }
}

