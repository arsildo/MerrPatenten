package com.arsildo.merrpatenten.shared.feature.preferences.di

import com.arsildo.merrpatenten.shared.feature.preferences.PreferencesViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val preferencesModule = module {
    viewModel<PreferencesViewModel>()
}
