package com.arsildo.merrpatenten.shared.core.designsystem

import androidx.compose.ui.graphics.Color

val md_theme_light_primary = Color(0xFF6750A4)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFFEADDFF)
val md_theme_light_onPrimaryContainer = Color(0xFF21005D)
val md_theme_light_secondary = Color(0xFF625B71)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFE8DEF8)
val md_theme_light_onSecondaryContainer = Color(0xFF1D192B)
val md_theme_light_tertiary = Color(0xFF7D5260)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFFFD8E4)
val md_theme_light_onTertiaryContainer = Color(0xFF31111D)
val md_theme_light_error = Color(0xFFBA1A1A)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_errorContainer = Color(0xFFFFDAD6)
val md_theme_light_onErrorContainer = Color(0xFF410002)
val md_theme_light_outline = Color(0xFF79747E)
val md_theme_light_background = Color(0xFFFEF7FF)
val md_theme_light_onBackground = Color(0xFF1D1B20)
val md_theme_light_surface = Color(0xFFFEF7FF)
val md_theme_light_onSurface = Color(0xFF1D1B20)
val md_theme_light_surfaceVariant = Color(0xFFE7E0EB)
val md_theme_light_onSurfaceVariant = Color(0xFF49454E)
val md_theme_light_inverseSurface = Color(0xFF322F35)
val md_theme_light_inverseOnSurface = Color(0xFFF5EFF7)
val md_theme_light_inversePrimary = Color(0xFFD0BCFF)
val md_theme_light_shadow = Color(0xFF000000)
val md_theme_light_surfaceTint = Color(0xFF6750A4)
val md_theme_light_outlineVariant = Color(0xFFCAC4CF)
val md_theme_light_scrim = Color(0xFF000000)
val md_theme_light_surfaceDim = Color(0xFFDED8E0)
val md_theme_light_surfaceBright = Color(0xFFFEF7FF)
val md_theme_light_surfaceContainerLowest = Color(0xFFFFFFFF)
val md_theme_light_surfaceContainerLow = Color(0xFFF8F1FA)
val md_theme_light_surfaceContainer = Color(0xFFF2ECF4)
val md_theme_light_surfaceContainerHigh = Color(0xFFECE6EE)
val md_theme_light_surfaceContainerHighest = Color(0xFFE6E0E9)

val md_theme_dark_primary = Color(0xFFD0BCFF)
val md_theme_dark_onPrimary = Color(0xFF381E72)
val md_theme_dark_primaryContainer = Color(0xFF4F378A)
val md_theme_dark_onPrimaryContainer = Color(0xFFEADDFF)
val md_theme_dark_secondary = Color(0xFFCCC2DC)
val md_theme_dark_onSecondary = Color(0xFF332D41)
val md_theme_dark_secondaryContainer = Color(0xFF4A4458)
val md_theme_dark_onSecondaryContainer = Color(0xFFE8DEF8)
val md_theme_dark_tertiary = Color(0xFFEFB8C8)
val md_theme_dark_onTertiary = Color(0xFF4A2532)
val md_theme_dark_tertiaryContainer = Color(0xFF633B48)
val md_theme_dark_onTertiaryContainer = Color(0xFFFFD8E4)
val md_theme_dark_error = Color(0xFFFFB4AB)
val md_theme_dark_onError = Color(0xFF690005)
val md_theme_dark_errorContainer = Color(0xFF93000A)
val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
val md_theme_dark_outline = Color(0xFF948F99)
val md_theme_dark_background = Color(0xFF141218)
val md_theme_dark_onBackground = Color(0xFFE6E0E9)
val md_theme_dark_surface = Color(0xFF141218)
val md_theme_dark_onSurface = Color(0xFFE6E0E9)
val md_theme_dark_surfaceVariant = Color(0xFF49454E)
val md_theme_dark_onSurfaceVariant = Color(0xFFCAC4CF)
val md_theme_dark_inverseSurface = Color(0xFFE6E0E9)
val md_theme_dark_inverseOnSurface = Color(0xFF322F35)
val md_theme_dark_inversePrimary = Color(0xFF6750A4)
val md_theme_dark_shadow = Color(0xFF000000)
val md_theme_dark_surfaceTint = Color(0xFFD0BCFF)
val md_theme_dark_outlineVariant = Color(0xFF49454E)
val md_theme_dark_scrim = Color(0xFF000000)
val md_theme_dark_surfaceDim = Color(0xFF141218)
val md_theme_dark_surfaceBright = Color(0xFF3B383E)
val md_theme_dark_surfaceContainerLowest = Color(0xFF0F0D13)
val md_theme_dark_surfaceContainerLow = Color(0xFF1D1B20)
val md_theme_dark_surfaceContainer = Color(0xFF211F24)
val md_theme_dark_surfaceContainerHigh = Color(0xFF2C292F)
val md_theme_dark_surfaceContainerHighest = Color(0xFF36343A)

// Expressive Semantic Colors
val Red = Color(0xFFBA1A1A)
val RedContainer = Color(0xFFFFDAD6)
val OnRedContainer = Color(0xFF410002)

val Green = Color(0xFF1E8E3E)
val GreenContainer = Color(0xFFC7F3C9)
val OnGreenContainer = Color(0xFF003915)

val RedDark = Color(0xFFFFB4AB)
val RedContainerDark = Color(0xFF93000A)
val OnRedContainerDark = Color(0xFFFFDAD6)

val GreenDark = Color(0xFF7DDA87)
val GreenContainerDark = Color(0xFF005322)
val OnGreenContainerDark = Color(0xFFC7F3C9)

data class SemanticColors(
    val success: Color,
    val onSuccess: Color,
    val successContainer: Color,
    val onSuccessContainer: Color,
)

val LightSemanticColors = SemanticColors(
    success = Green,
    onSuccess = Color.White,
    successContainer = GreenContainer,
    onSuccessContainer = OnGreenContainer,
)

val DarkSemanticColors = SemanticColors(
    success = GreenDark,
    onSuccess = Color(0xFF003915),
    successContainer = GreenContainerDark,
    onSuccessContainer = OnGreenContainerDark,
)

