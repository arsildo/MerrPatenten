package com.arsildo.merrpatenten

import android.app.Application
import com.arsildo.merrpatenten.shared.ui.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MerrPatentenApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@MerrPatentenApplication)
        }
    }
}
