package com.arsildo.merrpatenten.shared.feature.preferences.di

import com.arsildo.merrpatenten.shared.feature.preferences.PreferencesViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val preferencesModule = module {
    viewModelOf(::PreferencesViewModel)
}
