package com.arsildo.merrpatenten.shared.core.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arsildo.merrpatenten.shared.core.model.ExamResult

@Entity(tableName = "examResults")
data class ExamResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "errors")
    val errors: Int,
    @ColumnInfo(name = "time")
    val time: String,
)

fun ExamResultEntity.toDomain() = ExamResult(
    id = id,
    errors = errors,
    time = time
)

fun ExamResult.toEntity() = ExamResultEntity(
    id = id,
    errors = errors,
    time = time
)
