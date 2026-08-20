package com.arsildo.merrpatenten.shared.feature.dashboard.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.DPSHTRR_HELP
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.components.ExamTypeCard
import com.arsildo.merrpatenten.shared.core.designsystem.components.HelpfulMaterialCard
import com.arsildo.merrpatenten.shared.core.designsystem.components.SectionHeader
import com.arsildo.merrpatenten.shared.feature.dashboard.ui.components.DisclaimerDialog
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun DashboardRoute(
    onStartExamClick: (String) -> Unit,
    onPreferencesClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    onCatalogClick: () -> Unit,
) {
    DashboardScreen(
        onStartExamClick = onStartExamClick,
        onPreferencesClick = onPreferencesClick,
        onStatisticsClick = onStatisticsClick,
        onCatalogClick = onCatalogClick,
    )
}

@Composable
internal fun DashboardScreen(
    onStartExamClick: (String) -> Unit,
    onPreferencesClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    onCatalogClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val uriHandler = LocalUriHandler.current
    var showDisclaimerDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = stringResource(Res.string.app_name),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                        Text(
                            text = stringResource(Res.string.app_tagline),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
            )
        },
        modifier = modifier,
        floatingActionButton = {
            HorizontalFloatingToolbar(
                expanded = true,
                modifier = Modifier.navigationBarsPadding(),
                colors = FloatingToolbarDefaults.vibrantFloatingToolbarColors(),
            ) {
                IconButton(
                    onClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onCatalogClick()
                    },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.MenuBook,
                        contentDescription = stringResource(Res.string.catalog_title),
                    )
                }
                IconButton(
                    onClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onStatisticsClick()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AutoGraph,
                        contentDescription = stringResource(Res.string.statistics),
                    )
                }
                IconButton(
                    onClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onPreferencesClick()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = stringResource(Res.string.preferences),
                    )
                }
                IconButton(
                    onClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        showDisclaimerDialog = true
                    },
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = stringResource(Res.string.info),
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        contentWindowInsets = WindowInsets(bottom = 0),
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp, bottom = 104.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            // 1. First Major Category: Official Exam Simulations
            SectionHeader(title = stringResource(Res.string.dashboard_section_exams))

            // Exam Category Cards
            ExamTypeCard(
                title = stringResource(Res.string.questionnaire_category_one),
                description = stringResource(Res.string.category_b_desc),
                icon = Icons.Rounded.DirectionsCar,
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    onStartExamClick("B")
                },
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
            )

            ExamTypeCard(
                title = stringResource(Res.string.questionnaire_category_two),
                description = stringResource(Res.string.category_cd_desc),
                icon = Icons.Rounded.LocalShipping,
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    onStartExamClick("C")
                },
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                ),
            )

            ExamTypeCard(
                title = stringResource(Res.string.questionnaire_category_three),
                description = stringResource(Res.string.category_cd_desc),
                icon = Icons.Rounded.DirectionsBus,
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    onStartExamClick("D")
                },
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                ),
            )

            // 2. Second Major Category: Study & Learning Materials
            SectionHeader(title = stringResource(Res.string.dashboard_section_learning))

            // Road Signs & Intersections Encyclopedia Card
            ExamTypeCard(
                title = stringResource(Res.string.catalog_dashboard_title),
                description =
                stringResource(Res.string.catalog_category_warning) + " | " + stringResource(Res.string.catalog_category_prohibitory) +
                    " | " +
                    stringResource(Res.string.catalog_category_intersections),
                icon = Icons.AutoMirrored.Rounded.MenuBook,
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    onCatalogClick()
                },
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ),
            )

            // Helpful Material Card
            HelpfulMaterialCard(
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    uriHandler.openUri(DPSHTRR_HELP)
                },
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ),
            )
        }
    }

    if (showDisclaimerDialog) {
        DisclaimerDialog(
            onDismissRequest = { showDisclaimerDialog = false },
        )
    }
}

@Preview
@Composable
private fun DashboardScreenPreview() = MerrPatentenTheme {
    DashboardScreen(
        onStartExamClick = {},
        onPreferencesClick = {},
        onStatisticsClick = {},
        onCatalogClick = {},
    )
}

@Preview
@Composable
private fun DashboardScreenDarkPreview() = MerrPatentenTheme(darkTheme = true) {
    DashboardScreen(
        onStartExamClick = {},
        onPreferencesClick = {},
        onStatisticsClick = {},
        onCatalogClick = {},
    )
}
