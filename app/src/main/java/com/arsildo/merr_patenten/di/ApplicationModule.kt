package com.arsildo.merr_patenten.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.arsildo.merr_patenten.data.local.QuestionnaireDAO
import com.arsildo.merr_patenten.data.local.QuestionnaireDatabase
import com.arsildo.merr_patenten.utils.DATABASE_PATH
import com.arsildo.merr_patenten.utils.DATASTORE_KEY
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Singleton
    @Provides
    fun provideDatastore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(DATASTORE_KEY) },
        )
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): QuestionnaireDatabase {
        return Room.databaseBuilder(
            context = context.applicationContext,
            klass = QuestionnaireDatabase::class.java,
            name = "questionnaire.db",
        ).createFromAsset(DATABASE_PATH)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideDatabaseDAO(database: QuestionnaireDatabase): QuestionnaireDAO {
        return database.questionnaireDAO()
    }

}