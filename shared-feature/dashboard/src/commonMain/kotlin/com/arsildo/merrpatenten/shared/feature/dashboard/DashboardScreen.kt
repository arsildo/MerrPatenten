package com.arsildo.merrpatenten.shared.feature.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.DPSHTRR_HELP
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.components.ExamTypeCard
import com.arsildo.merrpatenten.shared.core.designsystem.components.HelpfulMaterialCard
import com.arsildo.merrpatenten.shared.core.designsystem.components.SectionHeader
import kotlinx.coroutines.launch
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun DashboardRoute(
    onStartExamClick: (String) -> Unit,
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
internal fun DashboardScreen(
    onStartExamClick: (String) -> Unit,
    onPreferencesClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current
    var showDisclaimerDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
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
                }
            )
        },
        modifier = modifier,
        floatingActionButton = {
            HorizontalFloatingToolbar(
                expanded = true,
                modifier = Modifier.navigationBarsPadding(),
                colors = FloatingToolbarDefaults.vibrantFloatingToolbarColors()
            ) {
                IconButton(onClick = onStatisticsClick) {
                    Icon(Icons.Rounded.AutoGraph, stringResource(Res.string.statistics))
                }
                IconButton(onClick = onPreferencesClick) {
                    Icon(Icons.Rounded.Settings, stringResource(Res.string.preferences))
                }
                IconButton(onClick = { showDisclaimerDialog = true }) {
                    Icon(imageVector = Icons.Outlined.Info, contentDescription = stringResource(Res.string.info))
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        contentWindowInsets = WindowInsets(bottom = 0)
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp, bottom = 104.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // Categories Section Title
            SectionHeader(title = stringResource(Res.string.select_category))

            // Exam Category Cards
            ExamTypeCard(
                title = stringResource(Res.string.questionnaire_category_one),
                description = stringResource(Res.string.category_b_desc),
                icon = Icons.Rounded.DirectionsCar,
                onClick = { onStartExamClick("B") },
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
            )

            ExamTypeCard(
                title = stringResource(Res.string.questionnaire_category_two),
                description = stringResource(Res.string.category_cd_desc),
                icon = Icons.Rounded.LocalShipping,
                onClick = { onStartExamClick("C") },
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
            )

            ExamTypeCard(
                title = stringResource(Res.string.questionnaire_category_three),
                description = stringResource(Res.string.category_cd_desc),
                icon = Icons.Rounded.DirectionsBus,
                onClick = { onStartExamClick("D") },
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
