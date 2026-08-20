package com.arsildo.merrpatenten.shared.feature.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.merrpatenten.shared.core.data.CatalogRepository
import com.arsildo.merrpatenten.shared.core.model.Question
import com.arsildo.merrpatenten.shared.core.model.RoadSign
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

data class SignDetailUiState(
    val sign: RoadSign? = null,
    val relatedQuestions: List<Question> = emptyList(),
    val isLoading: Boolean = false,
)

@OptIn(ExperimentalCoroutinesApi::class)
class SignDetailViewModel(signId: Int, private val catalogRepository: CatalogRepository) : ViewModel() {

    private val _sign = MutableStateFlow(catalogRepository.getSignById(signId))

    private val _relatedQuestions: StateFlow<List<Question>> = _sign
        .flatMapLatest { sign ->
            if (sign != null) {
                catalogRepository.getQuestionsForSign(sign.imageResNumber)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    val uiState: StateFlow<SignDetailUiState> = combine(
        _sign,
        _relatedQuestions,
    ) { sign, questions ->
        SignDetailUiState(
            sign = sign,
            relatedQuestions = questions,
            isLoading = false,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SignDetailUiState(sign = catalogRepository.getSignById(signId)),
    )
}
