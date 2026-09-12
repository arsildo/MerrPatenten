package com.arsildo.merrpatenten.shared.core.data

import com.arsildo.merrpatenten.shared.core.database.MistakeRecordEntity
import com.arsildo.merrpatenten.shared.core.database.MistakesDAO
import com.arsildo.merrpatenten.shared.core.database.QuestionnaireDAO
import com.arsildo.merrpatenten.shared.core.database.toDomain
import com.arsildo.merrpatenten.shared.core.model.MistakeQuestion
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class MistakesRepository(private val questionnaireDAO: QuestionnaireDAO, private val mistakesDAO: MistakesDAO) {
    fun getMistakeQuestions(category: String): Flow<List<MistakeQuestion>> = combine(
        questionnaireDAO.getByCategory(category),
        mistakesDAO.getMistakesByCategory(category),
    ) { questions, records ->
        val questionMap = questions.associateBy { it.id }
        records.mapNotNull { record ->
            questionMap[record.questionId]?.let { questionEntity ->
                MistakeQuestion(
                    question = questionEntity.toDomain(),
                    record = record.toDomain(),
                )
            }
        }
    }

    fun getAllMistakeQuestions(): Flow<List<MistakeQuestion>> = combine(
        questionnaireDAO.getAll(),
        mistakesDAO.getAllMistakes(),
    ) { questions, records ->
        val questionMap = questions.associateBy { "${it.id}_${it.category}" }
        records.mapNotNull { record ->
            questionMap["${record.questionId}_${record.category}"]?.let { questionEntity ->
                MistakeQuestion(
                    question = questionEntity.toDomain(),
                    record = record.toDomain(),
                )
            }
        }
    }

    fun getMistakesCount(category: String): Flow<Int> = mistakesDAO.getMistakesCountByCategory(category)

    fun getTotalMistakesCount(): Flow<Int> = mistakesDAO.getTotalMistakesCount()

    @OptIn(ExperimentalTime::class)
    suspend fun recordMistake(questionId: Int, category: String) {
        val existing = mistakesDAO.getMistake(questionId, category)
        val errorCount = (existing?.errorCount ?: 0) + 1
        val now = Clock.System.now().toEpochMilliseconds()
        val record = MistakeRecordEntity(
            questionId = questionId,
            category = category,
            errorCount = errorCount,
            consecutiveCorrect = 0,
            lastTestedAt = now,
        )
        mistakesDAO.insertOrUpdate(record)
    }

    /**
     * Records an answer during mistake practice mode.
     * Returns true if the question achieved mastery (consecutiveCorrect >= masteryThreshold) and was removed from the notebook.
     */
    @OptIn(ExperimentalTime::class)
    suspend fun recordPracticeAnswer(questionId: Int, category: String, isCorrect: Boolean, masteryThreshold: Int = 2): Boolean {
        val existing = mistakesDAO.getMistake(questionId, category) ?: return false
        val now = Clock.System.now().toEpochMilliseconds()
        return if (isCorrect) {
            val nextConsecutive = existing.consecutiveCorrect + 1
            if (nextConsecutive >= masteryThreshold) {
                mistakesDAO.deleteMistake(questionId, category)
                true
            } else {
                mistakesDAO.insertOrUpdate(
                    existing.copy(
                        consecutiveCorrect = nextConsecutive,
                        lastTestedAt = now,
                    ),
                )
                false
            }
        } else {
            mistakesDAO.insertOrUpdate(
                existing.copy(
                    errorCount = existing.errorCount + 1,
                    consecutiveCorrect = 0,
                    lastTestedAt = now,
                ),
            )
            false
        }
    }

    suspend fun deleteMistake(questionId: Int, category: String) {
        mistakesDAO.deleteMistake(questionId, category)
    }

    suspend fun clearMistakesByCategory(category: String) {
        mistakesDAO.deleteAllByCategory(category)
    }

    suspend fun clearAllMistakes() {
        mistakesDAO.deleteAll()
    }
}
