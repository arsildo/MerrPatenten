package com.arsildo.merrpatenten.shared.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamResultsDAO {
    @Query("SELECT * FROM examResults")
    fun getAllResults(): Flow<List<ExamResultEntity>>

    @Insert
    suspend fun insertResult(result: ExamResultEntity)

    @Query("DELETE FROM examResults")
    suspend fun deleteAllResults()

    @Query("DELETE FROM examResults WHERE id NOT IN (SELECT id FROM examResults ORDER BY id DESC LIMIT 20)")
    suspend fun limitResults()
}
