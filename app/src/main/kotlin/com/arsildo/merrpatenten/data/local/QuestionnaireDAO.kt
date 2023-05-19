package com.arsildo.merrpatenten.data.local

import androidx.room.Dao
import androidx.room.Query
import com.arsildo.merrpatenten.data.Question
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionnaireDAO {
    @Query("SELECT * FROM questionnaire")
    fun getAll(): Flow<List<Question>>

}
