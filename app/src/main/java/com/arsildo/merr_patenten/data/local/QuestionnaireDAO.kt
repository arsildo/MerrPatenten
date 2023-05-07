package com.arsildo.merr_patenten.data.local

import androidx.room.Dao
import androidx.room.Query
import com.arsildo.merr_patenten.data.Question
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionnaireDAO {
    @Query("SELECT * FROM questionnaire where id <= 40")
    fun getAll(): Flow<List<Question>>

}
