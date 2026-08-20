package com.arsildo.merrpatenten.shared.feature.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.merrpatenten.shared.core.data.CatalogRepository
import com.arsildo.merrpatenten.shared.core.model.RoadSign
import com.arsildo.merrpatenten.shared.core.model.SignCategory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class CatalogUiState(
    val selectedCategory: SignCategory = SignCategory.ALL,
    val searchQuery: String = "",
    val items: List<RoadSign> = emptyList(),
    val isSearching: Boolean = false,
)

@OptIn(ExperimentalCoroutinesApi::class)
class CatalogViewModel(private val catalogRepository: CatalogRepository) : ViewModel() {

    private val _selectedCategory = MutableStateFlow(SignCategory.ALL)
    private val _searchQuery = MutableStateFlow("")
    private val _isSearching = MutableStateFlow(false)

    private val _filteredItems: StateFlow<List<RoadSign>> = combine(
        _selectedCategory,
        _searchQuery,
    ) { category, query ->
        category to query
    }.flatMapLatest { (category, query) ->
        if (query.isBlank()) {
            if (category == SignCategory.ALL) {
                catalogRepository.getAllSigns()
            } else {
                catalogRepository.getSignsByCategory(category)
            }
        } else {
            catalogRepository.searchSigns(query, category)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList(),
    )

    val uiState: StateFlow<CatalogUiState> = combine(
        _selectedCategory,
        _searchQuery,
        _isSearching,
        _filteredItems,
    ) { category, query, isSearching, items ->
        CatalogUiState(
            selectedCategory = category,
            searchQuery = query,
            items = items,
            isSearching = isSearching,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CatalogUiState(),
    )

    fun onCategorySelected(category: SignCategory) {
        _selectedCategory.update { category }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.update { query }
    }

    fun toggleSearch(active: Boolean) {
        _isSearching.update { active }
        if (!active) {
            _searchQuery.update { "" }
        }
    }
}
