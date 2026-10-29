package com.arsildo.merrpatenten.shared.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.AutoGraph
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.defaultPopTransitionSpec
import androidx.navigation3.ui.defaultTransitionSpec
import androidx.savedstate.serialization.SavedStateConfiguration
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.adaptive.rememberAppAdaptiveInfo
import com.arsildo.merrpatenten.shared.feature.catalog.navigation.Catalog
import com.arsildo.merrpatenten.shared.feature.catalog.navigation.SignDetail
import com.arsildo.merrpatenten.shared.feature.catalog.navigation.catalogEntry
import com.arsildo.merrpatenten.shared.feature.dashboard.navigation.Dashboard
import com.arsildo.merrpatenten.shared.feature.dashboard.navigation.dashboardEntry
import com.arsildo.merrpatenten.shared.feature.exam.navigation.Exam
import com.arsildo.merrpatenten.shared.feature.exam.navigation.ExamResultBottomSheet
import com.arsildo.merrpatenten.shared.feature.exam.navigation.examEntry
import com.arsildo.merrpatenten.shared.feature.imagedetails.navigation.ZoomableImage
import com.arsildo.merrpatenten.shared.feature.imagedetails.navigation.imageDetailsEntry
import com.arsildo.merrpatenten.shared.feature.mistakes.navigation.Mistakes
import com.arsildo.merrpatenten.shared.feature.mistakes.navigation.MistakesPractice
import com.arsildo.merrpatenten.shared.feature.mistakes.navigation.mistakesEntry
import com.arsildo.merrpatenten.shared.feature.preferences.PreferencesViewModel
import com.arsildo.merrpatenten.shared.feature.preferences.navigation.Preferences
import com.arsildo.merrpatenten.shared.feature.preferences.navigation.preferencesEntry
import com.arsildo.merrpatenten.shared.feature.statistics.navigation.Statistics
import com.arsildo.merrpatenten.shared.feature.statistics.navigation.statisticsEntry
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import merrpatenten.shared_core.design_system.generated.resources.*
import navigation.BottomSheetSceneStrategy
import navigation.ListDetailSceneStrategy
import navigation.rememberNavigator
import navigation.toEntries
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private data class TopLevelDestination(val key: NavKey, val icon: ImageVector, val labelRes: StringResource)

private val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Dashboard::class, Dashboard.serializer())
            subclass(Exam::class, Exam.serializer())
            subclass(ExamResultBottomSheet::class, ExamResultBottomSheet.serializer())
            subclass(ZoomableImage::class, ZoomableImage.serializer())
            subclass(Statistics::class, Statistics.serializer())
            subclass(Preferences::class, Preferences.serializer())
            subclass(Catalog::class, Catalog.serializer())
            subclass(SignDetail::class, SignDetail.serializer())
            subclass(Mistakes::class, Mistakes.serializer())
            subclass(MistakesPractice::class, MistakesPractice.serializer())
        }
    }
}

