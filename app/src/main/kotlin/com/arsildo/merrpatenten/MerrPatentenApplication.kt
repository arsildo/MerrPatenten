package com.arsildo.merrpatenten

import android.app.Application
import com.arsildo.merrpatenten.di.applicationModule
import com.arsildo.merrpatenten.di.dataStoreModule
import com.arsildo.merrpatenten.di.databaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class MerrPatentenApplication : Application() {
    @Override
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger()
            androidContext(this@MerrPatentenApplication)
            modules(
                applicationModule,
                dataStoreModule,
                databaseModule
            )
        }

    }

}