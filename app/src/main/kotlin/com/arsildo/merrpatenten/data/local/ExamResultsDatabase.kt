package com.arsildo.merrpatenten.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arsildo.merrpatenten.data.ExamResult

@Database(entities = [ExamResult::class], version = 1, exportSchema = false)
abstract class ExamResultsDatabase : RoomDatabase() {
    abstract fun examResultsDAO(): ExamResultsDAO
}