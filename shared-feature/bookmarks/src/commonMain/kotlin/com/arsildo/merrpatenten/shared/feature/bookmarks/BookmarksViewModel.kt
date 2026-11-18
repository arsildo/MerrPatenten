package com.arsildo.merrpatenten.shared.feature.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.merrpatenten.shared.core.data.BookmarksRepository
import com.arsildo.merrpatenten.shared.core.model.BookmarkedQuestion
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class BookmarksUiState(
    val selectedCategory: String = "B",
    val searchQuery: String = "",
    val bookmarks: List<BookmarkedQuestion> = emptyList(),
    val totalCount: Int = 0,
    val isLoading: Boolean = false,
)

@OptIn(ExperimentalCoroutinesApi::class)
class BookmarksViewModel(private val bookmarksRepository: BookmarksRepository) : ViewModel() {

    private val _selectedCategory = MutableStateFlow("B")
    private val _searchQuery = MutableStateFlow("")

    val uiState: StateFlow<BookmarksUiState> = _selectedCategory
        .flatMapLatest { category ->
            combine(
                bookmarksRepository.getBookmarkedQuestions(category),
                _searchQuery,
            ) { bookmarks, query ->
                val filtered = if (query.isBlank()) {
                    bookmarks
                } else {
                    bookmarks.filter {
                        it.question.question.contains(query, ignoreCase = true) ||
                            it.question.id.toString().contains(query)
                    }
                }
                BookmarksUiState(
                    selectedCategory = category,
                    searchQuery = query,
                    bookmarks = filtered,
                    totalCount = bookmarks.size,
                    isLoading = false,
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = BookmarksUiState(isLoading = true),
        )

    fun onSelectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun removeBookmark(questionId: Int, category: String) = viewModelScope.launch {
        bookmarksRepository.removeBookmark(questionId, category)
    }

    fun clearCategoryBookmarks() = viewModelScope.launch {
        bookmarksRepository.clearBookmarksByCategory(_selectedCategory.value)
    }
}
