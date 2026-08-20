package com.arsildo.merrpatenten.shared.feature.statistics.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import merrpatenten.shared_core.design_system.generated.resources.Res
import merrpatenten.shared_core.design_system.generated.resources.results_delete
import merrpatenten.shared_core.design_system.generated.resources.results_delete_description
import merrpatenten.shared_core.design_system.generated.resources.results_dismiss
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun DeleteResultsDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    val hapticFeedback = LocalHapticFeedback.current
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = MaterialTheme.shapes.extraLarge,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        icon = {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.errorContainer,
                modifier = Modifier.size(56.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.size(28.dp),
                    )
                }
            }
        },
        title = {
            Text(
                text = stringResource(Res.string.results_delete),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        text = {
            Text(
                text = stringResource(Res.string.results_delete_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    onConfirm()
                },
                shapes = ButtonShapes(
                    shape = MaterialTheme.shapes.medium,
                    pressedShape = MaterialTheme.shapes.small,
                ),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                ),
            ) {
                Text(
                    text = stringResource(Res.string.results_delete),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onDismiss()
                },
                shapes = ButtonShapes(
                    shape = MaterialTheme.shapes.medium,
                    pressedShape = MaterialTheme.shapes.small,
                ),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            ) {
                Text(
                    text = stringResource(Res.string.results_dismiss),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        },
    )
}

@Preview
@Composable
private fun DeleteResultsDialogPreview() {
    MerrPatentenTheme {
        DeleteResultsDialog(
            onConfirm = {},
            onDismiss = {},
        )
    }
}

@Preview
@Composable
private fun DeleteResultsDialogDarkPreview() {
    MerrPatentenTheme(darkTheme = true) {
        DeleteResultsDialog(
            onConfirm = {},
            onDismiss = {},
        )
    }
}
