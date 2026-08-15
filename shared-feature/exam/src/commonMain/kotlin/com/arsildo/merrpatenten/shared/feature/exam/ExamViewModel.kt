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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

private const val ANSWER_TRUE = "Saktë"
private const val ANSWER_FALSE = "Gabim"
private const val INITIAL_TIMER = "40:00"

internal data class ExamUiState(
    val category: String = "B",
    val isCompleted: Boolean = false,
    val saveStats: Boolean = false,
    val errors: Int = 0,
    val questions: List<Question> = emptyList(),
    val immersiveMode: Boolean = false,
    val timer: String = INITIAL_TIMER,
    val trueCheckedPositions: List<Boolean> = emptyList(),
    val falseCheckedPositions: List<Boolean> = emptyList(),
    val responseList: List<String> = emptyList(),
    val mistakePositions: List<Int> = emptyList(),
)

internal class ExamViewModel(
    private val preferencesRepository: PreferencesRepository,
    private val questionnaireRepository: QuestionnaireRepository,
    private val examResultsRepository: ExamResultsRepository,
) : ViewModel() {

    private var timerJob: Job? = null

    private val _targetPage = MutableStateFlow<Int?>(null)
    val targetPage: StateFlow<Int?> = _targetPage.asStateFlow()

    fun navigateToPage(page: Int) {
        _targetPage.value = page
    }

    fun onPageNavigated() {
        _targetPage.value = null
    }

    private val _internalUiState = MutableStateFlow(
        ExamUiState(
            trueCheckedPositions = List(QUESTIONS_IN_EXAM) { false },
            falseCheckedPositions = List(QUESTIONS_IN_EXAM) { false },
            responseList = List(QUESTIONS_IN_EXAM) { "" },
            mistakePositions = List(QUESTIONS_IN_EXAM) { 1 },
        )
    )

    val uiState: StateFlow<ExamUiState> = combine(
        _internalUiState,
        preferencesRepository.getSaveStats,
        preferencesRepository.getImmersiveMode,
    ) { state, saveStats, immersiveMode ->
        state.copy(
            saveStats = saveStats,
            immersiveMode = immersiveMode,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = _internalUiState.value
    )

    fun startExam(category: String = "B") = viewModelScope.launch {
        timerJob?.cancel()
        try {
            val allQuestions = questionnaireRepository.getByCategory(category).first { it.isNotEmpty() }
            val selectedQuestions = allQuestions.shuffled().take(QUESTIONS_IN_EXAM)
            val questionCount = selectedQuestions.size

            _internalUiState.update {
                ExamUiState(
                    category = category,
                    questions = selectedQuestions,
                    trueCheckedPositions = List(questionCount) { false },
                    falseCheckedPositions = List(questionCount) { false },
                    responseList = List(questionCount) { "" },
                    mistakePositions = List(questionCount) { 1 },
                    timer = INITIAL_TIMER,
                    isCompleted = false,
                    errors = 0,
                )
            }
            startCountdown()
        } catch (e: Exception) {
            println("Error in startExam($category): ${e.message}")
            e.printStackTrace()
        }
    }

    private fun startCountdown() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var remainingMillis = EXAM_DURATION_RELEASE
            while (remainingMillis > 0) {
                if (_internalUiState.value.isCompleted) {
                    return@launch
                }
                val formatted = formatTimer(remainingMillis)
                _internalUiState.update { it.copy(timer = formatted) }
                delay(1000L.milliseconds)
                remainingMillis -= 1000L
            }
            if (!_internalUiState.value.isCompleted) {
                _internalUiState.update { it.copy(timer = formatTimer(0)) }
                concludeExam()
            }
        }
    }

    fun countCompletedQuestions(): Int {
        return _internalUiState.value.responseList.count { it.isNotBlank() }
    }

    fun completeExam() {
        concludeExam()
    }

    private fun concludeExam() {
        timerJob?.cancel()
        timerJob = null

        val currentState = _internalUiState.value
        if (currentState.isCompleted) return

        val (errors, mistakePositions) = calculateMistakes(currentState)
        _internalUiState.update {
            it.copy(
                isCompleted = true,
                errors = errors,
                mistakePositions = mistakePositions
            )
        }

        saveExamResult(errors = errors, timeSpent = currentState.timer)
    }

    fun checkTrueAtPosition(position: Int) {
        updateAnswerAt(position = position, selectedAnswer = ANSWER_TRUE)
    }

    fun checkFalseAtPosition(position: Int) {
        updateAnswerAt(position = position, selectedAnswer = ANSWER_FALSE)
    }

    private fun updateAnswerAt(position: Int, selectedAnswer: String) {
        _internalUiState.update { current ->
            if (current.isCompleted) return@update current
            if (position !in current.responseList.indices) return@update current

            val isSelectingTrue = selectedAnswer == ANSWER_TRUE
            val isCurrentlySelected = if (isSelectingTrue) {
                current.trueCheckedPositions.getOrElse(position) { false }
            } else {
                current.falseCheckedPositions.getOrElse(position) { false }
            }

            val newSelectedState = !isCurrentlySelected
            val newTrue = current.trueCheckedPositions.toMutableList()
            val newFalse = current.falseCheckedPositions.toMutableList()
            val newResponses = current.responseList.toMutableList()

            if (isSelectingTrue) {
                newTrue[position] = newSelectedState
                if (newSelectedState) {
                    newFalse[position] = false
                    newResponses[position] = ANSWER_TRUE
                } else {
                    newResponses[position] = ""
                }
            } else {
                newFalse[position] = newSelectedState
                if (newSelectedState) {
                    newTrue[position] = false
                    newResponses[position] = ANSWER_FALSE
                } else {
                    newResponses[position] = ""
                }
            }

            current.copy(
                trueCheckedPositions = newTrue,
                falseCheckedPositions = newFalse,
                responseList = newResponses
            )
        }
    }

    private fun calculateMistakes(state: ExamUiState): Pair<Int, List<Int>> {
        val mistakePositions = state.questions.indices.map { index ->
            val userResponse = state.responseList.getOrElse(index) { "" }
            val correctAnswer = state.questions[index].answer
            if (userResponse.isNotBlank() && userResponse == correctAnswer) 0 else 1
        }
        val totalErrors = mistakePositions.count { it == 1 }
        return Pair(totalErrors, mistakePositions)
    }

    private fun saveExamResult(errors: Int, timeSpent: String) = viewModelScope.launch {
        try {
            val shouldSave = preferencesRepository.getSaveStats.first()
            if (shouldSave) {
                examResultsRepository.insertResult(
                    ExamResult(
                        errors = errors,
                        time = timeSpent,
                        category = _internalUiState.value.category,
                    )
                )
                examResultsRepository.limitResults()
            }
        } catch (_: Exception) {
            // Failure to save background stats does not block user flow
        }
    }
}

