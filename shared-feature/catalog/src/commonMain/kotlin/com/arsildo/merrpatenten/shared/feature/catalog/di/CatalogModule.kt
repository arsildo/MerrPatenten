package com.arsildo.merrpatenten.shared.feature.catalog.di

import com.arsildo.merrpatenten.shared.feature.catalog.CatalogViewModel
import com.arsildo.merrpatenten.shared.feature.catalog.SignDetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val catalogModule = module {
    viewModelOf(::CatalogViewModel)
    viewModelOf(::SignDetailViewModel)
}
