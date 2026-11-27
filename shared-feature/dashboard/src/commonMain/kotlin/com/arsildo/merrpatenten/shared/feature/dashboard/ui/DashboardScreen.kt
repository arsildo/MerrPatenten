package com.arsildo.merrpatenten.shared.feature.dashboard.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arsildo.merrpatenten.shared.core.data.ReadinessLevel
import com.arsildo.merrpatenten.shared.core.data.ReadinessScore
import com.arsildo.merrpatenten.shared.core.designsystem.DPSHTRR_HELP
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.adaptive.rememberStartToolbarClearance
import com.arsildo.merrpatenten.shared.core.designsystem.adaptive.rememberWindowAdaptiveInfo
import com.arsildo.merrpatenten.shared.core.designsystem.adaptive.startPadding
import com.arsildo.merrpatenten.shared.core.designsystem.components.ExamTypeCard
import com.arsildo.merrpatenten.shared.core.designsystem.components.HelpfulMaterialCard
import com.arsildo.merrpatenten.shared.core.designsystem.components.SectionHeader
import com.arsildo.merrpatenten.shared.core.designsystem.icons.BookBookmark
import com.arsildo.merrpatenten.shared.core.designsystem.icons.BookOpen
import com.arsildo.merrpatenten.shared.core.designsystem.icons.Bus
import com.arsildo.merrpatenten.shared.core.designsystem.icons.Car
import com.arsildo.merrpatenten.shared.core.designsystem.icons.Info
import com.arsildo.merrpatenten.shared.core.designsystem.icons.Star
import com.arsildo.merrpatenten.shared.core.designsystem.icons.Timer
import com.arsildo.merrpatenten.shared.core.designsystem.icons.Truck
import com.arsildo.merrpatenten.shared.feature.dashboard.DashboardUiState
import com.arsildo.merrpatenten.shared.feature.dashboard.DashboardViewModel
import com.arsildo.merrpatenten.shared.feature.dashboard.ui.components.ReadinessCard
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun DashboardRoute(
    viewModel: DashboardViewModel = koinViewModel(),
    onStartExamClick: (category: String) -> Unit,
    onStartBlitzExamClick: (category: String) -> Unit,
    onPreferencesClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    onCatalogClick: () -> Unit,
    onMistakesClick: () -> Unit,
    onBookmarksClick: () -> Unit = {},
    onDisclaimerClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DashboardScreen(
        uiState = uiState,
        onStartExamClick = onStartExamClick,
        onStartBlitzExamClick = onStartBlitzExamClick,
        onPreferencesClick = onPreferencesClick,
        onStatisticsClick = onStatisticsClick,
        onCatalogClick = onCatalogClick,
        onMistakesClick = onMistakesClick,
        onBookmarksClick = onBookmarksClick,
        onDisclaimerClick = onDisclaimerClick,
    )
}

@Composable
internal fun DashboardScreen(
    modifier: Modifier = Modifier,
    onMistakesClick: () -> Unit,
    onBookmarksClick: () -> Unit = {},
    uiState: DashboardUiState,
    onStartExamClick: (category: String) -> Unit,
    onStartBlitzExamClick: (category: String) -> Unit,
    onPreferencesClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    onCatalogClick: () -> Unit,
    onDisclaimerClick: () -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val adaptiveInfo = rememberWindowAdaptiveInfo()
    val isMultiColumn = adaptiveInfo.isDualPane
    val startToolbarClearance = rememberStartToolbarClearance(adaptiveInfo.isCompact)

    val windowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)

    Scaffold(
        modifier = modifier.startPadding { startToolbarClearance.value },
        topBar = {
            TopAppBar(
                windowInsets = windowInsets,
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
                actions = {
                    IconButton(
                        onClick = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onDisclaimerClick()
                        },
                    ) {
                        Icon(
                            imageVector = Info,
                            contentDescription = stringResource(Res.string.info),
                        )
                    }
                },
            )
        },
        contentWindowInsets = windowInsets,
    ) { contentPadding ->
        if (isMultiColumn) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .consumeWindowInsets(contentPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
                    .padding(top = 20.dp, bottom = 24.dp)
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                ExamSection(
                    onStartExamClick = onStartExamClick,
                    modifier = Modifier.weight(1f),
                )
                LearningSection(
                    uiState = uiState,
                    onStatisticsClick = onStatisticsClick,
                    onMistakesClick = onMistakesClick,
                    onBookmarksClick = onBookmarksClick,
                    onCatalogClick = onCatalogClick,
                    onStartBlitzExamClick = onStartBlitzExamClick,
                    modifier = Modifier.weight(1f),
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .consumeWindowInsets(contentPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
                    .padding(top = 32.dp, bottom = 128.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                ExamSection(onStartExamClick = onStartExamClick)
                LearningSection(
                    uiState = uiState,
                    onStatisticsClick = onStatisticsClick,
                    onMistakesClick = onMistakesClick,
                    onBookmarksClick = onBookmarksClick,
                    onCatalogClick = onCatalogClick,
                    onStartBlitzExamClick = onStartBlitzExamClick,
                )
            }
        }
    }
}

@Composable
private fun ExamSection(modifier: Modifier = Modifier, onStartExamClick: (category: String) -> Unit) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SectionHeader(title = stringResource(Res.string.dashboard_section_exams))

        ExamTypeCard(
            title = stringResource(Res.string.questionnaire_category_one),
            description = stringResource(Res.string.category_b_desc),
            icon = Car,
            onClick = { onStartExamClick("B") },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
        )

        ExamTypeCard(
            title = stringResource(Res.string.questionnaire_category_two),
            description = stringResource(Res.string.category_cd_desc),
            icon = Truck,
            onClick = { onStartExamClick("C") },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            ),
        )

        ExamTypeCard(
            title = stringResource(Res.string.questionnaire_category_three),
            description = stringResource(Res.string.category_cd_desc),
            icon = Bus,
            onClick = { onStartExamClick("D") },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            ),
        )
    }
}

