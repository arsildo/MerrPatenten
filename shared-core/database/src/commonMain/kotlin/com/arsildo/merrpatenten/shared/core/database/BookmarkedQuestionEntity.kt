package com.arsildo.merrpatenten.shared.core.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.arsildo.merrpatenten.shared.core.model.BookmarkRecord

@Entity(
    tableName = "bookmarked_questions",
    primaryKeys = ["question_id", "category"],
)
data class BookmarkedQuestionEntity(
    @ColumnInfo(name = "question_id")
    val questionId: Int,
    @ColumnInfo(name = "category", defaultValue = "B")
    val category: String = "B",
    @ColumnInfo(name = "created_at", defaultValue = "0")
    val createdAt: Long = 0L,
)

fun BookmarkedQuestionEntity.toDomain() = BookmarkRecord(
    questionId = questionId,
    category = category,
    createdAt = createdAt,
)

fun BookmarkRecord.toEntity() = BookmarkedQuestionEntity(
    questionId = questionId,
    category = category,
    createdAt = createdAt,
)
