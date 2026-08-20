package com.arsildo.merrpatenten.shared.core.designsystem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme

/**
 * Material 3 Expressive section header used for clear category grouping and separation.
 */
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    style: TextStyle = MaterialTheme.typography.titleLarge,
) {
    Text(
        text = title,
        style = style,
        fontWeight = FontWeight.Bold,
        color = color,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp)
    )
}

@Preview
@Composable
private fun SectionHeaderPreview() {
    MerrPatentenTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            SectionHeader(title = "Përzgjidh kategorinë")
        }
    }
}

@Preview
@Composable
private fun SectionHeaderDarkPreview() {
    MerrPatentenTheme(darkTheme = true) {
        Column(modifier = Modifier.padding(16.dp)) {
            SectionHeader(title = "Sjellja e aplikacionit")
        }
    }
}