@Composable
private fun LearningSection(
    modifier: Modifier = Modifier,
    uiState: DashboardUiState,
    onStatisticsClick: () -> Unit,
    onMistakesClick: () -> Unit,
    onBookmarksClick: () -> Unit,
    onCatalogClick: () -> Unit,
    onStartBlitzExamClick: (category: String) -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SectionHeader(title = stringResource(Res.string.dashboard_section_learning))

        ReadinessCard(
            readinessScore = uiState.readinessScore,
            onClick = onStatisticsClick,
        )

        ExamTypeCard(
            title = stringResource(Res.string.blitz_exam_title),
            description = stringResource(Res.string.blitz_exam_desc),
            icon = Timer,
            onClick = { onStartBlitzExamClick("B") },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    .copy(alpha = 0.35f)
                    .compositeOver(MaterialTheme.colorScheme.surfaceContainerHigh),
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
        )

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
            icon = BookBookmark,
            onClick = onMistakesClick,
            colors = CardDefaults.cardColors(
                containerColor = if (uiState.mistakesCount > 0) {
                    MaterialTheme.colorScheme.errorContainer
                        .copy(alpha = 0.45f)
                        .compositeOver(MaterialTheme.colorScheme.surfaceContainerHigh)
                } else {
                    MaterialTheme.colorScheme.surfaceContainerHigh
                },
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
        )

        val bookmarkDesc = if (uiState.bookmarksCount > 0) {
            if (uiState.bookmarksCount == 1) {
                stringResource(Res.string.bookmarks_badge_count_singular)
            } else {
                stringResource(Res.string.bookmarks_badge_count, uiState.bookmarksCount)
            } + " • " + stringResource(Res.string.bookmarks_dashboard_desc)
        } else {
            stringResource(Res.string.bookmarks_dashboard_desc)
        }

        ExamTypeCard(
            title = stringResource(Res.string.bookmarks_dashboard_title),
            description = bookmarkDesc,
            icon = Star,
            onClick = onBookmarksClick,
            colors = CardDefaults.cardColors(
                containerColor = if (uiState.bookmarksCount > 0) {
                    MaterialTheme.colorScheme.primaryContainer
                        .copy(alpha = 0.45f)
                        .compositeOver(MaterialTheme.colorScheme.surfaceContainerHigh)
                } else {
                    MaterialTheme.colorScheme.surfaceContainerHigh
                },
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
        )

        ExamTypeCard(
            title = stringResource(Res.string.catalog_dashboard_title),
            description =
                stringResource(Res.string.catalog_category_warning) + " | " + stringResource(Res.string.catalog_category_prohibitory) +
                    " | " +
                    stringResource(Res.string.catalog_category_intersections),
            icon = BookOpen,
            onClick = onCatalogClick,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
        )

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

@PreviewScreenSizes
@PreviewLightDark
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
        onStartBlitzExamClick = {},
        onPreferencesClick = {},
        onStatisticsClick = {},
        onCatalogClick = {},
        onMistakesClick = {},
        onDisclaimerClick = {},
    )
}
