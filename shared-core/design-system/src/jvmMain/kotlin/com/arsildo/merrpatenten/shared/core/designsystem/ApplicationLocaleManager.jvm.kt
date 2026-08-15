package com.arsildo.merrpatenten.shared.core.designsystem

import java.util.Locale

actual object ApplicationLocaleManager : ApplicationLocaleManagerUseCase {
    private var currentLocale: String = "sq"

    override fun setDefaultLocale() {
        Locale.setDefault(Locale.forLanguageTag("sq"))
        currentLocale = "sq"
    }

    override fun getLocale(): String = currentLocale

    override fun setLocale(locale: ApplicationLocale) {
        currentLocale = locale.localeCode
        Locale.setDefault(Locale.forLanguageTag(locale.localeCode))
    }

    override fun restartApp() = Unit
}
