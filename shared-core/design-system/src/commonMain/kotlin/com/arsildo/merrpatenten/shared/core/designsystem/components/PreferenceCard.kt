package com.arsildo.merrpatenten.shared.core.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Insights
import androidx.compose.material.icons.rounded.TouchApp
import androidx.compose.material.icons.rounded.Vibration
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PreferenceCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String = "",
    checked: Boolean,
    enabled: Boolean = true,
    index: Int = 0,
    count: Int = 1,
    leadingIcon: (@Composable () -> Unit)? = null,
    onCheckedChange: (Boolean) -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val handleCheckedChange: (Boolean) -> Unit = { newChecked ->
        hapticFeedback.performHapticFeedback(
            if (newChecked) HapticFeedbackType.ToggleOn else HapticFeedbackType.ToggleOff,
        )
        onCheckedChange(newChecked)
    }

    SegmentedListItem(
        selected = checked,
        onClick = { handleCheckedChange(!checked) },
        enabled = enabled,
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            selectedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        shapes = ListItemDefaults.segmentedShapes(index = index, count = count),
        leadingContent = leadingIcon,
        content = {
            Text(text = title)
        },
        supportingContent = if (subtitle.isNotEmpty()) {
            {
                Text(text = subtitle)
            }
        } else {
            null
        },
        trailingContent = {
            Switch(
                checked = checked,
                enabled = enabled,
                onCheckedChange = handleCheckedChange,
            )
        },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview
@Composable
private fun PreferenceCardGroupPreview() {
    MerrPatentenTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
        ) {
            PreferenceCard(
                title = "Butonat e navigimit",
                subtitle = "Shfaq butonat për të naviguar pyetjet",
                checked = true,
                index = 0,
                count = 3,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.TouchApp,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                },
                onCheckedChange = {},
            )
            PreferenceCard(
                title = "Ruaj statistikat",
                subtitle = "Ruaj rezultatet e provimeve lokalisht",
                checked = true,
                index = 1,
                count = 3,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Insights,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                },
                onCheckedChange = {},
            )
            PreferenceCard(
                title = "Dridhje",
                subtitle = "Reagim me dridhje gjatë përzgjedhjes",
                checked = false,
                index = 2,
                count = 3,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Vibration,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                },
                onCheckedChange = {},
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview
@Composable
private fun PreferenceCardDarkPreview() {
    MerrPatentenTheme(darkTheme = true) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
        ) {
            PreferenceCard(
                title = "Tema e errët",
                subtitle = "Përdor pamjen me ngjyra të errëta",
                checked = true,
                index = 0,
                count = 2,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.DarkMode,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                },
                onCheckedChange = {},
            )
            PreferenceCard(
                title = "Opsion i çaktivizuar",
                subtitle = "Ky opsion nuk mund të ndryshohet",
                checked = false,
                enabled = false,
                index = 1,
                count = 2,
                onCheckedChange = {},
            )
        }
    }
}
