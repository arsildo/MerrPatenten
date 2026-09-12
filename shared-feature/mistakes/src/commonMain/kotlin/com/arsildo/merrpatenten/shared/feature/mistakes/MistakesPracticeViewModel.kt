package com.arsildo.merrpatenten.shared.feature.mistakes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.merrpatenten.shared.core.data.MistakesRepository
import com.arsildo.merrpatenten.shared.core.model.MistakeQuestion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MistakesPracticeUiState(
    val category: String = "B",
    val questions: List<MistakeQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedAnswer: String? = null,
    val isAnswerRevealed: Boolean = false,
    val isAnswerCorrect: Boolean = false,
    val hasGraduated: Boolean = false,
    val masteredQuestionsCount: Int = 0,
    val totalAnsweredCount: Int = 0,
    val isPracticeFinished: Boolean = false,
    val isLoading: Boolean = true,
) {
    val currentQuestion: MistakeQuestion?
        get() = questions.getOrNull(currentIndex)
}

class MistakesPracticeViewModel(private val category: String, private val mistakesRepository: MistakesRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(MistakesPracticeUiState(category = category))
    val uiState: StateFlow<MistakesPracticeUiState> = _uiState.asStateFlow()

    init {
        loadMistakes()
    }

    fun loadMistakes() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        val list = mistakesRepository.getMistakeQuestions(category).first()
        _uiState.update {
            it.copy(
                questions = list.shuffled(),
                currentIndex = 0,
                selectedAnswer = null,
                isAnswerRevealed = false,
                isAnswerCorrect = false,
                hasGraduated = false,
                masteredQuestionsCount = 0,
                totalAnsweredCount = 0,
                isPracticeFinished = list.isEmpty(),
                isLoading = false,
            )
        }
    }

    fun submitAnswer(selectedAnswer: String) = viewModelScope.launch {
        val state = _uiState.value
        val current = state.currentQuestion ?: return@launch
        if (state.isAnswerRevealed) return@launch

        val isCorrect = selectedAnswer.trim().equals(current.question.answer.trim(), ignoreCase = true)
        val graduated = mistakesRepository.recordPracticeAnswer(
            questionId = current.question.id,
            category = category,
            isCorrect = isCorrect,
            masteryThreshold = 2,
        )

        _uiState.update {
            it.copy(
                selectedAnswer = selectedAnswer,
                isAnswerRevealed = true,
                isAnswerCorrect = isCorrect,
                hasGraduated = graduated,
                masteredQuestionsCount = if (graduated) it.masteredQuestionsCount + 1 else it.masteredQuestionsCount,
                totalAnsweredCount = it.totalAnsweredCount + 1,
            )
        }
    }

    fun nextQuestion() {
        val state = _uiState.value
        val nextIdx = state.currentIndex + 1
        if (nextIdx >= state.questions.size) {
            _uiState.update { it.copy(isPracticeFinished = true) }
        } else {
            _uiState.update {
                it.copy(
                    currentIndex = nextIdx,
                    selectedAnswer = null,
                    isAnswerRevealed = false,
                    isAnswerCorrect = false,
                    hasGraduated = false,
                )
            }
        }
    }
}
