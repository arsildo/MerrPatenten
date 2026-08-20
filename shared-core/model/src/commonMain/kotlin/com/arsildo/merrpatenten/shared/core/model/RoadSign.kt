package com.arsildo.merrpatenten.shared.core.model

import kotlinx.serialization.Serializable

@Serializable
enum class SignCategory {
    ALL,
    WARNING,
    PROHIBITORY,
    INFORMATIVE,
    ROAD_MARKINGS,
    POLICE_SIGNALS,
    INTERSECTIONS,
}

@Serializable
data class RoadSign(
    val id: Int,
    val imageResNumber: Int,
    val title: String,
    val category: SignCategory,
    val code: String = "",
    val description: String = "",
    val rightOfWayOrder: String? = null,
)
