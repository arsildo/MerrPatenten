package com.arsildo.merrpatenten.shared.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.AutoGraph
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
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
        val statisticsSubStack = navigator.state.subStacks[Statistics] ?: error("Statistics stack missing")
        val preferencesSubStack = navigator.state.subStacks[Preferences] ?: error("Preferences stack missing")

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
                    backStack = statisticsSubStack,
                    onBackPress = { navigator.goBack() },
                    onChangePreferenceClick = { navigator.navigate(Preferences) },
                )
                preferencesEntry(
                    backStack = preferencesSubStack,
                    onBackPress = { navigator.goBack() },
                )
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
                NavigationRail(
                    modifier = Modifier.fillMaxHeight(),
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                ) {
                    Spacer(Modifier.height(16.dp))
                    topLevelDestinations.forEach { (key, icon, labelRes) ->
                        NavigationRailItem(
                            selected = currentTopLevel == key,
                            onClick = {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                navigator.navigate(key)
                            },
                            icon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = stringResource(labelRes),
                                )
                            },
                            label = {
                                Text(
                                    text = stringResource(labelRes),
                                    style = MaterialTheme.typography.labelMedium,
                                )
                            },
                        )
                    }
                }
            }
            Scaffold(
                bottomBar = {
                    AnimatedVisibility(
                        visible = adaptiveInfo.isCompact && showNavigation,
                        enter = slideInVertically { it },
                        exit = slideOutVertically { it },
                    ) {
                        NavigationBar(
                            modifier = Modifier.fillMaxWidth(),
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        ) {
                            topLevelDestinations.forEach { dest ->
                                NavigationBarItem(
                                    selected = currentTopLevel == dest.key,
                                    onClick = {
                                        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        navigator.navigate(dest.key)
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = dest.icon,
                                            contentDescription = stringResource(dest.labelRes),
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = stringResource(dest.labelRes),
                                            style = MaterialTheme.typography.labelSmall,
                                        )
                                    },
                                )
                            }
                        }
                    }
                },
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                ) {
                    NavDisplay(
                        entries = entries,
                        sceneStrategies = listOf(
                            remember { BottomSheetSceneStrategy() },
                            listDetailStrategy,
                        ),
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
                }
            }
        }
    }
}
