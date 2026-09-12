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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arsildo.merrpatenten.shared.core.data.ReadinessLevel
import com.arsildo.merrpatenten.shared.core.data.ReadinessScore
import com.arsildo.merrpatenten.shared.core.designsystem.DPSHTRR_HELP
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.components.ExamTypeCard
import com.arsildo.merrpatenten.shared.core.designsystem.components.HelpfulMaterialCard
import com.arsildo.merrpatenten.shared.core.designsystem.components.SectionHeader
import com.arsildo.merrpatenten.shared.feature.dashboard.DashboardUiState
import com.arsildo.merrpatenten.shared.feature.dashboard.DashboardViewModel
import com.arsildo.merrpatenten.shared.feature.dashboard.ui.components.DisclaimerDialog
import com.arsildo.merrpatenten.shared.feature.dashboard.ui.components.ReadinessCard
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun DashboardRoute(
    viewModel: DashboardViewModel = koinViewModel(),
    onStartExamClick: (String) -> Unit,
    onPreferencesClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    onCatalogClick: () -> Unit,
    onMistakesClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DashboardScreen(
        uiState = uiState,
        onStartExamClick = onStartExamClick,
        onPreferencesClick = onPreferencesClick,
        onStatisticsClick = onStatisticsClick,
        onCatalogClick = onCatalogClick,
        onMistakesClick = onMistakesClick,
    )
}

@Composable
internal fun DashboardScreen(
    uiState: DashboardUiState,
    onStartExamClick: (String) -> Unit,
    onPreferencesClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    onCatalogClick: () -> Unit,
    onMistakesClick: () -> Unit,
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
                colors = CardDefaults.cardColors(
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
                colors = CardDefaults.cardColors(
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
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                ),
            )

            // 2. Second Major Category: Study & Learning Materials
            SectionHeader(title = stringResource(Res.string.dashboard_section_learning))

            // Readiness Score Banner
            ReadinessCard(
                readinessScore = uiState.readinessScore,
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onStatisticsClick()
                },
            )

            // Mistake Notebook Card ("Gabimet e Mia")
            val mistakeDesc = if (uiState.mistakesCount > 0) {
                if (uiState.mistakesCount == 1) {
                    stringResource(Res.string.mistakes_badge_count_singular)
                } else {
                    stringResource(Res.string.mistakes_badge_count, uiState.mistakesCount)
                } + " • " + stringResource(Res.string.mistakes_dashboard_desc)
            } else {
                stringResource(Res.string.mistakes_dashboard_desc)
            }

            ExamTypeCard(
                title = stringResource(Res.string.mistakes_dashboard_title),
                description = mistakeDesc,
                icon = Icons.Rounded.EditNote,
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    onMistakesClick()
                },
                colors = CardDefaults.cardColors(
                    containerColor = if (uiState.mistakesCount > 0) {
                        MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.45f)
                    } else {
                        MaterialTheme.colorScheme.surfaceContainerHigh
                    },
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ),
            )

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
                colors = CardDefaults.cardColors(
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
                colors = CardDefaults.cardColors(
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
        uiState = DashboardUiState(
            readinessScore = ReadinessScore(
                percentage = 85,
                level = ReadinessLevel.READY,
                completedExams = 8,
                passedExams = 7,
                passRate = 88,
                averageErrors = 2.1,
            ),
            mistakesCount = 5,
        ),
        onStartExamClick = {},
        onPreferencesClick = {},
        onStatisticsClick = {},
        onCatalogClick = {},
        onMistakesClick = {},
    )
}

@Preview
@Composable
private fun DashboardScreenDarkPreview() = MerrPatentenTheme(darkTheme = true) {
    DashboardScreen(
        uiState = DashboardUiState(
            readinessScore = ReadinessScore(
                percentage = 40,
                level = ReadinessLevel.NEEDS_PRACTICE,
                completedExams = 4,
                passedExams = 1,
                passRate = 25,
                averageErrors = 6.0,
            ),
            mistakesCount = 0,
        ),
        onStartExamClick = {},
        onPreferencesClick = {},
        onStatisticsClick = {},
        onCatalogClick = {},
        onMistakesClick = {},
    )
}
