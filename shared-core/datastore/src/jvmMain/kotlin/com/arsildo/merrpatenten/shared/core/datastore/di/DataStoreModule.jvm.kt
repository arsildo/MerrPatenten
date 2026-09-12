package com.arsildo.merrpatenten.shared.core.datastore.di

import com.arsildo.merrpatenten.shared.core.datastore.createDataStoreJVM
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import org.koin.plugin.module.dsl.single

actual val dataStoreModule: Module = module {
    single { create(::createDataStoreJVM) }
}
