package com.arsildo.merrpatenten.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.arsildo.merrpatenten.data.ExamResult
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamResultsDAO {
    @Query("SELECT * FROM examResults")
    fun getAllResults(): Flow<List<ExamResult>>

    @Insert
    fun insertResult(result: ExamResult)

    @Query("DELETE FROM examResults")
    fun deleteAllResults()

}