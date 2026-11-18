package com.arsildo.merrpatenten.shared.core.data

import com.arsildo.merrpatenten.shared.core.database.BookmarksDAO
import com.arsildo.merrpatenten.shared.core.database.QuestionnaireDAO
import com.arsildo.merrpatenten.shared.core.database.toDomain
import com.arsildo.merrpatenten.shared.core.database.toEntity
import com.arsildo.merrpatenten.shared.core.model.BookmarkRecord
import com.arsildo.merrpatenten.shared.core.model.BookmarkedQuestion
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class BookmarksRepository(private val questionnaireDAO: QuestionnaireDAO, private val bookmarksDAO: BookmarksDAO) {
    fun getBookmarkedQuestions(category: String): Flow<List<BookmarkedQuestion>> = combine(
        questionnaireDAO.getByCategory(category),
        bookmarksDAO.getBookmarksByCategory(category),
    ) { questions, records ->
        val questionMap = questions.associateBy { it.id }
        records.mapNotNull { record ->
            questionMap[record.questionId]?.let { questionEntity ->
                BookmarkedQuestion(
                    question = questionEntity.toDomain(),
                    record = record.toDomain(),
                )
            }
        }
    }

    fun getAllBookmarkedQuestions(): Flow<List<BookmarkedQuestion>> = combine(
        questionnaireDAO.getAll(),
        bookmarksDAO.getAllBookmarks(),
    ) { questions, records ->
        val questionMap = questions.associateBy { "${it.id}_${it.category}" }
        records.mapNotNull { record ->
            questionMap["${record.questionId}_${record.category}"]?.let { questionEntity ->
                BookmarkedQuestion(
                    question = questionEntity.toDomain(),
                    record = record.toDomain(),
                )
            }
        }
    }

    fun getBookmarkedQuestionIds(category: String): Flow<Set<Int>> = bookmarksDAO.getBookmarkedQuestionIds(category).map { it.toSet() }

    fun getBookmarksCount(category: String): Flow<Int> = bookmarksDAO.getBookmarksCountByCategory(category)

    fun getTotalBookmarksCount(): Flow<Int> = bookmarksDAO.getTotalBookmarksCount()

    @OptIn(ExperimentalTime::class)
    suspend fun toggleBookmark(questionId: Int, category: String): Boolean {
        val existing = bookmarksDAO.getBookmark(questionId, category)
        return if (existing != null) {
            bookmarksDAO.deleteBookmark(questionId, category)
            false
        } else {
            val now = Clock.System.now().toEpochMilliseconds()
            bookmarksDAO.insert(
                BookmarkRecord(
                    questionId = questionId,
                    category = category,
                    createdAt = now,
                ).toEntity(),
            )
            true
        }
    }

    suspend fun removeBookmark(questionId: Int, category: String) {
        bookmarksDAO.deleteBookmark(questionId, category)
    }

    suspend fun clearBookmarksByCategory(category: String) {
        bookmarksDAO.deleteAllByCategory(category)
    }

    suspend fun clearAllBookmarks() {
        bookmarksDAO.deleteAll()
    }
}
