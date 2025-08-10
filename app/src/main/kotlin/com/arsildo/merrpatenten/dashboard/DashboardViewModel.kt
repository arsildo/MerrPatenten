package com.arsildo.merrpatenten.dashboard

import androidx.lifecycle.ViewModel
import com.arsildo.merrpatenten.data.local.PreferencesRepository

class DashboardViewModel(
    private val preferencesRepository: PreferencesRepository
): ViewModel() {
    val confirmAppExit = preferencesRepository.getConfirmExitApp
}