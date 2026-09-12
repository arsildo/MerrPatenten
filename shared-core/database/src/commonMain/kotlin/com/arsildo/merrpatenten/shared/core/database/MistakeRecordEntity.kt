package com.arsildo.merrpatenten.shared.core.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.arsildo.merrpatenten.shared.core.model.MistakeRecord

@Entity(
    tableName = "mistake_records",
    primaryKeys = ["question_id", "category"],
)
data class MistakeRecordEntity(
    @ColumnInfo(name = "question_id")
    val questionId: Int,
    @ColumnInfo(name = "category", defaultValue = "B")
    val category: String = "B",
    @ColumnInfo(name = "error_count", defaultValue = "1")
    val errorCount: Int = 1,
    @ColumnInfo(name = "consecutive_correct", defaultValue = "0")
    val consecutiveCorrect: Int = 0,
    @ColumnInfo(name = "last_tested_at", defaultValue = "0")
    val lastTestedAt: Long = 0L,
)

fun MistakeRecordEntity.toDomain() = MistakeRecord(
    questionId = questionId,
    category = category,
    errorCount = errorCount,
    consecutiveCorrect = consecutiveCorrect,
    lastTestedAt = lastTestedAt,
)

fun MistakeRecord.toEntity() = MistakeRecordEntity(
    questionId = questionId,
    category = category,
    errorCount = errorCount,
    consecutiveCorrect = consecutiveCorrect,
    lastTestedAt = lastTestedAt,
)
