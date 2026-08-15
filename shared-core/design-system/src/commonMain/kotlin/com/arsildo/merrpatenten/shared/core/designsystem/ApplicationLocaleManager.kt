package com.arsildo.merrpatenten.shared.core.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

internal interface ApplicationLocaleManagerUseCase {
    // When app first starts programmatically set the default locale
    fun setDefaultLocale()

    // Get the current locale set by user
    fun getLocale(): String

    // Set the locale
    fun setLocale(locale: ApplicationLocale)

    // For platforms requiring restart (iOS)
    fun restartApp()
}

expect object ApplicationLocaleManager : ApplicationLocaleManagerUseCase

@Composable
fun rememberApplicationLocale(): ApplicationLocale {
    val locale = ApplicationLocaleManager.getLocale()
    return remember(locale) {
        when {
            locale.startsWith(ApplicationLocale.Albanian.localeCode) -> ApplicationLocale.Albanian
            else -> ApplicationLocale.English
        }
    }
}
