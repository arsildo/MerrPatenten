package com.arsildo.merrpatenten.shared.feature.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arsildo.merrpatenten.shared.core.designsystem.ERRORS_ALLOWED
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.components.SectionHeader
import com.arsildo.merrpatenten.shared.core.designsystem.semanticColors
import com.arsildo.merrpatenten.shared.core.model.ExamResult
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun StatisticsRoute(
    viewModel: StatisticsViewModel = koinViewModel(),
    onChangePreferenceClick: () -> Unit,
    onBackPress: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    StatisticsScreen(
        uiState = uiState,
        onChangePreferenceClick = onChangePreferenceClick,
        onBackPress = onBackPress,
        onDeleteAllResults = viewModel::deleteAllResults,
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun StatisticsScreen(
    uiState: StatisticsUiState,
    onChangePreferenceClick: () -> Unit,
    onBackPress: () -> Unit,
    onDeleteAllResults: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val hapticFeedback = LocalHapticFeedback.current
    var deleteResultsDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(Res.string.statistics)) },
                navigationIcon = {
                    FilledTonalIconButton(
                        onClick = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onBackPress()
                        },
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(Res.string.back)
                        )
                    }
                },
                actions = {
                    if (uiState.saveResults && uiState.results.isNotEmpty()) {
                        FilledTonalIconButton(
                            onClick = {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.GestureThresholdActivate)
                                deleteResultsDialog = true
                            },
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.DeleteSweep,
                                contentDescription = stringResource(Res.string.results_delete)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
            )
        },
        contentColor = MaterialTheme.colorScheme.onBackground,
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
                            onClick = {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                onChangePreferenceClick()
                            },
                            shapes = ButtonShapes(
                                shape = MaterialTheme.shapes.large,
                                pressedShape = MaterialTheme.shapes.small
                            ),
                            contentPadding = PaddingValues(horizontal = 28.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = stringResource(Res.string.results_change),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                )

                else -> {
                    val results = uiState.results.sortedByDescending { it.id }
                    if (results.isNotEmpty()) {
                        val passedCount = results.count { it.errors <= ERRORS_ALLOWED }
                        val passRate = ((passedCount.toDouble() / results.size) * 100).roundToInt()
                        val totalErrors = results.sumOf { it.errors }
                        val avgErrors = ((totalErrors.toDouble() / results.size) * 10).roundToInt() / 10.0

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 32.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    StatSummaryCard(
                                        title = stringResource(Res.string.stat_exams),
                                        value = "${results.size}",
                                        icon = Icons.Rounded.Quiz,
                                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.weight(1f)
                                    )
                                    StatSummaryCard(
                                        title = stringResource(Res.string.stat_average),
                                        value = "$avgErrors",
                                        icon = Icons.Rounded.CheckCircle,
                                        containerColor = if (avgErrors <= ERRORS_ALLOWED) MaterialTheme.semanticColors.successContainer else MaterialTheme.colorScheme.errorContainer,
                                        contentColor = if (avgErrors <= ERRORS_ALLOWED) MaterialTheme.semanticColors.onSuccessContainer else MaterialTheme.colorScheme.onErrorContainer,
                                        modifier = Modifier.weight(1f)
                                    )
                                    StatSummaryCard(
                                        title = stringResource(Res.string.stat_pass_rate),
                                        value = "$passRate%",
                                        icon = Icons.Rounded.EmojiEvents,
                                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            item(span = { GridItemSpan(maxLineSpan) }) {
                                PerformanceGraph(
                                    results = results,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                            }

                            item(span = { GridItemSpan(maxLineSpan) }) {
                                SectionHeader(
                                    title = stringResource(Res.string.exam_history),
                                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                                )
                            }

                            resultList(results = results)
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

@Composable
private fun StatSummaryCard(
    title: String,
    value: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        color = containerColor,
        contentColor = contentColor,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(22.dp)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = contentColor.copy(alpha = 0.8f)
            )
        }
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
