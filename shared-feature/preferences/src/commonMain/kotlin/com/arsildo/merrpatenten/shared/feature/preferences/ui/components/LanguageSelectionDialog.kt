package com.arsildo.merrpatenten.shared.feature.preferences.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.ApplicationLocale
import com.arsildo.merrpatenten.shared.core.designsystem.ApplicationLocaleManager
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.rememberApplicationLocale
import merrpatenten.shared_core.design_system.generated.resources.Res
import merrpatenten.shared_core.design_system.generated.resources.close
import merrpatenten.shared_core.design_system.generated.resources.language
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun LanguageSelectionDialog(
    onDismissRequest: () -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val currentLocale = rememberApplicationLocale()

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = stringResource(Res.string.language),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ApplicationLocale.entries.forEach { locale ->
                    val onSelectLocale = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentTick)
                        ApplicationLocaleManager.setLocale(locale)
                        onDismissRequest()
                    }
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        color = if (currentLocale == locale) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onSelectLocale)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(locale.res),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = if (currentLocale == locale) FontWeight.Bold else FontWeight.Normal,
                                color = if (currentLocale == locale) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                            )
                            RadioButton(
                                selected = currentLocale == locale,
                                onClick = onSelectLocale
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onDismissRequest()
                }
            ) {
                Text(text = stringResource(Res.string.close))
            }
        }
    )
}

@Preview
@Composable
private fun LanguageSelectionDialogPreview() {
    MerrPatentenTheme {
        LanguageSelectionDialog(
            onDismissRequest = {}
        )
    }
}

@Preview
@Composable
private fun LanguageSelectionDialogDarkPreview() {
    MerrPatentenTheme(darkTheme = true) {
        LanguageSelectionDialog(
            onDismissRequest = {}
        )
    }
}
