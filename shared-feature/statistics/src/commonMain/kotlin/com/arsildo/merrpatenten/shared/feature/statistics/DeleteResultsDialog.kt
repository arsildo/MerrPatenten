package com.arsildo.merrpatenten.shared.feature.statistics

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.Red
import merrpatenten.shared_core.design_system.generated.resources.Res
import merrpatenten.shared_core.design_system.generated.resources.results_delete
import merrpatenten.shared_core.design_system.generated.resources.results_delete_description
import merrpatenten.shared_core.design_system.generated.resources.results_dismiss
import org.jetbrains.compose.resources.stringResource

@Composable
fun DeleteResultsDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = null,
                tint = Red
            )
        },
        title = {
            Text(text = stringResource(Res.string.results_delete))
        },
        text = {
            Text(text = stringResource(Res.string.results_delete_description))
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Red,
                    contentColor = Color.White
                )
            ) {
                Text(text = stringResource(Res.string.results_delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(Res.string.results_dismiss))
            }
        }
    )
}

@Preview
@Composable
private fun DeleteResultsDialogPreview() {
    MerrPatentenTheme {
        DeleteResultsDialog(
            onConfirm = {},
            onDismiss = {}
        )
    }
}

