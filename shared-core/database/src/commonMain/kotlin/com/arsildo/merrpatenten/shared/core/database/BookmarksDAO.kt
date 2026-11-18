package com.arsildo.merrpatenten.shared.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarksDAO {
    @Query("SELECT * FROM bookmarked_questions WHERE category = :category ORDER BY created_at DESC")
    fun getBookmarksByCategory(category: String): Flow<List<BookmarkedQuestionEntity>>

    @Query("SELECT question_id FROM bookmarked_questions WHERE category = :category")
    fun getBookmarkedQuestionIds(category: String): Flow<List<Int>>

    @Query("SELECT * FROM bookmarked_questions WHERE question_id = :questionId AND category = :category LIMIT 1")
    suspend fun getBookmark(questionId: Int, category: String): BookmarkedQuestionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bookmark: BookmarkedQuestionEntity)

    @Query("DELETE FROM bookmarked_questions WHERE question_id = :questionId AND category = :category")
    suspend fun deleteBookmark(questionId: Int, category: String)

    @Query("DELETE FROM bookmarked_questions WHERE category = :category")
    suspend fun deleteAllByCategory(category: String)

    @Query("SELECT COUNT(*) FROM bookmarked_questions")
    fun getTotalBookmarksCount(): Flow<Int>
}
