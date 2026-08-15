package com.arsildo.merrpatenten.shared.core.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.arsildo.merrpatenten.shared.core.database.MerrPatentenDatabase
import kotlinx.coroutines.runBlocking
import merrpatenten.shared_core.design_system.generated.resources.Res
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<MerrPatentenDatabase> {
    val applicationContext = context.applicationContext
    val dbFile = applicationContext.getDatabasePath("merrpatenten.db")
    if (!dbFile.exists() || dbFile.length() == 0L) {
        dbFile.parentFile?.mkdirs()
        try {
            val bytes = runBlocking { Res.readBytes("files/dpshtrr_questionnaire.db") }
            dbFile.writeBytes(bytes)
        } catch (e: Exception) {
            println("Failed to copy pre-populated database on Android: ${e.message}")
        }
    }
    return Room.databaseBuilder<MerrPatentenDatabase>(
        context = applicationContext,
        name = dbFile.absolutePath
    )
}

actual val platformDatabaseModule: Module = module {
    singleOf(::getDatabaseBuilder)
}

