package com.arsildo.merrpatenten.shared.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalFloatingToolbar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.icons.BookBookmark
import com.arsildo.merrpatenten.shared.core.designsystem.icons.BookOpen
import com.arsildo.merrpatenten.shared.core.designsystem.icons.GraphStar
import com.arsildo.merrpatenten.shared.core.designsystem.icons.House
import com.arsildo.merrpatenten.shared.core.designsystem.icons.Settings

@Composable
internal fun MerrPatentenVerticalFloatingToolbar(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    VerticalFloatingToolbar(
        expanded = true,
        modifier = modifier
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(WindowInsetsSides.Start + WindowInsetsSides.Vertical),
            )
            .padding(16.dp)
            .clip(FloatingToolbarDefaults.ContainerShape)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                shape = FloatingToolbarDefaults.ContainerShape,
            )
            .selectableGroup(),
        content = content,
    )
}

@Composable
internal fun VerticalFloatingToolbarNavItem(
    selected: Boolean,
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val containerColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            Color.Transparent
        },
        label = "VerticalFloatingToolbarItemContainer",
    )
    val contentColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.onSecondaryContainer
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        label = "VerticalFloatingToolbarItemContent",
    )

    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(containerColor)
            .clickable(
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onClick()
                },
            )
            .semantics {
                role = Role.Tab
                this.selected = selected
            },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = contentColor,
            modifier = Modifier.size(24.dp),
        )
    }
}

@Preview
@Composable
private fun VerticalFloatingToolbarNavItemSelectedPreview() {
    MerrPatentenTheme {
        VerticalFloatingToolbarNavItem(
            selected = true,
            icon = House,
            label = "Ballina",
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun VerticalFloatingToolbarNavItemUnselectedPreview() {
    MerrPatentenTheme {
        VerticalFloatingToolbarNavItem(
            selected = false,
            icon = BookOpen,
            label = "Sinjalistika",
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun MerrPatentenVerticalFloatingToolbarPreview() {
    MerrPatentenTheme {
        MerrPatentenVerticalFloatingToolbar {
            VerticalFloatingToolbarNavItem(
                selected = true,
                icon = House,
                label = "Ballina",
                onClick = {},
            )
            VerticalFloatingToolbarNavItem(
                selected = false,
                icon = BookOpen,
                label = "Sinjalistika",
                onClick = {},
            )
            VerticalFloatingToolbarNavItem(
                selected = false,
                icon = BookBookmark,
                label = "Gabimet",
                onClick = {},
            )
            VerticalFloatingToolbarNavItem(
                selected = false,
                icon = GraphStar,
                label = "Statistikat",
                onClick = {},
            )
            VerticalFloatingToolbarNavItem(
                selected = false,
                icon = Settings,
                label = "Cilësimet",
                onClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun MerrPatentenVerticalFloatingToolbarDarkPreview() {
    MerrPatentenTheme(darkTheme = true) {
        MerrPatentenVerticalFloatingToolbar {
            VerticalFloatingToolbarNavItem(
                selected = true,
                icon = House,
                label = "Ballina",
                onClick = {},
            )
            VerticalFloatingToolbarNavItem(
                selected = false,
                icon = BookOpen,
                label = "Sinjalistika",
                onClick = {},
            )
            VerticalFloatingToolbarNavItem(
                selected = false,
                icon = BookBookmark,
                label = "Gabimet",
                onClick = {},
            )
            VerticalFloatingToolbarNavItem(
                selected = false,
                icon = GraphStar,
                label = "Statistikat",
                onClick = {},
            )
            VerticalFloatingToolbarNavItem(
                selected = false,
                icon = Settings,
                label = "Cilësimet",
                onClick = {},
            )
        }
    }
}
