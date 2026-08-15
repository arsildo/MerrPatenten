package com.arsildo.merrpatenten.shared.core.datastore.di

import com.arsildo.merrpatenten.shared.core.datastore.createDataStoreJVM
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val dataStoreModule: Module = module {
    singleOf(::createDataStoreJVM)
}
