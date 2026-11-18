package com.arsildo.merrpatenten.shared.feature.bookmarks

import com.arsildo.merrpatenten.shared.core.data.BookmarksRepository
import com.arsildo.merrpatenten.shared.core.database.BookmarkedQuestionEntity
import com.arsildo.merrpatenten.shared.core.database.BookmarksDAO
import com.arsildo.merrpatenten.shared.core.database.QuestionEntity
import com.arsildo.merrpatenten.shared.core.database.QuestionnaireDAO
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

private class FakeQuestionnaireDAO : QuestionnaireDAO {
    val questions = MutableStateFlow(
        listOf(
            QuestionEntity(id = 1, question = "Kthesa majtas", answer = "Saktë", image = 1, category = "B"),
            QuestionEntity(id = 2, question = "Ndalim qarkullimi", answer = "Gabim", image = 2, category = "B"),
            QuestionEntity(id = 3, question = "Kamioni në rrugë", answer = "Saktë", image = 3, category = "C"),
        ),
    )

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
        records.value = records.value + (key(bookmark.questionId, bookmark.category) to bookmark)
    }

    override suspend fun deleteBookmark(questionId: Int, category: String) {
        records.value = records.value - key(questionId, category)
    }

    override suspend fun deleteAllByCategory(category: String) {
        records.value = records.value.filterValues { it.category != category }
    }

    override suspend fun deleteAll() {
        records.value = emptyMap()
    }

    override fun getBookmarksCountByCategory(category: String): Flow<Int> = records.map { it.values.count { r -> r.category == category } }

    override fun getTotalBookmarksCount(): Flow<Int> = records.map { it.size }
}

@OptIn(ExperimentalCoroutinesApi::class)
class BookmarksViewModelTest {

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun bookmarksViewModelFiltersByCategoryAndSearchQuery() = runTest {
        val repo = BookmarksRepository(FakeQuestionnaireDAO(), FakeBookmarksDAO())
        repo.toggleBookmark(questionId = 1, category = "B")
        repo.toggleBookmark(questionId = 2, category = "B")
        repo.toggleBookmark(questionId = 3, category = "C")

        val vm = BookmarksViewModel(repo)
        val initial = vm.uiState.first { !it.isLoading }
        assertEquals(2, initial.bookmarks.size)
        assertEquals(2, initial.totalCount)

        vm.onSearchQueryChange("Ndalim")
        val filtered = vm.uiState.first { it.searchQuery == "Ndalim" }
        assertEquals(1, filtered.bookmarks.size)
        assertEquals(2, filtered.bookmarks.first().question.id)
        assertEquals(2, filtered.totalCount)

        vm.onSearchQueryChange("")
        vm.onSelectCategory("C")
        val categoryC = vm.uiState.first { it.selectedCategory == "C" && !it.isLoading }
        assertEquals(1, categoryC.bookmarks.size)
        assertEquals(3, categoryC.bookmarks.first().question.id)

        vm.clearCategoryBookmarks()
        val cleared = vm.uiState.first { it.bookmarks.isEmpty() }
        assertEquals(0, cleared.totalCount)
    }

    @Test
    fun practiceViewModelTracksScoreAndCompletion() = runTest {
        val repo = BookmarksRepository(FakeQuestionnaireDAO(), FakeBookmarksDAO())
        repo.toggleBookmark(questionId = 1, category = "B")
        repo.toggleBookmark(questionId = 2, category = "B")

        val vm = BookmarksPracticeViewModel(category = "B", bookmarksRepository = repo)
        val state0 = vm.uiState.value
        assertEquals(2, state0.questions.size)
        assertFalse(state0.isPracticeFinished)

        val firstCorrectAnswer = state0.currentQuestion!!.question.answer
        vm.submitAnswer(firstCorrectAnswer)
        // Duplicate submit while revealed is ignored
        vm.submitAnswer("Gabim")
        assertTrue(vm.uiState.value.isAnswerCorrect)
        assertEquals(1, vm.uiState.value.correctCount)
        assertEquals(1, vm.uiState.value.totalAnsweredCount)

        vm.nextQuestion()
        assertEquals(1, vm.uiState.value.currentIndex)
        assertFalse(vm.uiState.value.isAnswerRevealed)

        val secondWrongAnswer = if (vm.uiState.value.currentQuestion!!.question.answer == "Saktë") "Gabim" else "Saktë"
        vm.submitAnswer(secondWrongAnswer)
        assertFalse(vm.uiState.value.isAnswerCorrect)
        assertEquals(1, vm.uiState.value.correctCount)
        assertEquals(2, vm.uiState.value.totalAnsweredCount)

        vm.nextQuestion()
        assertTrue(vm.uiState.value.isPracticeFinished)
    }
}
