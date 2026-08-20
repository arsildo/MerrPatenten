package com.arsildo.merrpatenten.shared.feature.dashboard.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonShapes
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.DPSHTRR_HELP
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import merrpatenten.shared_core.design_system.generated.resources.Res
import merrpatenten.shared_core.design_system.generated.resources.disclaimer_description
import merrpatenten.shared_core.design_system.generated.resources.disclaimer_title
import merrpatenten.shared_core.design_system.generated.resources.results_confirm
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun DisclaimerDialog(onDismissRequest: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    AlertDialog(
        onDismissRequest = onDismissRequest,
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
                        imageVector = Icons.Outlined.WarningAmber,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.size(28.dp),
                    )
                }
            }
        },
        title = {
            Text(
                text = stringResource(Res.string.disclaimer_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        text = {
            Text(
                text = stringResource(Res.string.disclaimer_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.clickable {
                    uriHandler.openUri(DPSHTRR_HELP)
                },
            )
        },
        confirmButton = {
            Button(
                onClick = onDismissRequest,
                shapes = ButtonShapes(
                    shape = MaterialTheme.shapes.medium,
                    pressedShape = MaterialTheme.shapes.small,
                ),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                content = {
                    Text(
                        text = stringResource(Res.string.results_confirm),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                },
            )
        },
    )
}

@Preview
@Composable
private fun DisclaimerDialogPreview() {
    MerrPatentenTheme {
        DisclaimerDialog(
            onDismissRequest = {},
        )
    }
}

@Preview
@Composable
private fun DisclaimerDialogDarkPreview() {
    MerrPatentenTheme(darkTheme = true) {
        DisclaimerDialog(
            onDismissRequest = {},
        )
    }
}
