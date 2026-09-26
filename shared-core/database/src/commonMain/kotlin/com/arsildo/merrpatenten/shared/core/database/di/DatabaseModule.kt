package com.arsildo.merrpatenten.shared.core.database.di

import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.execSQL
import com.arsildo.merrpatenten.shared.core.database.ExamResultsDAO
import com.arsildo.merrpatenten.shared.core.database.MerrPatentenDatabase
import com.arsildo.merrpatenten.shared.core.database.MistakesDAO
import com.arsildo.merrpatenten.shared.core.database.QuestionnaireDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformDatabaseModule: Module

val databaseModule = module {
    includes(platformDatabaseModule)
    single { getRoomDatabase(get()) }
    single<QuestionnaireDAO> { get<MerrPatentenDatabase>().questionnaireDAO() }
    single<ExamResultsDAO> { get<MerrPatentenDatabase>().examResultsDAO() }
    single<MistakesDAO> { get<MerrPatentenDatabase>().mistakesDAO() }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL(
            """
            CREATE TABLE IF NOT EXISTS mistake_records (
                question_id INTEGER NOT NULL,
                category TEXT NOT NULL DEFAULT 'B',
                error_count INTEGER NOT NULL DEFAULT 1,
                consecutive_correct INTEGER NOT NULL DEFAULT 0,
                last_tested_at INTEGER NOT NULL DEFAULT 0,
                PRIMARY KEY(question_id, category)
            );
            """.trimIndent(),
        )
    }
}

internal fun getRoomDatabase(builder: RoomDatabase.Builder<MerrPatentenDatabase>): MerrPatentenDatabase = builder
    .addMigrations(MIGRATION_1_2)
    .fallbackToDestructiveMigration(true)
    .fallbackToDestructiveMigrationOnDowngrade(true)
    .addCallback(
        object : RoomDatabase.Callback() {
            override fun onOpen(connection: SQLiteConnection) {
                connection.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS mistake_records (
                        question_id INTEGER NOT NULL,
                        category TEXT NOT NULL DEFAULT 'B',
                        error_count INTEGER NOT NULL DEFAULT 1,
                        consecutive_correct INTEGER NOT NULL DEFAULT 0,
                        last_tested_at INTEGER NOT NULL DEFAULT 0,
                        PRIMARY KEY(question_id, category)
                    );
                    """.trimIndent(),
                )
                connection.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS examResults (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        errors INTEGER NOT NULL,
                        time TEXT NOT NULL,
                        category TEXT NOT NULL DEFAULT 'B'
                    );
                    """.trimIndent(),
                )
            }
        },
    )
    .setDriver(BundledSQLiteDriver())
    .setQueryCoroutineContext(Dispatchers.IO)
    .build()
