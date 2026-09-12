package com.arsildo.merrpatenten.shared.feature.mistakes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.merrpatenten.shared.core.data.MistakesRepository
import com.arsildo.merrpatenten.shared.core.model.MistakeQuestion
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class MistakesUiState(
    val selectedCategory: String = "B",
    val searchQuery: String = "",
    val mistakes: List<MistakeQuestion> = emptyList(),
    val totalCount: Int = 0,
    val isLoading: Boolean = false,
)

@OptIn(ExperimentalCoroutinesApi::class)
class MistakesViewModel(private val mistakesRepository: MistakesRepository) : ViewModel() {

    private val _selectedCategory = MutableStateFlow("B")
    private val _searchQuery = MutableStateFlow("")

    val uiState: StateFlow<MistakesUiState> = _selectedCategory
        .flatMapLatest { category ->
            combine(
                mistakesRepository.getMistakeQuestions(category),
                _searchQuery,
            ) { mistakes, query ->
                val filtered = if (query.isBlank()) {
                    mistakes
                } else {
                    mistakes.filter {
                        it.question.question.contains(query, ignoreCase = true) ||
                            it.question.id.toString().contains(query)
                    }
                }
                MistakesUiState(
                    selectedCategory = category,
                    searchQuery = query,
                    mistakes = filtered,
                    totalCount = mistakes.size,
                    isLoading = false,
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MistakesUiState(isLoading = true),
        )

    fun onSelectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun deleteMistake(questionId: Int, category: String) = viewModelScope.launch {
        mistakesRepository.deleteMistake(questionId, category)
    }

    fun clearCategoryMistakes() = viewModelScope.launch {
        mistakesRepository.clearMistakesByCategory(_selectedCategory.value)
    }
}
