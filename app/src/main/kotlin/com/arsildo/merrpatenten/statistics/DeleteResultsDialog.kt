package com.arsildo.merrpatenten.statistics

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Composable
fun DeleteResultsDialog(
    onConfirm : () -> Unit,
    onDismiss : () -> Unit,
) {
    AlertDialog(
        title = { Text(text = "Delete all?") },
        text = { Text(text = "Are you sure you want to delete all records?") },
        onDismissRequest = onDismiss,
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(text = "Dismiss")
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Text(text = "Confirm")
            }
        },
        tonalElevation = 0.dp,
        iconContentColor = MaterialTheme.colorScheme.error,
    )
}