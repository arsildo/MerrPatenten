package com.arsildo.merrpatenten.shared.feature.exam.di

import com.arsildo.merrpatenten.shared.feature.exam.ExamViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val examModule = module {
    viewModel<ExamViewModel>()
}
