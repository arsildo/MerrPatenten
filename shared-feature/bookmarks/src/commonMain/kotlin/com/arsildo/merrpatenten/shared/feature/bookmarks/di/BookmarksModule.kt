package com.arsildo.merrpatenten.shared.feature.bookmarks.di

import com.arsildo.merrpatenten.shared.feature.bookmarks.BookmarksPracticeViewModel
import com.arsildo.merrpatenten.shared.feature.bookmarks.BookmarksViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val bookmarksModule = module {
    viewModel<BookmarksViewModel>()
    viewModel<BookmarksPracticeViewModel>()
}
