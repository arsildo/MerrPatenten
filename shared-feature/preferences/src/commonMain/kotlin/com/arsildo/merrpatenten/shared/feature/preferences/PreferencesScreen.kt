package com.arsildo.merrpatenten.shared.feature.preferences

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arsildo.merrpatenten.shared.core.designsystem.AnimateColorSchemeTransition
import com.arsildo.merrpatenten.shared.core.designsystem.GITHUB_URL
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.components.PreferenceCard
import merrpatenten.shared_core.design_system.generated.resources.Res
import merrpatenten.shared_core.design_system.generated.resources.github
import merrpatenten.shared_core.design_system.generated.resources.preferences
import merrpatenten.shared_core.design_system.generated.resources.preferences_dark_mode
import merrpatenten.shared_core.design_system.generated.resources.preferences_dark_mode_desc
import merrpatenten.shared_core.design_system.generated.resources.preferences_double_press
import merrpatenten.shared_core.design_system.generated.resources.preferences_double_press_desc
import merrpatenten.shared_core.design_system.generated.resources.preferences_follow_system_theme
import merrpatenten.shared_core.design_system.generated.resources.preferences_follow_system_theme_desc
import merrpatenten.shared_core.design_system.generated.resources.preferences_material_you
import merrpatenten.shared_core.design_system.generated.resources.preferences_material_you_desc
import merrpatenten.shared_core.design_system.generated.resources.preferences_navigation_buttons
import merrpatenten.shared_core.design_system.generated.resources.preferences_navigation_buttons_desc
import merrpatenten.shared_core.design_system.generated.resources.preferences_store_stats
import merrpatenten.shared_core.design_system.generated.resources.preferences_store_stats_desc
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PreferencesRoute(
    onBackPress: () -> Unit,
    viewModel: PreferencesViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PreferencesScreen(
        uiState = uiState,
        onBackPress = onBackPress,
        onImmersiveModeChange = viewModel::setImmersiveMode,
        onSaveStatsChange = viewModel::setSaveStats,
        onConfirmAppExitChange = viewModel::setConfirmAppExit,
        onFollowSystemChange = viewModel::setFollowSystem,
        onColorSchemeChange = viewModel::setColorScheme,
        onDynamicColorSchemeChange = viewModel::setDynamicColorScheme,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesScreen(
    uiState: PreferencesUiState,
    onBackPress: () -> Unit,
    onImmersiveModeChange: (Boolean) -> Unit,
    onSaveStatsChange: (Boolean) -> Unit,
    onConfirmAppExitChange: (Boolean) -> Unit,
    onFollowSystemChange: (Boolean) -> Unit,
    onColorSchemeChange: (Boolean) -> Unit,
    onDynamicColorSchemeChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current

    AnimateColorSchemeTransition {
        Scaffold(
            modifier = modifier,
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(Res.string.preferences)) },
                    navigationIcon = {
                        IconButton(onClick = onBackPress) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        titleContentColor = MaterialTheme.colorScheme.onSecondary,
                        actionIconContentColor = MaterialTheme.colorScheme.onSecondary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSecondary,
                    ),
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        uriHandler.openUri(GITHUB_URL)
                    },
                    text = { Text(text = "Github") },
                    icon = {
                        Icon(
                            painter = painterResource(Res.drawable.github),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                    },
                    elevation = FloatingActionButtonDefaults.loweredElevation(),
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    shape = MaterialTheme.shapes.extraLarge
                )
            },
            contentColor = MaterialTheme.colorScheme.primary
        ) { contentPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 32.dp)
                ) {
                    PreferenceCard(
                        title = stringResource(Res.string.preferences_navigation_buttons),
                        subtitle = stringResource(Res.string.preferences_navigation_buttons_desc),
                        checked = uiState.immersiveMode,
                        onCheckedChange = onImmersiveModeChange
                    )
                    PreferenceCard(
                        title = stringResource(Res.string.preferences_store_stats),
                        subtitle = stringResource(Res.string.preferences_store_stats_desc),
                        checked = uiState.saveStats,
                        onCheckedChange = onSaveStatsChange
                    )
                    PreferenceCard(
                        title = stringResource(Res.string.preferences_double_press),
                        subtitle = stringResource(Res.string.preferences_double_press_desc),
                        checked = uiState.confirmAppExit,
                        onCheckedChange = onConfirmAppExitChange
                    )
                    PreferenceCard(
                        title = stringResource(Res.string.preferences_follow_system_theme),
                        subtitle = stringResource(Res.string.preferences_follow_system_theme_desc),
                        checked = uiState.followSystemColors,
                        onCheckedChange = onFollowSystemChange
                    )
                    PreferenceCard(
                        title = stringResource(Res.string.preferences_dark_mode),
                        subtitle = stringResource(Res.string.preferences_dark_mode_desc),
                        checked = uiState.colorScheme,
                        enabled = !uiState.followSystemColors,
                        onCheckedChange = onColorSchemeChange
                    )
                    PreferenceCard(
                        title = stringResource(Res.string.preferences_material_you),
                        subtitle = stringResource(Res.string.preferences_material_you_desc),
                        checked = uiState.dynamicColorScheme,
                        onCheckedChange = onDynamicColorSchemeChange
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreferencesScreenPreview() {
    MerrPatentenTheme {
        PreferencesScreen(
            uiState = PreferencesUiState(),
            onBackPress = {},
            onImmersiveModeChange = {},
            onSaveStatsChange = {},
            onConfirmAppExitChange = {},
            onFollowSystemChange = {},
            onColorSchemeChange = {},
            onDynamicColorSchemeChange = {},
        )
    }
}

@Preview
@Composable
private fun PreferencesScreenDarkPreview() {
    MerrPatentenTheme(darkTheme = true) {
        PreferencesScreen(
            uiState = PreferencesUiState(followSystemColors = false, colorScheme = true),
            onBackPress = {},
            onImmersiveModeChange = {},
            onSaveStatsChange = {},
            onConfirmAppExitChange = {},
            onFollowSystemChange = {},
            onColorSchemeChange = {},
            onDynamicColorSchemeChange = {},
        )
    }
}

