package com.arsildo.merrpatenten.shared.core.model

import kotlinx.serialization.Serializable

@Serializable
data class ExamResult(val id: Int = 0, val errors: Int = 0, val time: String = "", val category: String = "B")
