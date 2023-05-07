package com.arsildo.merr_patenten.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arsildo.merr_patenten.data.Question

@Database(entities = [Question::class], version = 1, exportSchema = true)
abstract class QuestionnaireDatabase : RoomDatabase() {
    abstract fun questionnaireDAO(): QuestionnaireDAO
}
