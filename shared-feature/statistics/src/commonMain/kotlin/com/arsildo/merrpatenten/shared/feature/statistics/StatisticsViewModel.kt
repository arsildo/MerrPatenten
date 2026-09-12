package com.arsildo.merrpatenten.shared.feature.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.merrpatenten.shared.core.data.ExamResultsRepository
import com.arsildo.merrpatenten.shared.core.data.ReadinessScore
import com.arsildo.merrpatenten.shared.core.data.calculateReadinessScore
import com.arsildo.merrpatenten.shared.core.datastore.PreferencesRepository
import com.arsildo.merrpatenten.shared.core.model.ExamResult
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal data class StatisticsUiState(
    val results: List<ExamResult> = emptyList(),
    val saveResults: Boolean = true,
    val readinessScore: ReadinessScore = ReadinessScore.EMPTY,
)

internal class StatisticsViewModel(
    private val examResultsRepository: ExamResultsRepository,
    preferencesRepository: PreferencesRepository,
) : ViewModel() {

    val uiState = combine(
        preferencesRepository.getSaveStats,
        examResultsRepository.getAllResults(),
    ) { saveResults, results ->
        StatisticsUiState(
            results = results,
            saveResults = saveResults,
            readinessScore = calculateReadinessScore(results),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = StatisticsUiState(),
    )

    fun deleteAllResults() {
        viewModelScope.launch {
            examResultsRepository.deleteAllResults()
        }
    }
}
