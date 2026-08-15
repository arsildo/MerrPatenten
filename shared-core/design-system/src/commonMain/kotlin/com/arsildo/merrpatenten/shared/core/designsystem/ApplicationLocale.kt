package com.arsildo.merrpatenten.shared.core.designsystem

import merrpatenten.shared_core.design_system.generated.resources.Res
import merrpatenten.shared_core.design_system.generated.resources.albanian
import merrpatenten.shared_core.design_system.generated.resources.english
import org.jetbrains.compose.resources.StringResource

enum class ApplicationLocale(
    val localeCode: String,
    val res: StringResource
) {
    English(localeCode = "en", res = Res.string.english),
    Albanian(localeCode = "sq", res = Res.string.albanian)
}
