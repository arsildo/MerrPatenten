package com.arsildo.merrpatenten.shared.core.designsystem

import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

actual object ApplicationLocaleManager : ApplicationLocaleManagerUseCase {
    private const val INITIAL_LOCALE_KEY = "initialLocale"
    private const val APPLE_LANGUAGES_KEY = "AppleLanguages"

    override fun setDefaultLocale() {
        val userDefaults = NSUserDefaults.standardUserDefaults
        if (userDefaults.stringForKey(defaultName = INITIAL_LOCALE_KEY) == null) {
            setLocale(ApplicationLocale.Albanian)
            userDefaults.setObject(ApplicationLocale.Albanian.localeCode, forKey = INITIAL_LOCALE_KEY)
            userDefaults.synchronize()
        }
    }

    override fun getLocale(): String = NSLocale.currentLocale.languageCode

    override fun setLocale(locale: ApplicationLocale) {
        NSUserDefaults.standardUserDefaults.setObject(
            value = arrayListOf(locale.localeCode),
            forKey = APPLE_LANGUAGES_KEY
        )
        NSUserDefaults.standardUserDefaults.synchronize()
    }

    override fun restartApp() {
        kotlin.system.exitProcess(status = 0)
    }
}
