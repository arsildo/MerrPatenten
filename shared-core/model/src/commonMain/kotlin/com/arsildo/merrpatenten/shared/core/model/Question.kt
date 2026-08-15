package com.arsildo.merrpatenten.shared.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val id: Int,
    val question: String,
    val answer: String,
    val image: Int,
    val category: String = "B",
)
