package com.arsildo.merrpatenten.shared.core.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.TextStyle
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.StringResource

enum class QuestionTextSize(val key: String, val labelRes: StringResource) {
    Small(key = "small", labelRes = Res.string.text_size_small),
    Medium(key = "medium", labelRes = Res.string.text_size_medium),
    Large(key = "large", labelRes = Res.string.text_size_large),
    ;

    val textStyle: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = when (this) {
            Small -> MaterialTheme.typography.titleSmall
            Medium -> MaterialTheme.typography.titleMedium
            Large -> MaterialTheme.typography.titleLarge
        }

    companion object {
        val DEFAULT = Medium

        fun fromKey(key: String?): QuestionTextSize = entries.firstOrNull { it.key == key } ?: DEFAULT
    }
}
