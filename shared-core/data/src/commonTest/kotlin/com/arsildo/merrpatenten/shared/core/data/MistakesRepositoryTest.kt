package com.arsildo.merrpatenten.shared.core.data

import com.arsildo.merrpatenten.shared.core.database.MistakeRecordEntity
import com.arsildo.merrpatenten.shared.core.database.MistakesDAO
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

private class FakeQuestionnaireDAO : QuestionnaireDAO {
    private val questions = MutableStateFlow<List<QuestionEntity>>(emptyList())

    fun setQuestions(list: List<QuestionEntity>) {
        questions.value = list
    }

    override fun getByCategory(category: String): Flow<List<QuestionEntity>> = questions.map { it.filter { q -> q.category == category } }

    override fun getAll(): Flow<List<QuestionEntity>> = questions

    override fun getByImage(imageId: Int): Flow<List<QuestionEntity>> = questions.map { it.filter { q -> q.image == imageId } }
}

private class FakeMistakesDAO : MistakesDAO {
    private val records = MutableStateFlow<Map<String, MistakeRecordEntity>>(emptyMap())

    private fun key(id: Int, cat: String) = "${id}_$cat"

    override fun getAllMistakes(): Flow<List<MistakeRecordEntity>> = records.map { it.values.toList() }

    override fun getMistakesByCategory(category: String): Flow<List<MistakeRecordEntity>> =
        records.map { it.values.filter { r -> r.category == category } }

    override suspend fun getMistake(questionId: Int, category: String): MistakeRecordEntity? = records.value[key(questionId, category)]

    override suspend fun insertOrUpdate(mistake: MistakeRecordEntity) {
        val current = records.value.toMutableMap()
        current[key(mistake.questionId, mistake.category)] = mistake
        records.value = current
    }

    override suspend fun deleteMistake(questionId: Int, category: String) {
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

    override fun getMistakesCountByCategory(category: String): Flow<Int> = records.map { it.values.count { r -> r.category == category } }

    override fun getTotalMistakesCount(): Flow<Int> = records.map { it.size }
}

class MistakesRepositoryTest {

    @Test
    fun recordMistakeCreatesNewRecordWithCountOne() = runTest {
        val qDao = FakeQuestionnaireDAO()
        val mDao = FakeMistakesDAO()
        val repo = MistakesRepository(qDao, mDao)

        repo.recordMistake(questionId = 10, category = "B")

        val record = mDao.getMistake(10, "B")
        assertEquals(10, record?.questionId)
        assertEquals("B", record?.category)
        assertEquals(1, record?.errorCount)
        assertEquals(0, record?.consecutiveCorrect)
    }

    @Test
    fun repeatedMistakeIncrementsErrorCountAndResetsConsecutive() = runTest {
        val qDao = FakeQuestionnaireDAO()
        val mDao = FakeMistakesDAO()
        val repo = MistakesRepository(qDao, mDao)

        repo.recordMistake(questionId = 10, category = "B")
        // Practice answered right once
        repo.recordPracticeAnswer(questionId = 10, category = "B", isCorrect = true)
        assertEquals(1, mDao.getMistake(10, "B")?.consecutiveCorrect)

        // Failed again later
        repo.recordMistake(questionId = 10, category = "B")
        val record = mDao.getMistake(10, "B")
        assertEquals(2, record?.errorCount)
        assertEquals(0, record?.consecutiveCorrect)
    }

    @Test
    fun correctPracticeAchievingThresholdDeletesMistake() = runTest {
        val qDao = FakeQuestionnaireDAO()
        val mDao = FakeMistakesDAO()
        val repo = MistakesRepository(qDao, mDao)

        repo.recordMistake(questionId = 5, category = "B")

        // First correct answer (threshold = 2)
        val firstResult = repo.recordPracticeAnswer(5, "B", isCorrect = true, masteryThreshold = 2)
        assertFalse(firstResult)
        assertEquals(1, mDao.getMistake(5, "B")?.consecutiveCorrect)

        // Second correct answer -> reaches threshold and auto-graduates!
        val secondResult = repo.recordPracticeAnswer(5, "B", isCorrect = true, masteryThreshold = 2)
        assertTrue(secondResult)
        assertEquals(null, mDao.getMistake(5, "B"))
    }

    @Test
    fun wrongPracticeAnswerIncrementsErrorAndResetsStreak() = runTest {
        val qDao = FakeQuestionnaireDAO()
        val mDao = FakeMistakesDAO()
        val repo = MistakesRepository(qDao, mDao)

        repo.recordMistake(questionId = 7, category = "B")
        repo.recordPracticeAnswer(7, "B", isCorrect = true)
        assertEquals(1, mDao.getMistake(7, "B")?.consecutiveCorrect)

        val wrongResult = repo.recordPracticeAnswer(7, "B", isCorrect = false)
        assertFalse(wrongResult)
        val record = mDao.getMistake(7, "B")
        assertEquals(2, record?.errorCount)
        assertEquals(0, record?.consecutiveCorrect)
    }

    @Test
    fun getMistakeQuestionsCombinesQuestionWithRecord() = runTest {
        val qDao = FakeQuestionnaireDAO()
        val mDao = FakeMistakesDAO()
        val repo = MistakesRepository(qDao, mDao)

        qDao.setQuestions(
            listOf(
                QuestionEntity(id = 1, question = "Pyetja 1", answer = "Saktë", image = 0, category = "B"),
                QuestionEntity(id = 2, question = "Pyetja 2", answer = "Gabim", image = 10, category = "B"),
                QuestionEntity(id = 3, question = "Pyetja 3", answer = "Saktë", image = 0, category = "C"),
            ),
        )

        repo.recordMistake(1, "B")
        repo.recordMistake(2, "B")

        val bMistakes = repo.getMistakeQuestions("B").first()
        assertEquals(2, bMistakes.size)
        assertEquals(1, bMistakes[0].question.id)
        assertEquals("Pyetja 1", bMistakes[0].question.question)
        assertEquals(2, bMistakes[1].question.id)
    }
}
