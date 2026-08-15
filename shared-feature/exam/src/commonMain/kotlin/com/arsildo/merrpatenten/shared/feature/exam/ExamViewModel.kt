package com.arsildo.merrpatenten.shared.feature.exam

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.merrpatenten.shared.core.data.ExamResultsRepository
import com.arsildo.merrpatenten.shared.core.data.QuestionnaireRepository
import com.arsildo.merrpatenten.shared.core.datastore.PreferencesRepository
import com.arsildo.merrpatenten.shared.core.designsystem.EXAM_DURATION_RELEASE
import com.arsildo.merrpatenten.shared.core.designsystem.QUESTIONS_IN_EXAM
import com.arsildo.merrpatenten.shared.core.designsystem.formatTimer
import com.arsildo.merrpatenten.shared.core.model.ExamResult
import com.arsildo.merrpatenten.shared.core.model.Question
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

data class ExamUiState(
    val isCompleted: Boolean = false,
    val saveStats: Boolean = false,
    val errors: Int = 0,
    val questions: List<Question> = emptyList(),
    val immersiveMode: Boolean = false,
    val timer: String = "40:00",
    val trueCheckedPositions: List<Boolean> = emptyList(),
    val falseCheckedPositions: List<Boolean> = emptyList(),
    val responseList: List<String> = emptyList(),
    val mistakePositions: List<Int> = emptyList(),
)

class ExamViewModel(
    private val preferencesRepository: PreferencesRepository,
    private val questionnaireRepository: QuestionnaireRepository,
    private val examResultsRepository: ExamResultsRepository,
) : ViewModel() {

    private var timerJob: Job? = null

    private val _targetPage = MutableStateFlow<Int?>(null)
    val targetPage: StateFlow<Int?> = _targetPage

    fun navigateToPage(page: Int) {
        _targetPage.value = page
    }

    fun onPageNavigated() {
        _targetPage.value = null
    }

    private val _uiState = MutableStateFlow(
        ExamUiState(
            trueCheckedPositions = List(QUESTIONS_IN_EXAM) { false },
            falseCheckedPositions = List(QUESTIONS_IN_EXAM) { false },
            responseList = List(QUESTIONS_IN_EXAM) { "" },
            mistakePositions = List(QUESTIONS_IN_EXAM) { 1 },
        )
    )
    val uiState: StateFlow<ExamUiState> = combine(
        _uiState,
        preferencesRepository.getSaveStats,
        preferencesRepository.getImmersiveMode,
    ) { state, stats, immersive ->
        state.copy(
            saveStats = stats,
            immersiveMode = immersive
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = _uiState.value
    )

    init {
        loadQuestionsAndStartTimer()
    }

    private fun loadQuestionsAndStartTimer() = viewModelScope.launch {
        try {
            val allQuestions = questionnaireRepository.getAll().first { it.isNotEmpty() }
            val generated = mutableListOf<Question>()
            val indices = (allQuestions.indices).shuffled()
            for (i in 0 until minOf(QUESTIONS_IN_EXAM, indices.size)) {
                generated.add(allQuestions[indices[i]])
            }
            _uiState.update { it.copy(questions = generated) }
            startCountDown()
        } catch (_: Exception) {
            // Error handling
        }
    }

    private fun startCountDown() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var remainingMillis = EXAM_DURATION_RELEASE
            while (remainingMillis > 0) {
                if (_uiState.value.isCompleted) {
                    concludeExam()
                    return@launch
                }
                val formatted = formatTimer(remainingMillis)
                _uiState.update { it.copy(timer = formatted) }
                delay(1000L.milliseconds)
                remainingMillis -= 1000L
            }
            concludeExam()
        }
    }

    fun countCompletedQuestions(): Int {
        return _uiState.value.responseList.count { it.isNotBlank() }
    }

    fun completeExam() {
        concludeExam()
    }

    private fun concludeExam() = viewModelScope.launch {
        timerJob?.cancel()
        val (errors, mistakePositions) = countErrors()
        _uiState.update {
            it.copy(
                isCompleted = true,
                errors = errors,
                mistakePositions = mistakePositions
            )
        }
        insertResult(errors)
    }

    fun checkTrueAtPosition(position: Int) {
        _uiState.update { current ->
            if (position >= current.trueCheckedPositions.size) return@update current
            val newTrue = current.trueCheckedPositions.toMutableList()
            val newFalse = current.falseCheckedPositions.toMutableList()
            val newResponses = current.responseList.toMutableList()

            newTrue[position] = !newTrue[position]
            if (newTrue[position]) {
                newFalse[position] = false
                newResponses[position] = "Saktë"
            } else {
                newResponses[position] = ""
            }
            current.copy(
                trueCheckedPositions = newTrue,
                falseCheckedPositions = newFalse,
                responseList = newResponses
            )
        }
    }

    fun checkFalseAtPosition(position: Int) {
        _uiState.update { current ->
            if (position >= current.falseCheckedPositions.size) return@update current
            val newTrue = current.trueCheckedPositions.toMutableList()
            val newFalse = current.falseCheckedPositions.toMutableList()
            val newResponses = current.responseList.toMutableList()

            newFalse[position] = !newFalse[position]
            if (newFalse[position]) {
                newTrue[position] = false
                newResponses[position] = "Gabim"
            } else {
                newResponses[position] = ""
            }
            current.copy(
                trueCheckedPositions = newTrue,
                falseCheckedPositions = newFalse,
                responseList = newResponses
            )
        }
    }

    private fun countErrors(): Pair<Int, List<Int>> {
        var errors = 0
        val state = _uiState.value
        val newMistakes = state.mistakePositions.toMutableList()
        for (index in 0 until minOf(QUESTIONS_IN_EXAM, state.questions.size)) {
            if (state.responseList[index] != state.questions[index].answer) {
                errors++
                if (index < newMistakes.size) newMistakes[index] = 1
            } else {
                if (index < newMistakes.size) newMistakes[index] = 0
            }
        }
        return Pair(errors, newMistakes)
    }

    private fun insertResult(errors: Int) = viewModelScope.launch {
        if (_uiState.value.saveStats) {
            examResultsRepository.insertResult(
                ExamResult(
                    errors = errors,
                    time = _uiState.value.timer,
                )
            )
            examResultsRepository.limitResults()
        }
    }
}

