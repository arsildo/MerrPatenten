package com.arsildo.merrpatenten.shared.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.merrpatenten.shared.core.datastore.PreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class DashboardUiState(
    val confirmAppExit: Boolean = true,
)

class DashboardViewModel(
    preferencesRepository: PreferencesRepository
) : ViewModel() {
    val uiState: StateFlow<DashboardUiState> = preferencesRepository.getConfirmExitApp
        .map { DashboardUiState(confirmAppExit = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DashboardUiState()
        )
}

