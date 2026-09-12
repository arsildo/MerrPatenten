package com.arsildo.merrpatenten.shared.feature.catalog.di

import com.arsildo.merrpatenten.shared.feature.catalog.CatalogViewModel
import com.arsildo.merrpatenten.shared.feature.catalog.SignDetailViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val catalogModule = module {
    viewModel<CatalogViewModel>()
    viewModel<SignDetailViewModel>()
}
