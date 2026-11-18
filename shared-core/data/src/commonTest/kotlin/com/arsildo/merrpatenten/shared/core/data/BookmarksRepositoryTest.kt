package com.arsildo.merrpatenten.shared.core.data

import com.arsildo.merrpatenten.shared.core.database.BookmarkedQuestionEntity
import com.arsildo.merrpatenten.shared.core.database.BookmarksDAO
import com.arsildo.merrpatenten.shared.core.database.QuestionEntity
import com.arsildo.merrpatenten.shared.core.database.QuestionnaireDAO
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest

private class FakeBookmarksQuestionnaireDAO : QuestionnaireDAO {
    private val questions = MutableStateFlow<List<QuestionEntity>>(emptyList())

    fun setQuestions(list: List<QuestionEntity>) {
        questions.value = list
    }

    override fun getByCategory(category: String): Flow<List<QuestionEntity>> = questions.map { it.filter { q -> q.category == category } }

    override fun getAll(): Flow<List<QuestionEntity>> = questions

    override fun getByImage(imageId: Int): Flow<List<QuestionEntity>> = questions.map { it.filter { q -> q.image == imageId } }
}

private class FakeBookmarksDAO : BookmarksDAO {
    private val records = MutableStateFlow<Map<String, BookmarkedQuestionEntity>>(emptyMap())

    private fun key(id: Int, cat: String) = "${id}_$cat"

    override fun getAllBookmarks(): Flow<List<BookmarkedQuestionEntity>> = records.map { it.values.toList() }

    override fun getBookmarksByCategory(category: String): Flow<List<BookmarkedQuestionEntity>> =
        records.map { it.values.filter { r -> r.category == category } }

    override fun getBookmarkedQuestionIds(category: String): Flow<List<Int>> =
        records.map { it.values.filter { r -> r.category == category }.map { r -> r.questionId } }

    override suspend fun getBookmark(questionId: Int, category: String): BookmarkedQuestionEntity? =
        records.value[key(questionId, category)]

    override suspend fun insert(bookmark: BookmarkedQuestionEntity) {
        val current = records.value.toMutableMap()
        current[key(bookmark.questionId, bookmark.category)] = bookmark
        records.value = current
    }

    override suspend fun deleteBookmark(questionId: Int, category: String) {
        val current = records.value.toMutableMap()
        current.remove(key(questionId, category))
        records.value = current
    }

    override suspend fun deleteAllByCategory(category: String) {
        val current = records.value.toMutableMap()
        current.entries.removeAll { it.value.category == category }
        records.value = current
    }

    override suspend fun deleteAll() {
        records.value = emptyMap()
    }

    override fun getBookmarksCountByCategory(category: String): Flow<Int> = records.map { it.values.count { r -> r.category == category } }

    override fun getTotalBookmarksCount(): Flow<Int> = records.map { it.size }
}

class BookmarksRepositoryTest {

    @Test
    fun toggleBookmarkAddsAndRemovesBookmark() = runTest {
        val qDao = FakeBookmarksQuestionnaireDAO()
        val bDao = FakeBookmarksDAO()
        val repo = BookmarksRepository(qDao, bDao)

        val added = repo.toggleBookmark(questionId = 10, category = "B")
        assertTrue(added)
        assertEquals(setOf(10), repo.getBookmarkedQuestionIds("B").first())
        assertEquals(1, repo.getTotalBookmarksCount().first())

        val removed = repo.toggleBookmark(questionId = 10, category = "B")
        assertFalse(removed)
        assertEquals(emptySet(), repo.getBookmarkedQuestionIds("B").first())
        assertEquals(0, repo.getTotalBookmarksCount().first())
    }

    @Test
    fun getBookmarkedQuestionsJoinsWithQuestionnaire() = runTest {
        val qDao = FakeBookmarksQuestionnaireDAO().apply {
            setQuestions(
                listOf(
                    QuestionEntity(id = 1, question = "Q1", answer = "Saktë", image = 1, category = "B"),
                    QuestionEntity(id = 2, question = "Q2", answer = "Gabim", image = 2, category = "C"),
                ),
            )
        }
        val bDao = FakeBookmarksDAO()
        val repo = BookmarksRepository(qDao, bDao)

        repo.toggleBookmark(questionId = 1, category = "B")
        repo.toggleBookmark(questionId = 2, category = "C")

        val bBookmarks = repo.getBookmarkedQuestions("B").first()
        assertEquals(1, bBookmarks.size)
        assertEquals("Q1", bBookmarks.first().question.question)

        val allBookmarks = repo.getAllBookmarkedQuestions().first()
        assertEquals(2, allBookmarks.size)
    }

    @Test
    fun clearBookmarksByCategoryOnlyClearsTargetCategory() = runTest {
        val qDao = FakeBookmarksQuestionnaireDAO()
        val bDao = FakeBookmarksDAO()
        val repo = BookmarksRepository(qDao, bDao)

        repo.toggleBookmark(questionId = 1, category = "B")
        repo.toggleBookmark(questionId = 2, category = "C")

        repo.clearBookmarksByCategory("B")

        assertEquals(0, repo.getBookmarksCount("B").first())
        assertEquals(1, repo.getBookmarksCount("C").first())
        assertEquals(1, repo.getTotalBookmarksCount().first())
    }
}
