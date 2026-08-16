package com.arsildo.merrpatenten.shared.core.designsystem

import java.util.Locale

actual object ApplicationLocaleManager : ApplicationLocaleManagerUseCase {
    private var currentLocale: String = "sq"

    actual override fun setDefaultLocale() {
        Locale.setDefault(Locale.forLanguageTag("sq"))
        currentLocale = "sq"
    }

    actual override fun getLocale(): String = currentLocale

    actual override fun setLocale(locale: ApplicationLocale) {
        currentLocale = locale.localeCode
        Locale.setDefault(Locale.forLanguageTag(locale.localeCode))
    }

    actual override fun restartApp() = Unit
}
