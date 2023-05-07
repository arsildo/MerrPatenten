package com.arsildo.merr_patenten.exam

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.merr_patenten.data.Question
import com.arsildo.merr_patenten.data.local.PreferencesRepository
import com.arsildo.merr_patenten.data.local.QuestionnaireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

const val EXAM_DURATION: Long = 241000

data class ExamUiState(
    val isLoadingQuestions: Boolean = false,
    val isCompleted: Boolean = false,
    val saveStats: Boolean = false,
    val timer: String = "00:00",
    var questions: List<Question> = emptyList()
)

@HiltViewModel
class ExamViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val questionnaireRepository: QuestionnaireRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExamUiState())
    val uiState: StateFlow<ExamUiState> = combine(
        preferencesRepository.getSaveStats,
        questionnaireRepository.getAll()
    ) { stats, questions ->
        ExamUiState(
            saveStats = stats,
            questions = questions
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ExamUiState()
    )

    /*init {
        object : CountDownTimer(EXAM_DURATION, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                updateTimer(millisUntilFinished)
            }

            override fun onFinish() {
                cancel()
            }

        }.start()
    }

    private fun updateTimer(millis: Long) {
        _uiState.update { it.copy(timer = formatTimer(millis)) }
    }*/

    fun endExam() {
        _uiState.update { it.copy(isCompleted = !it.isCompleted) }
    }

}