package com.arsildo.merrpatenten.shared.core.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.arsildo.merrpatenten.shared.core.model.Question

@Entity(
    tableName = "questionnaire",
    primaryKeys = ["id", "category"],
)
data class QuestionEntity(
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "question")
    val question: String,
    @ColumnInfo(name = "answer")
    val answer: String,
    @ColumnInfo(name = "image")
    val image: Int,
    @ColumnInfo(name = "category", defaultValue = "B")
    val category: String = "B",
)

fun QuestionEntity.toDomain() = Question(
    id = id,
    question = question.trim(),
    answer = answer.trim(),
    image = image,
    category = category.trim(),
)
