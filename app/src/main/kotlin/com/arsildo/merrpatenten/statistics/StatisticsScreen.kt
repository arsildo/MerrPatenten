package com.arsildo.merrpatenten.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material.icons.rounded.MobiledataOff
import androidx.compose.material.icons.rounded.MultipleStop
import androidx.compose.material3.Button
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arsildo.merrpatenten.R
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = koinViewModel(),
    onChangePreferenceClick: () -> Unit,
    onBackPress: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
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
                            imageVector = Icons.Rounded.ArrowBack, contentDescription = null
                        )
                    }
                },
                actions = {
                    if (uiState.saveResults && uiState.results.isNotEmpty()) IconButton(
                        onClick = { deleteResultsDialog = true },
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.DeleteSweep, contentDescription = null
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
            when {
                !uiState.saveResults -> ResultStoringDisabled(
                    text = R.string.results_storing_disabled,
                    icon = Icons.Rounded.MobiledataOff,
                    changePreference = {
                        Button(
                            onClick = onChangePreferenceClick,
                            shape = MaterialTheme.shapes.extraLarge,
                            contentPadding = PaddingValues(horizontal = 32.dp)
                        ) {
                            Text(text = stringResource(id = R.string.results_change))
                        }
                    }
                )

                else -> {
                    val results = uiState.results.sortedByDescending { it.id }
                    if (results.isNotEmpty()) Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        PerformanceGraph(results = results)
                        AverageMistakes(previousExamResults = results)
                        ResultList(results = results)
                    }
                    else ResultStoringDisabled(
                        text = R.string.results_empty,
                        icon = Icons.Rounded.MultipleStop
                    )
                }
            }
        }
    }

    if (deleteResultsDialog) DeleteResultsDialog(
        onConfirm = {
            viewModel.deleteAllResults()
            deleteResultsDialog = false
        },
        onDismiss = { deleteResultsDialog = false }
    )
}

