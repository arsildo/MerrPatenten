package com.arsildo.merrpatenten.shared.core.data.di

import com.arsildo.merrpatenten.shared.core.data.ExamResultsRepository
import com.arsildo.merrpatenten.shared.core.data.QuestionnaireRepository
import com.arsildo.merrpatenten.shared.core.database.di.databaseModule
import com.arsildo.merrpatenten.shared.core.datastore.PreferencesRepository
import com.arsildo.merrpatenten.shared.core.datastore.di.dataStoreModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    includes(databaseModule, dataStoreModule)
    singleOf(::QuestionnaireRepository)
    singleOf(::ExamResultsRepository)
    singleOf(::PreferencesRepository)
}
