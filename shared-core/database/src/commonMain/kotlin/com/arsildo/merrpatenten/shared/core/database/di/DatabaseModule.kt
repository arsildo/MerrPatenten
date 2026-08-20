package com.arsildo.merrpatenten.shared.core.database.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.arsildo.merrpatenten.shared.core.database.ExamResultsDAO
import com.arsildo.merrpatenten.shared.core.database.MerrPatentenDatabase
import com.arsildo.merrpatenten.shared.core.database.QuestionnaireDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

expect val platformDatabaseModule: Module

val databaseModule = module {
    includes(platformDatabaseModule)
    singleOf(::getRoomDatabase)
    single<QuestionnaireDAO> { get<MerrPatentenDatabase>().questionnaireDAO() }
    single<ExamResultsDAO> { get<MerrPatentenDatabase>().examResultsDAO() }
}

internal fun getRoomDatabase(builder: RoomDatabase.Builder<MerrPatentenDatabase>): MerrPatentenDatabase = builder
    .fallbackToDestructiveMigrationOnDowngrade(true)
    .setDriver(BundledSQLiteDriver())
    .setQueryCoroutineContext(Dispatchers.IO)
    .build()
