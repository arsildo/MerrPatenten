package com.arsildo.merrpatenten.di

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.arsildo.merrpatenten.dashboard.DashboardViewModel
import com.arsildo.merrpatenten.data.local.ExamResultsDatabase
import com.arsildo.merrpatenten.data.local.PreferencesRepository
import com.arsildo.merrpatenten.data.local.QuestionnaireDatabase
import com.arsildo.merrpatenten.data.local.QuestionnaireRepository
import com.arsildo.merrpatenten.exam.ExamViewModel
import com.arsildo.merrpatenten.preferences.PreferencesViewModel
import com.arsildo.merrpatenten.statistics.StatisticsViewModel
import com.arsildo.merrpatenten.utils.DATABASE_PATH
import com.arsildo.merrpatenten.utils.DATASTORE_KEY
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val applicationModule = module {
    viewModelOf(::DashboardViewModel)
    viewModelOf(::ExamViewModel)
    viewModelOf(::PreferencesViewModel)
    viewModelOf(::StatisticsViewModel)
}

val dataStoreModule = module {
    single {
        PreferenceDataStoreFactory.create(
            produceFile = { androidContext().preferencesDataStoreFile(DATASTORE_KEY) }
        )
    }
    singleOf(::PreferencesRepository)
}

val databaseModule = module {
    single {
        Room.databaseBuilder(
            context = androidContext().applicationContext,
            klass = QuestionnaireDatabase::class.java,
            name = "questionnaire.db",
        ).createFromAsset(DATABASE_PATH)
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }
    single { get<QuestionnaireDatabase>().questionnaireDAO() }
    singleOf(::QuestionnaireRepository)

    single {
        Room.databaseBuilder(
            context = androidContext().applicationContext,
            klass = ExamResultsDatabase::class.java,
            name = "examResults.db",
        )
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }
    single { get<ExamResultsDatabase>().examResultsDAO() }
}