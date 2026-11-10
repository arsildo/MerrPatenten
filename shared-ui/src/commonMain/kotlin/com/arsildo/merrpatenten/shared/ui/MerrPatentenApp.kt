package com.arsildo.merrpatenten.shared.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.defaultPopTransitionSpec
import androidx.navigation3.ui.defaultTransitionSpec
import androidx.savedstate.serialization.SavedStateConfiguration
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.adaptive.rememberAppAdaptiveInfo
import com.arsildo.merrpatenten.shared.core.designsystem.icons.BookBookmark
import com.arsildo.merrpatenten.shared.core.designsystem.icons.BookOpen
import com.arsildo.merrpatenten.shared.core.designsystem.icons.GraphStar
import com.arsildo.merrpatenten.shared.core.designsystem.icons.House
import com.arsildo.merrpatenten.shared.core.designsystem.icons.Settings
import com.arsildo.merrpatenten.shared.feature.catalog.navigation.Catalog
import com.arsildo.merrpatenten.shared.feature.catalog.navigation.SignDetail
import com.arsildo.merrpatenten.shared.feature.catalog.navigation.catalogEntry
import com.arsildo.merrpatenten.shared.feature.dashboard.navigation.Dashboard
import com.arsildo.merrpatenten.shared.feature.dashboard.navigation.Disclaimer
import com.arsildo.merrpatenten.shared.feature.dashboard.navigation.dashboardEntry
import com.arsildo.merrpatenten.shared.feature.exam.navigation.Exam
import com.arsildo.merrpatenten.shared.feature.exam.navigation.ExamResultBottomSheet
import com.arsildo.merrpatenten.shared.feature.exam.navigation.ExamType
import com.arsildo.merrpatenten.shared.feature.exam.navigation.ExitExamBottomSheet
import com.arsildo.merrpatenten.shared.feature.exam.navigation.examEntry
import com.arsildo.merrpatenten.shared.feature.imagedetails.navigation.ZoomableImage
import com.arsildo.merrpatenten.shared.feature.imagedetails.navigation.imageDetailsEntry
import com.arsildo.merrpatenten.shared.feature.mistakes.navigation.Mistakes
import com.arsildo.merrpatenten.shared.feature.mistakes.navigation.MistakesPractice
import com.arsildo.merrpatenten.shared.feature.mistakes.navigation.mistakesEntry
import com.arsildo.merrpatenten.shared.feature.preferences.PreferencesViewModel
import com.arsildo.merrpatenten.shared.feature.preferences.navigation.LanguageSelection
import com.arsildo.merrpatenten.shared.feature.preferences.navigation.Preferences
import com.arsildo.merrpatenten.shared.feature.preferences.navigation.preferencesEntry
import com.arsildo.merrpatenten.shared.feature.statistics.navigation.Statistics
import com.arsildo.merrpatenten.shared.feature.statistics.navigation.statisticsEntry
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
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
            subclass(Disclaimer::class, Disclaimer.serializer())
            subclass(Exam::class, Exam.serializer())
            subclass(ExamResultBottomSheet::class, ExamResultBottomSheet.serializer())
            subclass(ExitExamBottomSheet::class, ExitExamBottomSheet.serializer())
            subclass(ZoomableImage::class, ZoomableImage.serializer())
            subclass(Statistics::class, Statistics.serializer())
            subclass(Preferences::class, Preferences.serializer())
            subclass(LanguageSelection::class, LanguageSelection.serializer())
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

    val isDark = if (uiState.followSystemColors) {
        isSystemInDarkTheme()
    } else {
        uiState.colorScheme
    }

    MerrPatentenTheme(
        darkTheme = isDark,
        dynamicColor = uiState.dynamicColorScheme,
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
                    icon = House,
                    labelRes = Res.string.nav_home,
                ),
                TopLevelDestination(
                    key = Catalog,
                    icon = BookOpen,
                    labelRes = Res.string.nav_catalog,
                ),
                TopLevelDestination(
                    key = Mistakes,
                    icon = BookBookmark,
                    labelRes = Res.string.nav_mistakes,
                ),
                TopLevelDestination(
                    key = Statistics,
                    icon = GraphStar,
                    labelRes = Res.string.nav_statistics,
                ),
                TopLevelDestination(
                    key = Preferences,
                    icon = Settings,
                    labelRes = Res.string.nav_preferences,
                ),
            )
        }

        val topLevelKeys = remember { topLevelDestinations.map { it.key }.toSet() }
        val topLevelIndexByContentKey = remember {
            topLevelDestinations.mapIndexed { index, dest -> "${dest.key}:${dest.key::class}" to index }.toMap()
        }

        val navigator = rememberNavigator(
            startKey = Dashboard,
            topLevelKeys = topLevelKeys,
            configuration = config,
        )

        val currentTopLevel = navigator.currentTab ?: Dashboard
        val showFloatingToolbar = when (navigator.currentKey) {
            is Exam, is ExamResultBottomSheet, is ExitExamBottomSheet, is ZoomableImage -> false
            else -> navigator.state.currentSubStack?.none { it is Exam || it is ZoomableImage } != false
        }

        val dashboardSubStack = navigator.state.subStacks[Dashboard] ?: error("Dashboard stack missing")
        val catalogSubStack = navigator.state.subStacks[Catalog] ?: error("Catalog stack missing")
        val mistakesSubStack = navigator.state.subStacks[Mistakes] ?: error("Mistakes stack missing")
        val preferencesSubStack = navigator.state.subStacks[Preferences] ?: error("Preferences stack missing")

        val entries = navigator.state.toEntries(
            entryProvider = entryProvider {
                dashboardEntry(
                    backStack = dashboardSubStack,
                    onStartExamClick = { category -> navigator.navigate(Exam(category)) },
                    onStartBlitzExamClick = { category -> navigator.navigate(Exam(category = category, type = ExamType.BLITZ)) },
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
                    onRestartExam = { category, type ->
                        navigator.navigate(Exam(category = category, type = type), popCurrent = true)
                    },
                )
                imageDetailsEntry(
                    backStack = navigator.state.currentSubStack ?: dashboardSubStack,
                    onDismiss = { navigator.goBack() },
                )
                statisticsEntry(
                    onChangePreferenceClick = { navigator.navigate(Preferences) },
                )
                preferencesEntry(
                    backStack = preferencesSubStack,
                )
            },
        )

        val hazeState = rememberHazeState()

        Box(
            modifier = Modifier.fillMaxHeight(),
        ) {
            NavDisplay(
                entries = entries,
                sceneStrategies = listOf(
                    remember { DialogSceneStrategy() },
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
                    .background(MaterialTheme.colorScheme.background)
                    .hazeSource(state = hazeState),
            )
            // floating vertical toolbar
            AnimatedVisibility(
                visible = !adaptiveInfo.isCompact && showFloatingToolbar,
                enter = slideInHorizontally { -it },
                exit = slideOutHorizontally { -it },
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .zIndex(1f),
            ) {
                MerrPatentenVerticalFloatingToolbar {
                    topLevelDestinations.forEach { (key, icon, labelRes) ->
                        val selected = currentTopLevel == key
                        val label = stringResource(labelRes)
                        VerticalFloatingToolbarNavItem(
                            selected = selected,
                            icon = icon,
                            label = label,
                            onClick = { navigator.navigate(key) },
                        )
                    }
                }
            }
            // floating horizontal toolbar
            AnimatedVisibility(
                visible = adaptiveInfo.isCompact && showFloatingToolbar,
                enter = slideInVertically { it },
                exit = slideOutVertically { it },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .zIndex(1f),
            ) {
                MerrPatentenHorizontalFloatingToolbar(
                    hazeState = hazeState,
                ) {
                    topLevelDestinations.forEach { (key, icon, labelRes) ->
                        val selected = currentTopLevel == key
                        val label = stringResource(labelRes)
                        FloatingToolbarNavItem(
                            selected = selected,
                            hazeState = hazeState,
                            icon = icon,
                            label = label,
                            onClick = { navigator.navigate(key) },
                        )
                    }
                }
            }
        }
    }
}
