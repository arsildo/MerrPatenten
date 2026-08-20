package com.arsildo.merrpatenten.shared.core.designsystem.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import merrpatenten.shared_core.design_system.generated.resources.Res
import merrpatenten.shared_core.design_system.generated.resources.end_exam
import merrpatenten.shared_core.design_system.generated.resources.exit_exam
import merrpatenten.shared_core.design_system.generated.resources.restart_exam
import org.jetbrains.compose.resources.stringResource

@Composable
fun EndExamButton(
    modifier: Modifier = Modifier,
    title: String = stringResource(Res.string.end_exam),
    icon: ImageVector,
    onClick: () -> Unit,
) {
    ActionButton(
        title = title,
        icon = icon,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        modifier = modifier,
    )
}

@Composable
fun RestartExamButton(
    modifier: Modifier = Modifier,
    title: String = stringResource(Res.string.restart_exam),
    icon: ImageVector,
    onClick: () -> Unit,
) {
    ActionButton(
        title = title,
        icon = icon,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
        ),
        modifier = modifier,
    )
}

@Composable
fun ExitExamButton(
    modifier: Modifier = Modifier,
    title: String = stringResource(Res.string.exit_exam),
    icon: ImageVector,
    onClick: () -> Unit,
) {
    ActionButton(
        title = title,
        icon = icon,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError,
        ),
        modifier = modifier,
    )
}

@Composable
private fun ActionButton(modifier: Modifier = Modifier, title: String, icon: ImageVector, onClick: () -> Unit, colors: ButtonColors) {
    val hapticFeedback = LocalHapticFeedback.current
    Button(
        onClick = {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            onClick()
        },
        shapes = ButtonShapes(
            shape = MaterialTheme.shapes.large,
            pressedShape = MaterialTheme.shapes.small,
        ),
        contentPadding = PaddingValues(16.dp),
        colors = colors,
        modifier = modifier,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(22.dp),
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Preview
@Composable
private fun ExamActionButtonsPreview() {
    MerrPatentenTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            EndExamButton(
                icon = Icons.Rounded.DoneAll,
                onClick = {},
            )
            RestartExamButton(
                icon = Icons.Rounded.RestartAlt,
                onClick = {},
            )
            ExitExamButton(
                icon = Icons.AutoMirrored.Rounded.ExitToApp,
                onClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun ExamActionButtonsDarkPreview() {
    MerrPatentenTheme(darkTheme = true) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            EndExamButton(
                icon = Icons.Rounded.DoneAll,
                onClick = {},
            )
            RestartExamButton(
                icon = Icons.Rounded.RestartAlt,
                onClick = {},
            )
            ExitExamButton(
                icon = Icons.AutoMirrored.Rounded.ExitToApp,
                onClick = {},
            )
        }
    }
}
