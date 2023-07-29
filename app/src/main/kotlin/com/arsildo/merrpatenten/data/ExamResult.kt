package com.arsildo.merrpatenten.data

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "examResults")
data class ExamResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "errors") val errors: Int,
    @ColumnInfo(name = "time") val time: String,
)