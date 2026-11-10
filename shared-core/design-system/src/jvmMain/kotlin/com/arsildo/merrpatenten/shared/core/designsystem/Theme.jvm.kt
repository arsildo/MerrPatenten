package com.arsildo.merrpatenten.shared.core.designsystem

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

actual val supportsDynamicColor: Boolean = false

@Composable
actual fun getColorScheme(darkTheme: Boolean, dynamicColor: Boolean): ColorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
