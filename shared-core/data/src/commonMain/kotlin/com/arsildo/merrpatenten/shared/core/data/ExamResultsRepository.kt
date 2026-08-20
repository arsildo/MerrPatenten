package com.arsildo.merrpatenten.shared.core.data

import com.arsildo.merrpatenten.shared.core.database.ExamResultsDAO
import com.arsildo.merrpatenten.shared.core.database.toDomain
import com.arsildo.merrpatenten.shared.core.database.toEntity
import com.arsildo.merrpatenten.shared.core.model.ExamResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExamResultsRepository(private val examResultsDAO: ExamResultsDAO) {
    fun getAllResults(): Flow<List<ExamResult>> = examResultsDAO.getAllResults().map { list ->
        list.map { it.toDomain() }
    }

    suspend fun insertResult(result: ExamResult) {
        examResultsDAO.insertResult(result.toEntity())
    }

    suspend fun deleteAllResults() {
        examResultsDAO.deleteAllResults()
    }

    suspend fun limitResults() {
        examResultsDAO.limitResults()
    }
}
