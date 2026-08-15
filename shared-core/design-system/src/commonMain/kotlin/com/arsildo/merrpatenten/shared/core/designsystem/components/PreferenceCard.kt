package com.arsildo.merrpatenten.shared.core.designsystem.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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
    SegmentedListItem(
        selected = checked,
        onClick = { onCheckedChange(!checked) },
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
        } else null,
        trailingContent = {
            Switch(
                checked = checked,
                enabled = enabled,
                onCheckedChange = onCheckedChange
            )
        },
        modifier = modifier
    )
}
