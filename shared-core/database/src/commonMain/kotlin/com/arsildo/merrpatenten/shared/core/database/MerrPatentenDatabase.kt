package com.arsildo.merrpatenten.shared.core.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor

@Database(
    entities = [QuestionEntity::class, ExamResultEntity::class],
    version = 1,
    exportSchema = false,
)
@ConstructedBy(MerrPatentenDatabaseConstructor::class)
abstract class MerrPatentenDatabase : RoomDatabase() {
    abstract fun questionnaireDAO(): QuestionnaireDAO
    abstract fun examResultsDAO(): ExamResultsDAO
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object MerrPatentenDatabaseConstructor : RoomDatabaseConstructor<MerrPatentenDatabase> {
    override fun initialize(): MerrPatentenDatabase
}
