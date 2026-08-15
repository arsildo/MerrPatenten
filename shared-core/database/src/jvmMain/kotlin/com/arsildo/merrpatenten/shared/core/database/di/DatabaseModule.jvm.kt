package com.arsildo.merrpatenten.shared.core.database.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.arsildo.merrpatenten.shared.core.database.MerrPatentenDatabase
import kotlinx.coroutines.runBlocking
import merrpatenten.shared_core.design_system.generated.resources.Res
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import java.io.File

internal fun getDatabaseBuilder(): RoomDatabase.Builder<MerrPatentenDatabase> {
    val appData = File(System.getProperty("user.home"), ".merrpatenten")
    if (!appData.exists()) appData.mkdirs()
    val dbFile = File(appData, "merrpatenten.db")
    if (!dbFile.exists() || dbFile.length() == 0L) {
        try {
            val resourcePath = "composeResources/merrpatenten.shared_core.design_system.generated.resources/files/dpshtrr_questionnaire.db"
            val stream = Res::class.java.classLoader?.getResourceAsStream(resourcePath)
                ?: Thread.currentThread().contextClassLoader?.getResourceAsStream(resourcePath)
            if (stream != null) {
                stream.use { input ->
                    dbFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            } else {
                val bytes = runBlocking { Res.readBytes("files/dpshtrr_questionnaire.db") }
                dbFile.writeBytes(bytes)
            }
        } catch (e: Exception) {
            println("Failed to copy pre-populated database on JVM: ${e.message}")
        }
    }
    return Room.databaseBuilder<MerrPatentenDatabase>(name = dbFile.absolutePath)
}

actual val platformDatabaseModule: Module = module {
    singleOf(::getDatabaseBuilder)
}
