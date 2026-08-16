package com.arsildo.merrpatenten.shared.feature.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arsildo.merrpatenten.shared.core.designsystem.*
import com.arsildo.merrpatenten.shared.core.designsystem.components.PreferenceCard
import com.arsildo.merrpatenten.shared.core.designsystem.components.SectionHeader
import com.arsildo.merrpatenten.shared.core.designsystem.components.TextSizePreferenceCard
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun PreferencesRoute(
    viewModel: PreferencesViewModel = koinViewModel(),
    onBackPress: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PreferencesScreen(
        uiState = uiState,
        onBackPress = onBackPress,
        onImmersiveModeChange = viewModel::setImmersiveMode,
        onSaveStatsChange = viewModel::setSaveStats,
        onFollowSystemChange = viewModel::setFollowSystem,
        onColorSchemeChange = viewModel::setColorScheme,
        onDynamicColorSchemeChange = viewModel::setDynamicColorScheme,
        onQuestionTextSizeChange = viewModel::setQuestionTextSize,
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun PreferencesScreen(
    uiState: PreferencesUiState,
    onBackPress: () -> Unit,
    onImmersiveModeChange: (Boolean) -> Unit,
    onSaveStatsChange: (Boolean) -> Unit,
    onFollowSystemChange: (Boolean) -> Unit,
    onColorSchemeChange: (Boolean) -> Unit,
    onDynamicColorSchemeChange: (Boolean) -> Unit,
    onQuestionTextSizeChange: (QuestionTextSize) -> Unit,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current
    val currentLocale = rememberApplicationLocale()
    var languageDialogVisible by remember { mutableStateOf(false) }

    AnimateColorSchemeTransition {
        Scaffold(
            modifier = modifier,
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = stringResource(Res.string.preferences))
                    },
                    navigationIcon = {
                        FilledTonalIconButton(
                            onClick = onBackPress,
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = stringResource(Res.string.back)
                            )
                        }
                    },
                    actions = {
                        FilledTonalIconButton(
                            onClick = { languageDialogVisible = true },
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Language,
                                contentDescription = stringResource(Res.string.language)
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        uriHandler.openUri(GITHUB_URL)
                    },
                    text = {
                        Text(
                            text = stringResource(Res.string.github),
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    icon = {
                        Icon(
                            painter = painterResource(Res.drawable.github),
                            contentDescription = null,
                            modifier = Modifier.size(28.dp)
                        )
                    },
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 2.dp),
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    shape = MaterialTheme.shapes.large
                )
            },
            contentColor = MaterialTheme.colorScheme.onBackground
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Section 1: Behavior
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SectionHeader(title = stringResource(Res.string.pref_section_behavior))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
                    ) {
                        PreferenceCard(
                            title = stringResource(Res.string.preferences_navigation_buttons),
                            subtitle = stringResource(Res.string.preferences_navigation_buttons_desc),
                            checked = uiState.immersiveMode,
                            index = 0,
                            count = 2,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.TouchApp,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            onCheckedChange = onImmersiveModeChange
                        )
                        PreferenceCard(
                            title = stringResource(Res.string.preferences_store_stats),
                            subtitle = stringResource(Res.string.preferences_store_stats_desc),
                            checked = uiState.saveStats,
                            index = 1,
                            count = 2,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Insights,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            onCheckedChange = onSaveStatsChange
                        )
                    }
                }

                // Section 2: Appearance
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SectionHeader(title = stringResource(Res.string.pref_section_appearance))

                    val appearanceCount = (if (supportsDynamicColor) 3 else 2) + 1

                    Column(
                        verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
                    ) {
                        PreferenceCard(
                            title = stringResource(Res.string.preferences_follow_system_theme),
                            subtitle = stringResource(Res.string.preferences_follow_system_theme_desc),
                            checked = uiState.followSystemColors,
                            index = 0,
                            count = appearanceCount,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.BrightnessAuto,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            onCheckedChange = onFollowSystemChange
                        )
                        PreferenceCard(
                            title = stringResource(Res.string.preferences_dark_mode),
                            subtitle = stringResource(Res.string.preferences_dark_mode_desc),
                            checked = uiState.colorScheme,
                            enabled = !uiState.followSystemColors,
                            index = 1,
                            count = appearanceCount,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.DarkMode,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            onCheckedChange = onColorSchemeChange
                        )
                        if (supportsDynamicColor) {
                            PreferenceCard(
                                title = stringResource(Res.string.preferences_material_you),
                                subtitle = stringResource(Res.string.preferences_material_you_desc),
                                checked = uiState.dynamicColorScheme,
                                index = 2,
                                count = appearanceCount,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Palette,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                },
                                onCheckedChange = onDynamicColorSchemeChange
                            )
                        }
                        TextSizePreferenceCard(
                            selectedSize = uiState.questionTextSize,
                            onSizeSelected = onQuestionTextSizeChange,
                            index = if (supportsDynamicColor) 3 else 2,
                            count = appearanceCount
                        )
                    }
                }
            }

            if (languageDialogVisible) {
                AlertDialog(
                    onDismissRequest = { languageDialogVisible = false },
                    title = {
                        Text(
                            text = stringResource(Res.string.language),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    text = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ApplicationLocale.entries.forEach { locale ->
                                Surface(
                                    shape = MaterialTheme.shapes.medium,
                                    color = if (currentLocale == locale) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            ApplicationLocaleManager.setLocale(locale)
                                            languageDialogVisible = false
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 14.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = stringResource(locale.res),
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = if (currentLocale == locale) FontWeight.Bold else FontWeight.Normal,
                                            color = if (currentLocale == locale) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                        )
                                        RadioButton(
                                            selected = currentLocale == locale,
                                            onClick = {
                                                ApplicationLocaleManager.setLocale(locale)
                                                languageDialogVisible = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { languageDialogVisible = false }) {
                            Text(text = stringResource(Res.string.close))
                        }
                    }
                )
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
            onFollowSystemChange = {},
            onColorSchemeChange = {},
            onDynamicColorSchemeChange = {},
            onQuestionTextSizeChange = {},
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
            onFollowSystemChange = {},
            onColorSchemeChange = {},
            onDynamicColorSchemeChange = {},
            onQuestionTextSizeChange = {},
        )
    }
}
