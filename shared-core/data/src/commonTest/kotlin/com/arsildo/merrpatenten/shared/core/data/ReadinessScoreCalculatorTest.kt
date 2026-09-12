package com.arsildo.merrpatenten.shared.core.data

import com.arsildo.merrpatenten.shared.core.model.ExamResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ReadinessScoreCalculatorTest {

    @Test
    fun emptyResultsReturnsEmptyScore() {
        val score = calculateReadinessScore(emptyList())
        assertEquals(0, score.percentage)
        assertEquals(ReadinessLevel.NOT_ENOUGH_DATA, score.level)
        assertEquals(0, score.completedExams)
    }

    @Test
    fun lessThanThreeExamsReturnsNotEnoughData() {
        val results = listOf(
            ExamResult(id = 1, errors = 1, time = "30:00", category = "B"),
            ExamResult(id = 2, errors = 2, time = "28:00", category = "B"),
        )
        val score = calculateReadinessScore(results)
        assertEquals(ReadinessLevel.NOT_ENOUGH_DATA, score.level)
        assertEquals(2, score.completedExams)
        assertEquals(2, score.passedExams)
        assertEquals(100, score.passRate)
    }

    @Test
    fun highPassRateAndLowErrorsReturnsReady() {
        val results = listOf(
            ExamResult(id = 1, errors = 0, time = "30:00", category = "B"),
            ExamResult(id = 2, errors = 1, time = "28:00", category = "B"),
            ExamResult(id = 3, errors = 2, time = "31:00", category = "B"),
            ExamResult(id = 4, errors = 1, time = "25:00", category = "B"),
            ExamResult(id = 5, errors = 0, time = "27:00", category = "B"),
        )
        val score = calculateReadinessScore(results)
        assertEquals(ReadinessLevel.READY, score.level)
        assertTrue(score.percentage >= 80)
        assertEquals(5, score.completedExams)
        assertEquals(5, score.passedExams)
    }

    @Test
    fun mixedResultsReturnsInProgress() {
        val results = listOf(
            ExamResult(id = 1, errors = 2, time = "30:00", category = "B"),
            ExamResult(id = 2, errors = 5, time = "28:00", category = "B"),
            ExamResult(id = 3, errors = 3, time = "31:00", category = "B"),
            ExamResult(id = 4, errors = 4, time = "25:00", category = "B"),
        )
        val score = calculateReadinessScore(results)
        assertEquals(ReadinessLevel.IN_PROGRESS, score.level)
        assertTrue(score.percentage in 50..79)
    }

    @Test
    fun highErrorsReturnsNeedsPractice() {
        val results = listOf(
            ExamResult(id = 1, errors = 7, time = "30:00", category = "B"),
            ExamResult(id = 2, errors = 8, time = "28:00", category = "B"),
            ExamResult(id = 3, errors = 6, time = "31:00", category = "B"),
            ExamResult(id = 4, errors = 9, time = "25:00", category = "B"),
        )
        val score = calculateReadinessScore(results)
        assertEquals(ReadinessLevel.NEEDS_PRACTICE, score.level)
        assertTrue(score.percentage < 50)
    }
}
