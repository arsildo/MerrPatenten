package com.arsildo.merrpatenten.shared.core.designsystem

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

actual object ApplicationLocaleManager : ApplicationLocaleManagerUseCase {
    actual override fun setDefaultLocale() {
        if (AppCompatDelegate.getApplicationLocales().toLanguageTags().isEmpty()) {
            setLocale(ApplicationLocale.Albanian)
        }
    }

    actual override fun getLocale(): String = AppCompatDelegate.getApplicationLocales().toLanguageTags()

    actual override fun setLocale(locale: ApplicationLocale) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(locale.localeCode))
    }

    actual override fun restartApp() = Unit
}
