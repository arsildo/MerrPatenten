package com.arsildo.merrpatenten.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.merrpatenten.data.ExamResult
import com.arsildo.merrpatenten.data.local.ExamResultsDAO
import com.arsildo.merrpatenten.data.local.PreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class StatisticsUiState(
    val results: List<ExamResult> = emptyList(),
    val saveResults: Boolean = true
)

class StatisticsViewModel(
    private val examResultsDAO: ExamResultsDAO,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    val uiState = combine(
        preferencesRepository.getSaveStats,
        examResultsDAO.getAllResults()
    ) { saveResults, results ->
        StatisticsUiState(
            results = results,
            saveResults = saveResults
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = StatisticsUiState()
    )

    fun deleteAllResults() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                examResultsDAO.deleteAllResults()
            }
        }
    }
}