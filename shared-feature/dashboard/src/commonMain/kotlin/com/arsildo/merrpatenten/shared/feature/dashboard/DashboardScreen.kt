package com.arsildo.merrpatenten.shared.feature.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Looks3
import androidx.compose.material.icons.filled.LooksOne
import androidx.compose.material.icons.filled.LooksTwo
import androidx.compose.material.icons.rounded.AutoGraph
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arsildo.merrpatenten.shared.core.designsystem.DPSHTRR_HELP
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.Red
import com.arsildo.merrpatenten.shared.core.designsystem.components.ExamTypeCard
import com.arsildo.merrpatenten.shared.core.designsystem.components.HelpfulMaterialCard
import kotlinx.coroutines.launch
import androidx.compose.ui.tooling.preview.Preview
import merrpatenten.shared_core.design_system.generated.resources.Res
import merrpatenten.shared_core.design_system.generated.resources.questionnaire_category_one
import merrpatenten.shared_core.design_system.generated.resources.questionnaire_category_three
import merrpatenten.shared_core.design_system.generated.resources.questionnaire_category_two
import merrpatenten.shared_core.design_system.generated.resources.statistics
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DashboardRoute(
    onStartExamClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    onPreferencesClick: () -> Unit,
    viewModel: DashboardViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DashboardScreen(
        uiState = uiState,
        onStartExamClick = onStartExamClick,
        onStatisticsClick = onStatisticsClick,
        onPreferencesClick = onPreferencesClick,
    )
}

@Composable
fun DashboardScreen(
    uiState: DashboardUiState,
    onStartExamClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    onPreferencesClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = stringResource(Res.string.statistics)) },
                icon = {
                    Icon(imageVector = Icons.Rounded.AutoGraph, contentDescription = null)
                },
                onClick = onStatisticsClick,
                contentColor = Color.White,
                containerColor = Red,
                elevation = FloatingActionButtonDefaults.loweredElevation(),
                shape = MaterialTheme.shapes.extraLarge,
            )
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
                .padding(top = 32.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ExamTypeCard(
                    title = stringResource(Res.string.questionnaire_category_one),
                    description = "40 MINUTA | 40 PYETJE | 4 GABIME",
                    icon = Icons.Filled.LooksOne,
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    onClick = onStartExamClick
                )

                ExamTypeCard(
                    title = stringResource(Res.string.questionnaire_category_two),
                    description = "40 MINUTA | 40 PYETJE | 4 GABIME",
                    icon = Icons.Filled.LooksTwo,
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    ),
                    onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("Vjen së shpejti...")
                        }
                    }
                )

                ExamTypeCard(
                    title = stringResource(Res.string.questionnaire_category_three),
                    description = "10 MINUTA | 10 PYETJE | 1 GABIM",
                    icon = Icons.Filled.Looks3,
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("Vjen së shpejti...")
                        }
                    }
                )

                HelpfulMaterialCard(
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    onClick = {
                        uriHandler.openUri(DPSHTRR_HELP)
                    }
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
}

@Preview
@Composable
private fun DashboardScreenPreview() {
    MerrPatentenTheme {
        DashboardScreen(
            uiState = DashboardUiState(),
            onStartExamClick = {},
            onStatisticsClick = {},
            onPreferencesClick = {}
        )
    }
}

@Preview
@Composable
private fun DashboardScreenDarkPreview() {
    MerrPatentenTheme(darkTheme = true) {
        DashboardScreen(
            uiState = DashboardUiState(),
            onStartExamClick = {},
            onStatisticsClick = {},
            onPreferencesClick = {}
        )
    }
}

