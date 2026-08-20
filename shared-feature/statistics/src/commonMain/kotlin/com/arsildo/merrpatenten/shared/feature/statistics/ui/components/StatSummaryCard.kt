package com.arsildo.merrpatenten.shared.feature.statistics.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.semanticColors

@Composable
internal fun StatSummaryCard(
    title: String,
    value: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        color = containerColor,
        contentColor = contentColor,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(22.dp),
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = contentColor.copy(alpha = 0.8f),
            )
        }
    }
}

@Preview
@Composable
private fun StatSummaryCardsRowPreview() {
    MerrPatentenTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            StatSummaryCard(
                title = "Provime",
                value = "12",
                icon = Icons.Rounded.Quiz,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.weight(1f),
            )
            StatSummaryCard(
                title = "Mesatarja",
                value = "2.4",
                icon = Icons.Rounded.CheckCircle,
                containerColor = MaterialTheme.semanticColors.successContainer,
                contentColor = MaterialTheme.semanticColors.onSuccessContainer,
                modifier = Modifier.weight(1f),
            )
            StatSummaryCard(
                title = "Suksesi",
                value = "85%",
                icon = Icons.Rounded.EmojiEvents,
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Preview
@Composable
private fun StatSummaryCardsRowDarkPreview() {
    MerrPatentenTheme(darkTheme = true) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            StatSummaryCard(
                title = "Provime",
                value = "5",
                icon = Icons.Rounded.Quiz,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.weight(1f),
            )
            StatSummaryCard(
                title = "Mesatarja",
                value = "6.2",
                icon = Icons.Rounded.CheckCircle,
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.weight(1f),
            )
            StatSummaryCard(
                title = "Suksesi",
                value = "40%",
                icon = Icons.Rounded.EmojiEvents,
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier.weight(1f),
            )
        }
    }
}
