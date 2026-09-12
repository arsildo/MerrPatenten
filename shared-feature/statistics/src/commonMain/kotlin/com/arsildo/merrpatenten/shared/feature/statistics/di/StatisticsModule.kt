package com.arsildo.merrpatenten.shared.feature.statistics.di

import com.arsildo.merrpatenten.shared.feature.statistics.StatisticsViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val statisticsModule = module {
    viewModel<StatisticsViewModel>()
}