@Composable
fun MerrPatentenApp() {
    val preferencesViewModel = koinViewModel<PreferencesViewModel>()
    val uiState by preferencesViewModel.uiState.collectAsStateWithLifecycle()
    val hapticFeedback = LocalHapticFeedback.current

    val isDark = if (uiState.followSystemColors) {
        isSystemInDarkTheme()
    } else {
        uiState.colorScheme
    }

    MerrPatentenTheme(
        darkTheme = isDark,
        hapticFeedback = uiState.hapticFeedback,
    ) {
        val adaptiveInfo = rememberAppAdaptiveInfo()
        val listDetailStrategy = remember(adaptiveInfo.isDualPane, adaptiveInfo.isBook) {
            ListDetailSceneStrategy<NavKey>(
                isDualPane = { adaptiveInfo.isDualPane },
                splitWeights = { adaptiveInfo.dualPaneSplitWeights(defaultPrimaryWeight = 0.42f) },
            )
        }

        val topLevelDestinations = remember {
            listOf(
                TopLevelDestination(
                    key = Dashboard,
                    icon = Icons.Rounded.Home,
                    labelRes = Res.string.nav_home,
                ),
                TopLevelDestination(
                    key = Catalog,
                    icon = Icons.AutoMirrored.Rounded.MenuBook,
                    labelRes = Res.string.nav_catalog,
                ),
                TopLevelDestination(
                    key = Mistakes,
                    icon = Icons.Rounded.EditNote,
                    labelRes = Res.string.nav_mistakes,
                ),
                TopLevelDestination(
                    key = Statistics,
                    icon = Icons.Rounded.AutoGraph,
                    labelRes = Res.string.nav_statistics,
                ),
                TopLevelDestination(
                    key = Preferences,
                    icon = Icons.Rounded.Settings,
                    labelRes = Res.string.nav_preferences,
                ),
            )
        }

        val topLevelKeys = remember { topLevelDestinations.map { it.key }.toSet() }
        val topLevelIndexByContentKey = remember {
            topLevelDestinations.mapIndexed { index, dest ->
                "${dest.key}:${dest.key::class}" to index
            }.toMap()
        }

        val navigator = rememberNavigator(
            startKey = Dashboard,
            topLevelKeys = topLevelKeys,
            configuration = config,
        )

        val currentTopLevel = navigator.currentTab ?: Dashboard
        val isFullscreen = navigator.state.currentSubStack?.any { it is Exam || it is ZoomableImage } == true ||
            navigator.currentKey is Exam || navigator.currentKey is ExamResultBottomSheet || navigator.currentKey is ZoomableImage
        val showNavigation = !isFullscreen

        val dashboardSubStack = navigator.state.subStacks[Dashboard] ?: error("Dashboard stack missing")
        val catalogSubStack = navigator.state.subStacks[Catalog] ?: error("Catalog stack missing")
        val mistakesSubStack = navigator.state.subStacks[Mistakes] ?: error("Mistakes stack missing")

        val entries = navigator.state.toEntries(
            entryProvider = entryProvider {
                dashboardEntry(
                    backStack = dashboardSubStack,
                    onStartExamClick = { category -> navigator.navigate(Exam(category)) },
                    onStatisticsClick = { navigator.navigate(Statistics) },
                    onPreferencesClick = { navigator.navigate(Preferences) },
                    onCatalogClick = { navigator.navigate(Catalog) },
                    onMistakesClick = { navigator.navigate(Mistakes) },
                )
                catalogEntry(
                    backStack = catalogSubStack,
                    onBackPress = { navigator.goBack() },
                    onImageDetailsClick = { imageId -> navigator.navigate(ZoomableImage(imageId)) },
                )
                mistakesEntry(
                    backStack = mistakesSubStack,
                    onBackPress = { navigator.goBack() },
                    onImageDetailsClick = { imageId -> navigator.navigate(ZoomableImage(imageId)) },
                )
                examEntry(
                    backStack = dashboardSubStack,
                    onImageDetailsClick = { imageId -> navigator.navigate(ZoomableImage(imageId)) },
                    onExitExam = { navigator.popToRoot(Dashboard) },
                    onRestartExam = { category ->
                        navigator.navigate(Exam(category), popCurrent = true)
                    },
                )
                imageDetailsEntry(
                    backStack = navigator.state.currentSubStack ?: dashboardSubStack,
                    onDismiss = { navigator.goBack() },
                )
                statisticsEntry(
                    onChangePreferenceClick = { navigator.navigate(Preferences) },
                )
                preferencesEntry()
            },
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            AnimatedVisibility(
                visible = !adaptiveInfo.isCompact && showNavigation,
                enter = slideInHorizontally { -it },
                exit = slideOutHorizontally { -it },
            ) {
                VerticalFloatingToolbar(
                    expanded = true,
                    modifier = Modifier
                        .systemBarsPadding()
                        .padding(16.dp)
                        .selectableGroup(),
                ) {
                    topLevelDestinations.forEach { (key, icon, labelRes) ->
                        val selected = currentTopLevel == key
                        val label = stringResource(labelRes)
                        VerticalFloatingToolbarNavItem(
                            selected = selected,
                            icon = icon,
                            label = label,
                            onClick = {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                navigator.navigate(key)
                            },
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            ) {
                NavDisplay(
                    entries = entries,
                    sceneStrategies = listOf(
                        remember { BottomSheetSceneStrategy() },
                        listDetailStrategy,
                    ),
                    transitionSpec = {
                        val initialIndex = topLevelIndexByContentKey[initialState.entries.lastOrNull()?.contentKey]
                        val targetIndex = topLevelIndexByContentKey[targetState.entries.lastOrNull()?.contentKey]
                        if (initialIndex != null && targetIndex != null && initialIndex != targetIndex) {
                            if (adaptiveInfo.isCompact) {
                                if (targetIndex > initialIndex) {
                                    slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
                                } else {
                                    slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
                                }
                            } else {
                                fadeIn() togetherWith fadeOut()
                            }
                        } else {
                            defaultTransitionSpec<NavKey>()(this)
                        }
                    },
                    popTransitionSpec = {
                        val initialIndex = topLevelIndexByContentKey[initialState.entries.lastOrNull()?.contentKey]
                        val targetIndex = topLevelIndexByContentKey[targetState.entries.lastOrNull()?.contentKey]
                        if (initialIndex != null && targetIndex != null && initialIndex != targetIndex) {
                            if (adaptiveInfo.isCompact) {
                                if (targetIndex > initialIndex) {
                                    slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
                                } else {
                                    slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
                                }
                            } else {
                                fadeIn() togetherWith fadeOut()
                            }
                        } else {
                            defaultPopTransitionSpec<NavKey>()(this)
                        }
                    },
                    onBack = dropUnlessResumed {
                        if (navigator.canGoBack) {
                            navigator.goBack()
                        } else {
                            navigator.popRoot()
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                )

                androidx.compose.animation.AnimatedVisibility(
                    visible = adaptiveInfo.isCompact && showNavigation,
                    enter = slideInVertically { it },
                    exit = slideOutVertically { it },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .zIndex(1f),
                ) {
                    HorizontalFloatingToolbar(
                        expanded = true,
                        modifier = Modifier
                            .navigationBarsPadding()
                            .padding(bottom = 16.dp)
                            .selectableGroup(),
                    ) {
                        topLevelDestinations.forEach { (key, icon, labelRes) ->
                            val selected = currentTopLevel == key
                            val label = stringResource(labelRes)
                            FloatingToolbarNavItem(
                                selected = selected,
                                icon = icon,
                                label = label,
                                onClick = {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    navigator.navigate(key)
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FloatingToolbarNavItem(
    selected: Boolean,
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            Color.Transparent
        },
        label = "FloatingToolbarItemContainer",
    )
    val contentColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.onSecondaryContainer
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        label = "FloatingToolbarItemContent",
    )

    Row(
        modifier = modifier
            .height(48.dp)
            .clip(CircleShape)
            .background(containerColor)
            .clickable(onClick = onClick)
            .semantics {
                role = Role.Tab
                this.selected = selected
            }
            .padding(horizontal = if (selected) 16.dp else 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = contentColor,
            modifier = Modifier.size(24.dp),
        )
        AnimatedVisibility(
            visible = selected,
            enter = expandHorizontally(expandFrom = Alignment.Start) + fadeIn(),
            exit = shrinkHorizontally(shrinkTowards = Alignment.Start) + fadeOut(),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(Modifier.width(8.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun VerticalFloatingToolbarNavItem(
    selected: Boolean,
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            Color.Transparent
        },
        label = "VerticalFloatingToolbarItemContainer",
    )
    val contentColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.onSecondaryContainer
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        label = "VerticalFloatingToolbarItemContent",
    )

    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(containerColor)
            .clickable(onClick = onClick)
            .semantics {
                role = Role.Tab
                this.selected = selected
            },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = contentColor,
            modifier = Modifier.size(24.dp),
        )
    }
}
