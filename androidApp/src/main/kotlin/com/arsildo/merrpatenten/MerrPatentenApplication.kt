package com.arsildo.merrpatenten

import android.app.Application
import com.arsildo.merrpatenten.shared.ui.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androix.startup.KoinStartup
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.koinConfiguration

@OptIn(KoinExperimentalAPI::class)
class MerrPatentenApplication : Application(), KoinStartup {
    override fun onKoinStartup(): KoinConfiguration {
        return koinConfiguration {
            androidLogger()
            androidContext(this@MerrPatentenApplication)
            modules(appModules)
        }
    }
}
