package com.arsildo.merrpatenten.shared.core.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.arsildo.merrpatenten.shared.core.database.MerrPatentenDatabase
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<MerrPatentenDatabase> {
    val applicationContext = context.applicationContext
    val dbFile = applicationContext.getDatabasePath("merrpatenten.db")
    if (!dbFile.exists() || dbFile.length() == 0L) {
        dbFile.parentFile?.mkdirs()
        try {
            applicationContext.assets.open("database/dpshtrr_questionnaire.db").use { input ->
                dbFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        } catch (_: Exception) {
            // fallback if assets not in old folder
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
