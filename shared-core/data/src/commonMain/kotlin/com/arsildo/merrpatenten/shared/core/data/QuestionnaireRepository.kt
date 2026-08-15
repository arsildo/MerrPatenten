package com.arsildo.merrpatenten.shared.core.data

import com.arsildo.merrpatenten.shared.core.database.QuestionnaireDAO
import com.arsildo.merrpatenten.shared.core.database.toDomain
import com.arsildo.merrpatenten.shared.core.model.Question
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class QuestionnaireRepository(
    private val questionnaireDAO: QuestionnaireDAO
) {
    fun getByCategory(category: String): Flow<List<Question>> {
        return questionnaireDAO.getByCategory(category).map { list ->
            list.map { it.toDomain() }
        }
    }

    fun getAll(): Flow<List<Question>> {
        return questionnaireDAO.getAll().map { list ->
            list.map { it.toDomain() }
        }
    }
}
