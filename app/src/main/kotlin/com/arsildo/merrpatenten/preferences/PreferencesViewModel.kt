package com.arsildo.merrpatenten.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.merrpatenten.data.local.PreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class PreferencesUiState(
    val immersiveMode: Boolean = false,
    val saveStats: Boolean = true,
    val confirmAppExit: Boolean = true,
    val followSystemColors: Boolean = true,
    val colorScheme: Boolean = false,
    val dynamicColorScheme: Boolean = true,
)

class PreferencesViewModel(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    val uiState = combineFlows(
        preferencesRepository.getImmersiveMode,
        preferencesRepository.getSaveStats,
        preferencesRepository.getConfirmExitApp,
        preferencesRepository.getSystemColorScheme,
        preferencesRepository.getColorScheme,
        preferencesRepository.getDynamicColorScheme
    ) { immersive, saveStats, confirmExit, systemColorScheme, colorScheme, dynamicColors ->
        PreferencesUiState(
            immersiveMode = immersive,
            saveStats = saveStats,
            confirmAppExit = confirmExit,
            followSystemColors = systemColorScheme,
            colorScheme = colorScheme,
            dynamicColorScheme = dynamicColors,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PreferencesUiState()
    )


    fun setImmersiveMode(immersiveMode: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                preferencesRepository.setImmersiveMode(immersiveMode)
            }
        }
    }

    fun setSaveStats(save: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                preferencesRepository.setSaveStats(save)
            }
        }
    }


    fun setConfirmAppExit(confirm: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                preferencesRepository.setConfirmExitApp(confirm)
            }
        }
    }

    fun setFollowSystem(followSystemColors: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                preferencesRepository.setFollowSystemColorScheme(followSystemColors)
            }
        }
    }

    fun setColorScheme(colorScheme: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                preferencesRepository.setColorScheme(colorScheme)
            }
        }
    }

    fun setDynamicColorScheme(dynamicColorScheme: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                preferencesRepository.setDynamicColorScheme(dynamicColorScheme)
            }
        }
    }
}

fun <T1, T2, T3, T4, T5, T6, R> combineFlows(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    transform: suspend (T1, T2, T3, T4, T5, T6) -> R
): Flow<R> = combine(
    combine(flow, flow2, flow3, ::Triple),
    combine(flow4, flow5, flow6, ::Triple)
) { t1, t2 ->
    transform(
        t1.first,
        t1.second,
        t1.third,
        t2.first,
        t2.second,
        t2.third
    )
}
