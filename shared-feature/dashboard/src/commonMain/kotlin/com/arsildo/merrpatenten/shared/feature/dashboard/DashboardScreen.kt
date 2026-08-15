package com.arsildo.merrpatenten.shared.feature.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.AutoGraph
import androidx.compose.material.icons.rounded.DirectionsBus
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.Motorcycle
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.DPSHTRR_HELP
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.Red
import com.arsildo.merrpatenten.shared.core.designsystem.components.ExamTypeCard
import com.arsildo.merrpatenten.shared.core.designsystem.components.HelpfulMaterialCard
import com.arsildo.merrpatenten.shared.core.designsystem.components.SectionHeader
import kotlinx.coroutines.launch
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun DashboardRoute(
    onStartExamClick: () -> Unit,
    onPreferencesClick: () -> Unit,
    onStatisticsClick: () -> Unit,
) {
    DashboardScreen(
        onStartExamClick = onStartExamClick,
        onPreferencesClick = onPreferencesClick,
        onStatisticsClick = onStatisticsClick,
    )
}

@Composable
fun DashboardScreen(
    onStartExamClick: () -> Unit,
    onPreferencesClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current
    var showDisclaimerDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val categoryAInProgressMessage = stringResource(Res.string.category_a_in_progress)
    val categoryCDInProgressMessage = stringResource(Res.string.category_cd_in_progress)

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = {
                    Text(
                        text = stringResource(Res.string.statistics),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                icon = {
                    Icon(imageVector = Icons.Rounded.AutoGraph, contentDescription = null)
                },
                onClick = onStatisticsClick,
                contentColor = Color.White,
                containerColor = Red,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 3.dp),
                shape = MaterialTheme.shapes.large,
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp, bottom = 88.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Expressive Top Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.app_name),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = stringResource(Res.string.app_tagline),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilledTonalIconButton(
                        onClick = { showDisclaimerDialog = true },
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(Res.string.info),
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    FilledTonalIconButton(
                        onClick = onPreferencesClick,
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Settings,
                            contentDescription = stringResource(Res.string.preferences),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            // Categories Section Title
            SectionHeader(title = stringResource(Res.string.select_category))

            // Exam Category Cards
            ExamTypeCard(
                title = stringResource(Res.string.questionnaire_category_one),
                description = stringResource(Res.string.category_b_desc),
                icon = Icons.Rounded.DirectionsCar,
                onClick = onStartExamClick,
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
            )

            ExamTypeCard(
                title = stringResource(Res.string.questionnaire_category_two),
                description = stringResource(Res.string.category_a_desc),
                icon = Icons.Rounded.Motorcycle,
                onClick = {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(categoryAInProgressMessage)
                    }
                },
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
            )

            ExamTypeCard(
                title = stringResource(Res.string.questionnaire_category_three),
                description = stringResource(Res.string.category_cd_desc),
                icon = Icons.Rounded.DirectionsBus,
                onClick = {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(categoryCDInProgressMessage)
                    }
                },
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                ),
            )

            // Helpful Material Card
            HelpfulMaterialCard(
                onClick = { uriHandler.openUri(DPSHTRR_HELP) },
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
            )
        }
    }

    if (showDisclaimerDialog) {
        DisclaimerDialog(
            onDismissRequest = { showDisclaimerDialog = false }
        )
    }
}

@Preview
@Composable
private fun DashboardScreenPreview() {
    MerrPatentenTheme {
        DashboardScreen(
            onStartExamClick = {},
            onPreferencesClick = {},
            onStatisticsClick = {},
        )
    }
}
