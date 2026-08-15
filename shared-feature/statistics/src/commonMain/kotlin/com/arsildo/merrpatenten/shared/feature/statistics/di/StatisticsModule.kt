package com.arsildo.merrpatenten.shared.feature.statistics.di

import com.arsildo.merrpatenten.shared.feature.statistics.StatisticsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val statisticsModule = module {
    viewModelOf(::StatisticsViewModel)
}
