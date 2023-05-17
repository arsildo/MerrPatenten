package com.arsildo.merrpatenten.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arsildo.merrpatenten.data.Question

@Database(entities = [Question::class], version = 1, exportSchema = false)
abstract class QuestionnaireDatabase : RoomDatabase() {
    abstract fun questionnaireDAO(): QuestionnaireDAO
}
