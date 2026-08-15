package com.arsildo.merrpatenten.shared.core.database.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.arsildo.merrpatenten.shared.core.database.MerrPatentenDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.runBlocking
import merrpatenten.shared_core.design_system.generated.resources.Res
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import platform.Foundation.*

@OptIn(ExperimentalForeignApi::class)
internal fun getDatabaseBuilder(): RoomDatabase.Builder<MerrPatentenDatabase> {
    val dbFilePath = documentDirectory() + "/merrpatenten.db"
    val fileManager = NSFileManager.defaultManager
    if (!fileManager.fileExistsAtPath(dbFilePath)) {
        try {
            val bytes = runBlocking { Res.readBytes("files/dpshtrr_questionnaire.db") }
            bytes.usePinned { pinned ->
                val nsData = NSData.dataWithBytes(pinned.addressOf(0), bytes.size.toULong())
                nsData.writeToFile(dbFilePath, true)
            }
        } catch (e: Exception) {
            println("Failed to copy pre-populated database on iOS: ${e.message}")
        }
    }
    return Room.databaseBuilder<MerrPatentenDatabase>(name = dbFilePath)
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}

actual val platformDatabaseModule: Module = module {
    singleOf(::getDatabaseBuilder)
}
