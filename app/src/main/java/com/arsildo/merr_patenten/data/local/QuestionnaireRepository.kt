package com.arsildo.merr_patenten.data.local

import com.arsildo.merr_patenten.data.Question
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class QuestionnaireRepository @Inject constructor(
    private val questionnaireDAO: QuestionnaireDAO
){
    fun getAll(): Flow<List<Question>> {
        return questionnaireDAO.getAll()
    }
}