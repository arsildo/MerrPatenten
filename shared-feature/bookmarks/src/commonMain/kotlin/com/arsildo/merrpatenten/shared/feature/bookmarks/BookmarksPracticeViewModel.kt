package com.arsildo.merrpatenten.shared.feature.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.merrpatenten.shared.core.data.BookmarksRepository
import com.arsildo.merrpatenten.shared.core.model.BookmarkedQuestion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BookmarksPracticeUiState(
    val category: String = "B",
    val questions: List<BookmarkedQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedAnswer: String? = null,
    val isAnswerRevealed: Boolean = false,
    val isAnswerCorrect: Boolean = false,
    val correctCount: Int = 0,
    val totalAnsweredCount: Int = 0,
    val isLoading: Boolean = true,
) {
    val currentQuestion: BookmarkedQuestion?
        get() = questions.getOrNull(currentIndex)
}

class BookmarksPracticeViewModel(private val category: String, private val bookmarksRepository: BookmarksRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(BookmarksPracticeUiState(category = category))
    val uiState: StateFlow<BookmarksPracticeUiState> = _uiState.asStateFlow()

    init {
        loadBookmarks()
    }

    fun loadBookmarks() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        val list = bookmarksRepository.getBookmarkedQuestions(category).first()
        _uiState.update {
            it.copy(
                questions = list.shuffled(),
                currentIndex = 0,
                selectedAnswer = null,
                isAnswerRevealed = false,
                isAnswerCorrect = false,
                correctCount = 0,
                totalAnsweredCount = 0,
                isLoading = false,
            )
        }
    }

    fun submitAnswer(selectedAnswer: String) {
        val state = _uiState.value
        val current = state.currentQuestion ?: return
        if (state.isAnswerRevealed) return

        val isCorrect = selectedAnswer.trim().equals(current.question.answer.trim(), ignoreCase = true)
        _uiState.update {
            it.copy(
                selectedAnswer = selectedAnswer,
                isAnswerRevealed = true,
                isAnswerCorrect = isCorrect,
                correctCount = if (isCorrect) it.correctCount + 1 else it.correctCount,
                totalAnsweredCount = it.totalAnsweredCount + 1,
            )
        }
    }

    fun nextQuestion() {
        val state = _uiState.value
        val nextIdx = state.currentIndex + 1
        if (nextIdx < state.questions.size) {
            _uiState.update {
                it.copy(
                    currentIndex = nextIdx,
                    selectedAnswer = null,
                    isAnswerRevealed = false,
                    isAnswerCorrect = false,
                )
            }
        }
    }
}
