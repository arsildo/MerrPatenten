package com.arsildo.merrpatenten.shared.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.merrpatenten.shared.core.data.ExamResultsRepository
import com.arsildo.merrpatenten.shared.core.data.MistakesRepository
import com.arsildo.merrpatenten.shared.core.data.ReadinessScore
import com.arsildo.merrpatenten.shared.core.data.calculateReadinessScore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

internal data class DashboardUiState(val readinessScore: ReadinessScore = ReadinessScore.EMPTY, val mistakesCount: Int = 0)

internal class DashboardViewModel(examResultsRepository: ExamResultsRepository, mistakesRepository: MistakesRepository) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = combine(
        examResultsRepository.getAllResults(),
        mistakesRepository.getTotalMistakesCount(),
    ) { results, mistakesCount ->
        DashboardUiState(
            readinessScore = calculateReadinessScore(results),
            mistakesCount = mistakesCount,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = DashboardUiState(),
    )
}
