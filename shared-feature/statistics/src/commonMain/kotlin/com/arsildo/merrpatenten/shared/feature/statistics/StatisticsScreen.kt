package com.arsildo.merrpatenten.shared.feature.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.model.ExamResult
import merrpatenten.shared_core.design_system.generated.resources.Res
import merrpatenten.shared_core.design_system.generated.resources.results_change
import merrpatenten.shared_core.design_system.generated.resources.results_empty
import merrpatenten.shared_core.design_system.generated.resources.results_storing_disabled
import merrpatenten.shared_core.design_system.generated.resources.statistics
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StatisticsRoute(
    onChangePreferenceClick: () -> Unit,
    onBackPress: () -> Unit,
    viewModel: StatisticsViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    StatisticsScreen(
        uiState = uiState,
        onChangePreferenceClick = onChangePreferenceClick,
        onBackPress = onBackPress,
        onDeleteAllResults = viewModel::deleteAllResults,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    uiState: StatisticsUiState,
    onChangePreferenceClick: () -> Unit,
    onBackPress: () -> Unit,
    onDeleteAllResults: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var deleteResultsDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(Res.string.statistics)) },
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    if (uiState.saveResults && uiState.results.isNotEmpty()) {
                        IconButton(onClick = { deleteResultsDialog = true }) {
                            Icon(
                                imageVector = Icons.Rounded.DeleteSweep,
                                contentDescription = null
                            )
                        }
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
                    text = stringResource(Res.string.results_storing_disabled),
                    icon = Icons.Rounded.MobiledataOff,
                    changePreference = {
                        Button(
                            onClick = onChangePreferenceClick,
                            shape = MaterialTheme.shapes.extraLarge,
                            contentPadding = PaddingValues(horizontal = 32.dp)
                        ) {
                            Text(text = stringResource(Res.string.results_change))
                        }
                    }
                )

                else -> {
                    val results = uiState.results.sortedByDescending { it.id }
                    if (results.isNotEmpty()) {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            PerformanceGraph(results = results)
                            AverageMistakes(previousExamResults = results)
                            ResultList(results = results)
                        }
                    } else {
                        ResultStoringDisabled(
                            text = stringResource(Res.string.results_empty),
                            icon = Icons.Rounded.MultipleStop
                        )
                    }
                }
            }
        }
    }

    if (deleteResultsDialog) {
        DeleteResultsDialog(
            onConfirm = {
                onDeleteAllResults()
                deleteResultsDialog = false
            },
            onDismiss = { deleteResultsDialog = false }
        )
    }
}

@Preview
@Composable
private fun StatisticsScreenWithDataPreview() {
    MerrPatentenTheme {
        StatisticsScreen(
            uiState = StatisticsUiState(
                results = listOf(
                    ExamResult(id = 1, errors = 2, time = "34:20"),
                    ExamResult(id = 2, errors = 5, time = "28:10"),
                    ExamResult(id = 3, errors = 0, time = "31:45"),
                ),
                saveResults = true
            ),
            onChangePreferenceClick = {},
            onBackPress = {},
            onDeleteAllResults = {}
        )
    }
}

@Preview
@Composable
private fun StatisticsScreenEmptyPreview() {
    MerrPatentenTheme {
        StatisticsScreen(
            uiState = StatisticsUiState(results = emptyList(), saveResults = true),
            onChangePreferenceClick = {},
            onBackPress = {},
            onDeleteAllResults = {}
        )
    }
}

@Preview
@Composable
private fun StatisticsScreenDisabledPreview() {
    MerrPatentenTheme {
        StatisticsScreen(
            uiState = StatisticsUiState(results = emptyList(), saveResults = false),
            onChangePreferenceClick = {},
            onBackPress = {},
            onDeleteAllResults = {}
        )
    }
}

