package com.arsildo.merrpatenten.shared.ui.di

import com.arsildo.merrpatenten.shared.core.data.di.dataModule
import com.arsildo.merrpatenten.shared.feature.dashboard.di.dashboardModule
import com.arsildo.merrpatenten.shared.feature.exam.di.examModule
import com.arsildo.merrpatenten.shared.feature.preferences.di.preferencesModule
import com.arsildo.merrpatenten.shared.feature.statistics.di.statisticsModule
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatformTools

val appModules = listOf(
    dataModule,
    dashboardModule,
    examModule,
    statisticsModule,
    preferencesModule,
)

fun initKoin() {
    if (KoinPlatformTools.defaultContext().getOrNull() == null) {
        startKoin {
            modules(appModules)
        }
    }
}
