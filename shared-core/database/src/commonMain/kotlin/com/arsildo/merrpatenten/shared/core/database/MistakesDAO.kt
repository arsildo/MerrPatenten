package com.arsildo.merrpatenten.shared.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MistakesDAO {
    @Query("SELECT * FROM mistake_records ORDER BY error_count DESC, last_tested_at DESC")
    fun getAllMistakes(): Flow<List<MistakeRecordEntity>>

    @Query("SELECT * FROM mistake_records WHERE category = :category ORDER BY error_count DESC, last_tested_at DESC")
    fun getMistakesByCategory(category: String): Flow<List<MistakeRecordEntity>>

    @Query("SELECT * FROM mistake_records WHERE question_id = :questionId AND category = :category LIMIT 1")
    suspend fun getMistake(questionId: Int, category: String): MistakeRecordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(mistake: MistakeRecordEntity)

    @Query("DELETE FROM mistake_records WHERE question_id = :questionId AND category = :category")
    suspend fun deleteMistake(questionId: Int, category: String)

    @Query("DELETE FROM mistake_records WHERE category = :category")
    suspend fun deleteAllByCategory(category: String)

    @Query("DELETE FROM mistake_records")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM mistake_records WHERE category = :category")
    fun getMistakesCountByCategory(category: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM mistake_records")
    fun getTotalMistakesCount(): Flow<Int>
}
