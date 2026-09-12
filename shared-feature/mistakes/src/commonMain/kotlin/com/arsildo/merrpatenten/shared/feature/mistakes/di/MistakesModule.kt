package com.arsildo.merrpatenten.shared.feature.mistakes.di

import com.arsildo.merrpatenten.shared.feature.mistakes.MistakesPracticeViewModel
import com.arsildo.merrpatenten.shared.feature.mistakes.MistakesViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val mistakesModule = module {
    viewModel<MistakesViewModel>()
    viewModel<MistakesPracticeViewModel>()
}
