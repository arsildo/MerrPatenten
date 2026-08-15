package com.arsildo.merrpatenten.shared.feature.exam.di

import com.arsildo.merrpatenten.shared.feature.exam.ExamViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val examModule = module {
    viewModelOf(::ExamViewModel)
}
