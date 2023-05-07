package com.arsildo.merr_patenten.landing

import androidx.lifecycle.ViewModel
import com.arsildo.merr_patenten.data.local.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LandingViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
): ViewModel() {

    val confirmAppExit = preferencesRepository.getConfirmExitApp

}