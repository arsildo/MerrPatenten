package com.arsildo.merrpatenten.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.merrpatenten.data.local.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class PreferencesUiState(
    val saveStats: Boolean = true,
    val confirmAppExit: Boolean = true,
    val followSystemColors: Boolean = true,
    val colorScheme: Boolean = false,
    val dynamicColorScheme: Boolean = true,
)

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PreferencesUiState())
    val uiState = combine(
        preferencesRepository.getSaveStats,
        preferencesRepository.getConfirmExitApp,
        preferencesRepository.getSystemColorScheme,
        preferencesRepository.getColorScheme,
        preferencesRepository.getDynamicColorScheme
    ) { saveStats, confirmExit, systemColorScheme, colorScheme, dynamicColors ->
        PreferencesUiState(
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