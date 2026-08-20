package com.arsildo.merrpatenten.shared.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.arsildo.merrpatenten.shared.core.datastore.PreferencesRepository
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.feature.dashboard.navigation.Dashboard
import com.arsildo.merrpatenten.shared.feature.dashboard.navigation.dashboardEntry
import com.arsildo.merrpatenten.shared.feature.exam.navigation.Exam
import com.arsildo.merrpatenten.shared.feature.exam.navigation.ExamResultBottomSheet
import com.arsildo.merrpatenten.shared.feature.exam.navigation.examEntry
import com.arsildo.merrpatenten.shared.feature.imagedetails.navigation.ZoomableImage
import com.arsildo.merrpatenten.shared.feature.imagedetails.navigation.imageDetailsEntry
import com.arsildo.merrpatenten.shared.feature.preferences.PreferencesViewModel
import com.arsildo.merrpatenten.shared.feature.preferences.navigation.Preferences
import com.arsildo.merrpatenten.shared.feature.preferences.navigation.preferencesEntry
import com.arsildo.merrpatenten.shared.feature.statistics.navigation.Statistics
import com.arsildo.merrpatenten.shared.feature.statistics.navigation.statisticsEntry
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import navigation.BottomSheetSceneStrategy
import navigation.rememberSharedViewModelStoreNavEntryDecorator
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Dashboard::class, Dashboard.serializer())
            subclass(Exam::class, Exam.serializer())
            subclass(ExamResultBottomSheet::class, ExamResultBottomSheet.serializer())
            subclass(ZoomableImage::class, ZoomableImage.serializer())
            subclass(Statistics::class, Statistics.serializer())
            subclass(Preferences::class, Preferences.serializer())
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
        hapticFeedback = uiState.hapticFeedback
    ) {
        val backStack = rememberNavBackStack(configuration = config, Dashboard)

        NavDisplay(
            backStack = backStack,
            sceneStrategies = listOf(
                remember { BottomSheetSceneStrategy() }
            ),
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberSharedViewModelStoreNavEntryDecorator(),
            ),
            onBack = dropUnlessResumed(block = backStack::removeLastOrNull),
            entryProvider = entryProvider {
                dashboardEntry(
                    backStack = backStack,
                    onStartExamClick = { category -> backStack.add(Exam(category)) },
                    onStatisticsClick = { backStack.add(Statistics) },
                    onPreferencesClick = { backStack.add(Preferences) }
                )
                examEntry(
                    backStack = backStack,
                    onImageDetailsClick = { imageId -> backStack.add(ZoomableImage(imageId)) },
                    onExitExam = {
                        backStack.clear()
                        backStack.add(Dashboard)
                    },
                    onRestartExam = { category ->
                        backStack.removeLastOrNull()
                        backStack.add(Exam(category))
                    }
                )
                imageDetailsEntry(
                    backStack = backStack,
                    onDismiss = { backStack.removeLastOrNull() }
                )
                statisticsEntry(
                    backStack = backStack,
                    onBackPress = { backStack.removeLastOrNull() },
                    onChangePreferenceClick = { backStack.add(Preferences) }
                )
                preferencesEntry(
                    backStack = backStack,
                    onBackPress = { backStack.removeLastOrNull() }
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        )
    }
}
