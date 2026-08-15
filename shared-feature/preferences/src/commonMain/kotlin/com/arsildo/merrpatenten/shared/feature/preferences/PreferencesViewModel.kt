package com.arsildo.merrpatenten.shared.feature.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.merrpatenten.shared.core.datastore.PreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class PreferencesUiState(
    val immersiveMode: Boolean = false,
    val saveStats: Boolean = true,
    val followSystemColors: Boolean = true,
    val colorScheme: Boolean = false,
    val dynamicColorScheme: Boolean = true,
)

class PreferencesViewModel(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    val uiState = combine(
        preferencesRepository.getImmersiveMode,
        preferencesRepository.getSaveStats,
        preferencesRepository.getSystemColorScheme,
        preferencesRepository.getColorScheme,
        preferencesRepository.getDynamicColorScheme
    ) { immersive, saveStats, systemColorScheme, colorScheme, dynamicColors ->
        PreferencesUiState(
            immersiveMode = immersive,
            saveStats = saveStats,
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
            preferencesRepository.setImmersiveMode(immersiveMode)
        }
    }

    fun setSaveStats(save: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setSaveStats(save)
        }
    }

    fun setFollowSystem(followSystemColors: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setFollowSystemColorScheme(followSystemColors)
        }
    }

    fun setColorScheme(colorScheme: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setColorScheme(colorScheme)
        }
    }

    fun setDynamicColorScheme(dynamicColorScheme: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setDynamicColorScheme(dynamicColorScheme)
        }
    }
}
