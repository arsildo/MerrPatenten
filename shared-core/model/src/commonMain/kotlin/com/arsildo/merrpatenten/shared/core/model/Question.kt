package com.arsildo.merrpatenten.shared.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Question(val id: Int, val question: String, val answer: String, val image: Int, val category: String = "B")

@Serializable
data class MistakeRecord(
    val questionId: Int,
    val category: String = "B",
    val errorCount: Int = 1,
    val consecutiveCorrect: Int = 0,
    val lastTestedAt: Long = 0L,
)

@Serializable
data class MistakeQuestion(val question: Question, val record: MistakeRecord)
