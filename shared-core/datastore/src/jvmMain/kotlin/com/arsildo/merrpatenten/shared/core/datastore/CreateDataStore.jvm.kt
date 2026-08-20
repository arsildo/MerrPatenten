package com.arsildo.merrpatenten.shared.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import java.io.File

fun createDataStoreJVM(): DataStore<Preferences> = createDataStore(
    producePath = {
        val appData = File(System.getProperty("user.home"), ".merrpatenten")
        if (!appData.exists()) appData.mkdirs()
        File(appData, DATA_STORE_FILE_NAME).absolutePath
    },
)
