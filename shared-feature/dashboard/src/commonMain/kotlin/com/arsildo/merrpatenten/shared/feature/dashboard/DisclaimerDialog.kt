package com.arsildo.merrpatenten.shared.feature.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.DPSHTRR_HELP
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import androidx.compose.ui.tooling.preview.Preview
import merrpatenten.shared_core.design_system.generated.resources.Res
import merrpatenten.shared_core.design_system.generated.resources.disclaimer_description
import merrpatenten.shared_core.design_system.generated.resources.disclaimer_title
import merrpatenten.shared_core.design_system.generated.resources.results_confirm
import org.jetbrains.compose.resources.stringResource

@Composable
fun DisclaimerDialog(
    onDismissRequest: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current
    AlertDialog(
        onDismissRequest = onDismissRequest,
        icon = {
            Icon(
                imageVector = Icons.Outlined.WarningAmber,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = {
            Text(text = stringResource(Res.string.disclaimer_title))
        },
        text = {
            Text(
                text = stringResource(Res.string.disclaimer_description),
                modifier = Modifier.clickable {
                    uriHandler.openUri(DPSHTRR_HELP)
                }
            )
        },
        confirmButton = {
            Button(
                onClick = onDismissRequest,
                content = {
                    Text(text = stringResource(Res.string.results_confirm))
                }
            )
        },
        tonalElevation = 2.dp,
    )
}

@Preview
@Composable
private fun DisclaimerDialogPreview() {
    MerrPatentenTheme {
        DisclaimerDialog(
            onDismissRequest = {}
        )
    }
}

