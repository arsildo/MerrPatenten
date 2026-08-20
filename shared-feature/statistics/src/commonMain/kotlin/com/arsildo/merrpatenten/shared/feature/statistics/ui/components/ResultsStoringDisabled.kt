package com.arsildo.merrpatenten.shared.feature.statistics.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MobiledataOff
import androidx.compose.material.icons.rounded.MultipleStop
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme

@Composable
internal fun ResultStoringDisabled(text: String, icon: ImageVector, changePreference: @Composable () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceContainerHighest,
                    modifier = Modifier.size(72.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(36.dp),
                        )
                    }
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                changePreference()
            }
        }
    }
}

@Preview
@Composable
private fun ResultStoringDisabledPreview() {
    MerrPatentenTheme {
        ResultStoringDisabled(
            text = "Ruajtja e rezultateve të provimeve është e çaktivizuar.",
            icon = Icons.Rounded.MobiledataOff,
            changePreference = {
                Button(onClick = {}) {
                    Text(text = "Ndrysho cilësimin")
                }
            },
        )
    }
}

@Preview
@Composable
private fun ResultsEmptyPreview() {
    MerrPatentenTheme {
        ResultStoringDisabled(
            text = "Nuk keni kryer asnjë provim ende.",
            icon = Icons.Rounded.MultipleStop,
        )
    }
}

@Preview
@Composable
private fun ResultStoringDisabledDarkPreview() {
    MerrPatentenTheme(darkTheme = true) {
        ResultStoringDisabled(
            text = "Ruajtja e rezultateve të provimeve është e çaktivizuar.",
            icon = Icons.Rounded.MobiledataOff,
        )
    }
}
