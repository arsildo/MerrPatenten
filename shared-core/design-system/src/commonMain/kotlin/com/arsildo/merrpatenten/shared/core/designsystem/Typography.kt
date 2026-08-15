package com.arsildo.merrpatenten.shared.core.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import merrpatenten.shared_core.design_system.generated.resources.GoogleSansFlex
import merrpatenten.shared_core.design_system.generated.resources.Res
import org.jetbrains.compose.resources.Font

@Composable
fun GoogleSansFlexTypography(): Typography {
    val googleSansFlex = FontFamily(
        Font(
            resource = Res.font.GoogleSansFlex,
            variationSettings = FontVariation.Settings(
                FontVariation.grade(value = 20),
                FontVariation.Setting(name = "ROND", value = 100f)
            )
        )
    )

    return with(MaterialTheme.typography) {
        copy(
            displayLarge = displayLarge.copy(fontFamily = googleSansFlex),
            displayMedium = displayMedium.copy(fontFamily = googleSansFlex),
            displaySmall = displaySmall.copy(fontFamily = googleSansFlex),
            headlineLarge = headlineLarge.copy(fontFamily = googleSansFlex),
            headlineMedium = headlineMedium.copy(fontFamily = googleSansFlex),
            headlineSmall = headlineSmall.copy(fontFamily = googleSansFlex),
            titleLarge = titleLarge.copy(fontFamily = googleSansFlex),
            titleMedium = titleMedium.copy(fontFamily = googleSansFlex),
            titleSmall = titleSmall.copy(fontFamily = googleSansFlex),
            labelLarge = labelLarge.copy(fontFamily = googleSansFlex),
            labelMedium = labelMedium.copy(fontFamily = googleSansFlex),
            labelSmall = labelSmall.copy(fontFamily = googleSansFlex),
            bodyLarge = bodyLarge.copy(fontFamily = googleSansFlex),
            bodyMedium = bodyMedium.copy(fontFamily = googleSansFlex),
            bodySmall = bodySmall.copy(fontFamily = googleSansFlex),
        )
    }
}
