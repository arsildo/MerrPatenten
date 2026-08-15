package com.arsildo.merrpatenten.shared.feature.dashboard.di

import com.arsildo.merrpatenten.shared.feature.dashboard.DashboardViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val dashboardModule = module {
    viewModelOf(::DashboardViewModel)
}
