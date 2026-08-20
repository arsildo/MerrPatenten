package com.arsildo.merrpatenten.shared.feature.exam.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ZoomIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.getImageResource
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun QuestionImage(image: DrawableResource, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val hapticFeedback = LocalHapticFeedback.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onClick()
            },
        contentAlignment = Alignment.BottomEnd,
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.5f),
        )

        // Zoom Hint Pill
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
            contentColor = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(12.dp),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Outlined.ZoomIn,
                    contentDescription = stringResource(Res.string.zoom),
                    modifier = Modifier.size(16.dp),
                )
                Text(
                    text = stringResource(Res.string.zoom),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

@Preview
@Composable
private fun QuestionImagePreview() {
    MerrPatentenTheme {
        QuestionImage(
            image = getImageResource(1),
            onClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview
@Composable
private fun QuestionImageDarkPreview() {
    MerrPatentenTheme(darkTheme = true) {
        QuestionImage(
            image = getImageResource(1),
            onClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
