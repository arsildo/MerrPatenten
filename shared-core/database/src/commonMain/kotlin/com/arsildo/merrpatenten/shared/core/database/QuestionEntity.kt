package com.arsildo.merrpatenten.shared.core.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arsildo.merrpatenten.shared.core.model.Question

@Entity(tableName = "questionnaire")
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "question")
    val question: String,
    @ColumnInfo(name = "answer")
    val answer: String,
    @ColumnInfo(name = "image")
    val image: Int,
)

fun QuestionEntity.toDomain() = Question(
    id = id,
    question = question,
    answer = answer,
    image = image
)
