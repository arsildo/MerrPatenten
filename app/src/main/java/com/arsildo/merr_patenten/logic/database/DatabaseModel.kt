package com.arsildo.merr_patenten.logic.database

import androidx.compose.runtime.Immutable

@Immutable
data class DatabaseModel(
    val id: Int,
    val question : String,
    val image : Int,
    val answer : String,
)