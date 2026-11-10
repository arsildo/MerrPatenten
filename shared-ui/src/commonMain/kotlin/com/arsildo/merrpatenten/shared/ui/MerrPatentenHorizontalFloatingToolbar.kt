package com.arsildo.merrpatenten.shared.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.icons.BookBookmark
import com.arsildo.merrpatenten.shared.core.designsystem.icons.BookOpen
import com.arsildo.merrpatenten.shared.core.designsystem.icons.GraphStar
import com.arsildo.merrpatenten.shared.core.designsystem.icons.House
import com.arsildo.merrpatenten.shared.core.designsystem.icons.Settings
import dev.chrisbanes.haze.HazeInput
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.blur.HazeBlurStyle
import dev.chrisbanes.haze.blur.hazeBlur
import dev.chrisbanes.haze.blur.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState

@Composable
internal fun MerrPatentenHorizontalFloatingToolbar(
    hazeState: HazeState,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    val backgroundColor = MaterialTheme.colorScheme.background

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        backgroundColor.copy(alpha = 0.7f),
                        backgroundColor,
                    ),
                ),
            ),
        contentAlignment = Alignment.BottomCenter,
    ) {
        HorizontalFloatingToolbar(
            expanded = true,
            colors = FloatingToolbarDefaults.standardFloatingToolbarColors(
                toolbarContainerColor = Color.Transparent,
            ),
            modifier = Modifier
                .navigationBarsPadding()
                .padding(top = 24.dp, bottom = 16.dp)
                .clip(FloatingToolbarDefaults.ContainerShape)
                .hazeBlur(
                    input = HazeInput.Backdrop(hazeState),
                    style = HazeMaterials.regular(),
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                    shape = FloatingToolbarDefaults.ContainerShape,
                )
                .selectableGroup(),
            content = content,
        )
    }
}

@Composable
internal fun FloatingToolbarNavItem(
    selected: Boolean,
    hazeState: HazeState,
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val hazeAlpha by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        label = "FloatingToolbarItemHazeAlpha",
    )
    val contentColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.onSecondaryContainer
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        label = "FloatingToolbarItemContent",
    )

    Row(
        modifier = modifier
            .height(48.dp)
            .clip(CircleShape)
            .hazeBlur(
                input = HazeInput.Backdrop(hazeState),
                style = HazeMaterials.thin(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ).then(
                    remember {
                        HazeBlurStyle {
                            alpha(hazeAlpha)
                        }
                    },
                ),
            )
            .clickable(
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onClick()
                },
            )
            .semantics {
                role = Role.Tab
                this.selected = selected
            }
            .padding(horizontal = if (selected) 16.dp else 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = contentColor,
            modifier = Modifier.size(24.dp),
        )
        AnimatedVisibility(
            visible = selected,
            enter = expandHorizontally(expandFrom = Alignment.Start) + fadeIn(),
            exit = shrinkHorizontally(shrinkTowards = Alignment.Start) + fadeOut(),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(Modifier.width(8.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview
@Composable
private fun FloatingToolbarNavItemSelectedPreview() {
    MerrPatentenTheme {
        FloatingToolbarNavItem(
            selected = true,
            hazeState = rememberHazeState(),
            icon = House,
            label = "Ballina",
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun FloatingToolbarNavItemUnselectedPreview() {
    MerrPatentenTheme {
        FloatingToolbarNavItem(
            selected = false,
            hazeState = rememberHazeState(),
            icon = BookOpen,
            label = "Sinjalistika",
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun MerrPatentenHorizontalFloatingToolbarPreview() {
    MerrPatentenTheme {
        val hazeState = rememberHazeState()
        MerrPatentenHorizontalFloatingToolbar(hazeState = hazeState) {
            FloatingToolbarNavItem(
                selected = true,
                hazeState = hazeState,
                icon = House,
                label = "Ballina",
                onClick = {},
            )
            FloatingToolbarNavItem(
                selected = false,
                hazeState = hazeState,
                icon = BookOpen,
                label = "Sinjalistika",
                onClick = {},
            )
            FloatingToolbarNavItem(
                selected = false,
                hazeState = hazeState,
                icon = BookBookmark,
                label = "Gabimet",
                onClick = {},
            )
            FloatingToolbarNavItem(
                selected = false,
                hazeState = hazeState,
                icon = GraphStar,
                label = "Statistikat",
                onClick = {},
            )
            FloatingToolbarNavItem(
                selected = false,
                hazeState = hazeState,
                icon = Settings,
                label = "Cilësimet",
                onClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun MerrPatentenHorizontalFloatingToolbarDarkPreview() {
    MerrPatentenTheme(darkTheme = true) {
        val hazeState = rememberHazeState()
        MerrPatentenHorizontalFloatingToolbar(hazeState = hazeState) {
            FloatingToolbarNavItem(
                selected = true,
                hazeState = hazeState,
                icon = House,
                label = "Ballina",
                onClick = {},
            )
            FloatingToolbarNavItem(
                selected = false,
                hazeState = hazeState,
                icon = BookOpen,
                label = "Sinjalistika",
                onClick = {},
            )
            FloatingToolbarNavItem(
                selected = false,
                hazeState = hazeState,
                icon = BookBookmark,
                label = "Gabimet",
                onClick = {},
            )
            FloatingToolbarNavItem(
                selected = false,
                hazeState = hazeState,
                icon = GraphStar,
                label = "Statistikat",
                onClick = {},
            )
            FloatingToolbarNavItem(
                selected = false,
                hazeState = hazeState,
                icon = Settings,
                label = "Cilësimet",
                onClick = {},
            )
        }
    }
}
