package com.arsildo.merrpatenten.shared.core.designsystem

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

actual object ApplicationLocaleManager : ApplicationLocaleManagerUseCase {
    override fun setDefaultLocale() {
        if (AppCompatDelegate.getApplicationLocales().toLanguageTags().isEmpty()) {
            setLocale(ApplicationLocale.Albanian)
        }
    }

    override fun getLocale(): String =
        AppCompatDelegate.getApplicationLocales().toLanguageTags()

    override fun setLocale(locale: ApplicationLocale) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(locale.localeCode))
    }

    override fun restartApp() = Unit
}
