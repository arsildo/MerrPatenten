package com.arsildo.merrpatenten.shared.core.data.di

import com.arsildo.merrpatenten.shared.core.data.CatalogRepository
import com.arsildo.merrpatenten.shared.core.data.ExamResultsRepository
import com.arsildo.merrpatenten.shared.core.data.MistakesRepository
import com.arsildo.merrpatenten.shared.core.data.QuestionnaireRepository
import com.arsildo.merrpatenten.shared.core.database.di.databaseModule
import com.arsildo.merrpatenten.shared.core.datastore.PreferencesRepository
import com.arsildo.merrpatenten.shared.core.datastore.di.dataStoreModule
import org.koin.dsl.module
import org.koin.plugin.module.dsl.single

val dataModule = module {
    includes(databaseModule, dataStoreModule)
    single<QuestionnaireRepository>()
    single<ExamResultsRepository>()
    single<PreferencesRepository>()
    single<CatalogRepository>()
    single<MistakesRepository>()
}
