package com.arsildo.merrpatenten.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    onBackPress: () -> Unit,
) {
    var deleteResultsDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.statistics)) },
                navigationIcon = {
                    IconButton(
                        onClick = onBackPress,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { deleteResultsDialog = true },
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.DeleteSweep,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
        },
        contentColor = MaterialTheme.colorScheme.secondary,
        contentWindowInsets = WindowInsets(top = 0, bottom = 0)
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
                Results(
                    errors = 8,
                    time = "12:11"
                ),
                Results(
                    errors = 8,
                    time = "12:11"
                )
            )
            if (results.isEmpty()) ResultStoringDisabled(enabled = false)
            else Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PerformanceGraph(results = results)
                AverageMistakes(previousExamResults = results)
                ResultList(results = results)
            }
        }
    }

    if (deleteResultsDialog) DeleteResultsDialog(
        onConfirm = { /*TODO*/ },
        onDismiss = { deleteResultsDialog = false }
    )
}

