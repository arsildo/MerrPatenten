package com.arsildo.merrpatenten.shared.ui.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.mp.KoinPlatformTools

fun initKoin(appDeclaration: KoinAppDeclaration) {
    if (KoinPlatformTools.defaultContext().getOrNull() == null) {
        startKoin {
            appDeclaration()
            modules(appModules)
        }
    }
}
