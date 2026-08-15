package com.arsildo.merrpatenten.shared.core.database

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionnaireDAO {
    @Query("SELECT * FROM questionnaire")
    fun getAll(): Flow<List<QuestionEntity>>
}
