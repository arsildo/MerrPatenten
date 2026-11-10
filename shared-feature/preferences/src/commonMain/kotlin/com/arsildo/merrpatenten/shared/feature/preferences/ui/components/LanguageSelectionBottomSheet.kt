package com.arsildo.merrpatenten.shared.feature.preferences.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import merrpatenten.shared_core.design_system.generated.resources.language
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun LanguageSelectionBottomSheet(modifier: Modifier = Modifier) {
    val hapticFeedback = LocalHapticFeedback.current
    val initialLocale = rememberApplicationLocale()
    var currentLocale by remember(initialLocale) { mutableStateOf(initialLocale) }
    val locales = ApplicationLocale.entries

    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp)
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(Res.string.language),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup(),
            verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
        ) {
            locales.forEachIndexed { index, locale ->
                val isSelected = currentLocale == locale
                val onSelectLocale = {
                    if (!isSelected) {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentTick)
                        currentLocale = locale
                        ApplicationLocaleManager.setLocale(locale)
                    }
                }
                SegmentedListItem(
                    selected = isSelected,
                    onClick = onSelectLocale,
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        selectedLeadingContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        selectedTrailingContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                    shapes = ListItemDefaults.segmentedShapes(index = index, count = locales.size),
                    leadingContent = {
                        RadioButton(
                            selected = isSelected,
                            onClick = null,
                        )
                    },
                    content = {
                        Text(
                            text = stringResource(locale.res),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        )
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun LanguageSelectionBottomSheetPreview() {
    MerrPatentenTheme {
        Surface {
            LanguageSelectionBottomSheet()
        }
    }
}

@Preview
@Composable
private fun LanguageSelectionBottomSheetDarkPreview() {
    MerrPatentenTheme(darkTheme = true) {
        Surface {
            LanguageSelectionBottomSheet()
        }
    }
}
