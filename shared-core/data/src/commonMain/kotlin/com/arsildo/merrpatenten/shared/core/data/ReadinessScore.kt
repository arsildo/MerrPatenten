package com.arsildo.merrpatenten.shared.core.data

import com.arsildo.merrpatenten.shared.core.model.ExamResult
import kotlin.math.roundToInt

private const val DEFAULT_ALLOWED_ERRORS = 4

enum class ReadinessLevel {
    NOT_ENOUGH_DATA,
    NEEDS_PRACTICE,
    IN_PROGRESS,
    READY,
}

data class ReadinessScore(
    val percentage: Int = 0,
    val level: ReadinessLevel = ReadinessLevel.NOT_ENOUGH_DATA,
    val completedExams: Int = 0,
    val passedExams: Int = 0,
    val passRate: Int = 0,
    val averageErrors: Double = 0.0,
) {
    companion object {
        val EMPTY = ReadinessScore()
    }
}

fun calculateReadinessScore(results: List<ExamResult>, maxAllowedErrors: Int = DEFAULT_ALLOWED_ERRORS): ReadinessScore {
    if (results.isEmpty()) return ReadinessScore.EMPTY

    val sample = results.take(20)
    val totalCount = sample.size
    val passedCount = sample.count { it.errors <= maxAllowedErrors }
    val passRatio = passedCount.toDouble() / totalCount
    val passRate = (passRatio * 100).roundToInt()
    val totalErrors = sample.sumOf { it.errors }
    val avgErrors = totalErrors.toDouble() / totalCount
    val roundedAvgErrors = (avgErrors * 10).roundToInt() / 10.0

    if (totalCount < 3) {
        val partialPercentage = if (passedCount > 0) (passRatio * 50).roundToInt() else 15
        return ReadinessScore(
            percentage = partialPercentage,
            level = ReadinessLevel.NOT_ENOUGH_DATA,
            completedExams = totalCount,
            passedExams = passedCount,
            passRate = passRate,
            averageErrors = roundedAvgErrors,
        )
    }

    val passRatePoints = passRatio * 55.0
    val errorPoints = when {
        avgErrors <= 2.0 -> 35.0
        avgErrors <= maxAllowedErrors.toDouble() -> 35.0 - ((avgErrors - 2.0) / (maxAllowedErrors - 2.0).coerceAtLeast(1.0)) * 15.0
        else -> (20.0 - ((avgErrors - maxAllowedErrors) / 4.0) * 20.0).coerceAtLeast(0.0)
    }
    val volumePoints = (totalCount / 5.0).coerceAtMost(1.0) * 10.0
    val totalPercentage = (passRatePoints + errorPoints + volumePoints).roundToInt().coerceIn(0, 100)

    val level = when {
        totalPercentage >= 80 -> ReadinessLevel.READY
        totalPercentage >= 50 -> ReadinessLevel.IN_PROGRESS
        else -> ReadinessLevel.NEEDS_PRACTICE
    }

    return ReadinessScore(
        percentage = totalPercentage,
        level = level,
        completedExams = totalCount,
        passedExams = passedCount,
        passRate = passRate,
        averageErrors = roundedAvgErrors,
    )
}
