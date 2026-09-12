package com.arsildo.merrpatenten.shared.feature.dashboard.di

import com.arsildo.merrpatenten.shared.feature.dashboard.DashboardViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val dashboardModule = module {
    viewModel<DashboardViewModel>()
}
