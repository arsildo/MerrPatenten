package com.arsildo.merrpatenten.statistics

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.arsildo.merrpatenten.R

@Composable
fun DeleteResultsDialog(
    onConfirm : () -> Unit,
    onDismiss : () -> Unit,
) {
    AlertDialog(
        title = { Text(text = stringResource(id = R.string.results_delete)) },
        text = { Text(text = stringResource(id = R.string.results_delete_description)) },
        onDismissRequest = onDismiss,
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(text = stringResource(id = R.string.results_dismiss))
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
                Text(text = stringResource(id = R.string.results_confirm))
            }
        },
        tonalElevation = 0.dp,
        iconContentColor = MaterialTheme.colorScheme.error,
        modifier = Modifier.fillMaxWidth(),
    )
}