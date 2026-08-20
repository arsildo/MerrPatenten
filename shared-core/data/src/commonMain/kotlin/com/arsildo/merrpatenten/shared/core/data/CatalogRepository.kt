package com.arsildo.merrpatenten.shared.core.data

import com.arsildo.merrpatenten.shared.core.model.Question
import com.arsildo.merrpatenten.shared.core.model.RoadSign
import com.arsildo.merrpatenten.shared.core.model.SignCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class CatalogRepository(private val questionnaireRepository: QuestionnaireRepository) {
    fun getAllSigns(): Flow<List<RoadSign>> = flowOf(CatalogDataSource.items)

    fun getSignsByCategory(category: SignCategory): Flow<List<RoadSign>> {
        val filtered = if (category == SignCategory.ALL) {
            CatalogDataSource.items
        } else {
            CatalogDataSource.items.filter { it.category == category }
        }
        return flowOf(filtered)
    }

    fun searchSigns(query: String, category: SignCategory): Flow<List<RoadSign>> {
        val trimmed = query.trim().lowercase()
        val categoryFiltered = if (category == SignCategory.ALL) {
            CatalogDataSource.items
        } else {
            CatalogDataSource.items.filter { it.category == category }
        }

        if (trimmed.isEmpty()) {
            return flowOf(categoryFiltered)
        }

        val result = categoryFiltered.filter { sign ->
            sign.title.lowercase().contains(trimmed) ||
                sign.description.lowercase().contains(trimmed) ||
                sign.code.lowercase().contains(trimmed) ||
                sign.imageResNumber.toString() == trimmed
        }
        return flowOf(result)
    }

    fun getSignById(id: Int): RoadSign? = CatalogDataSource.items.firstOrNull { it.id == id }

    fun getQuestionsForSign(imageId: Int): Flow<List<Question>> = questionnaireRepository.getByImage(imageId)
}
