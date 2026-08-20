package com.arsildo.merrpatenten.shared.feature.exam.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonShapes
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.semanticColors

@Composable
internal fun ExpressiveOptionCard(
    title: String,
    iconSelected: ImageVector,
    iconUnselected: ImageVector,
    isChecked: Boolean,
    enabled: Boolean,
    selectedContainerColor: Color,
    selectedContentColor: Color,
    selectedBorderColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val containerColor by animateColorAsState(
        targetValue = if (isChecked) {
            selectedContainerColor
        } else {
            MaterialTheme.colorScheme.surfaceContainerHigh
        },
        label = "OptionContainerColor",
    )

    val contentColor by animateColorAsState(
        targetValue = if (isChecked) {
            selectedContentColor
        } else {
            MaterialTheme.colorScheme.onSurface
        },
        label = "OptionContentColor",
    )

    val borderColor = if (isChecked) {
        selectedBorderColor
    } else {
        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    }

    Button(
        onClick = {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            onClick()
        },
        enabled = enabled,
        shapes = ButtonShapes(
            shape = MaterialTheme.shapes.large,
            pressedShape = MaterialTheme.shapes.small,
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor,
            disabledContentColor = contentColor,
        ),
        border = BorderStroke(if (isChecked) 2.dp else 1.dp, borderColor),
        contentPadding = PaddingValues(16.dp),
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            AnimatedContent(
                targetState = isChecked,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "OptionIcon",
            ) { checked ->
                Icon(
                    imageVector = if (checked) iconSelected else iconUnselected,
                    contentDescription = null,
                    tint = if (checked) selectedBorderColor else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(22.dp),
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (isChecked) FontWeight.Bold else FontWeight.SemiBold,
                color = contentColor,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}

@Preview
@Composable
private fun ExpressiveOptionCardCheckedPreview() {
    MerrPatentenTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            ExpressiveOptionCard(
                title = "Saktë",
                iconSelected = Icons.Rounded.CheckCircle,
                iconUnselected = Icons.Outlined.CheckCircle,
                isChecked = true,
                enabled = true,
                selectedContainerColor = MaterialTheme.semanticColors.successContainer,
                selectedContentColor = MaterialTheme.semanticColors.onSuccessContainer,
                selectedBorderColor = MaterialTheme.semanticColors.success,
                onClick = {},
                modifier = Modifier.weight(1f),
            )
            ExpressiveOptionCard(
                title = "Gabuar",
                iconSelected = Icons.Rounded.Cancel,
                iconUnselected = Icons.Outlined.Cancel,
                isChecked = false,
                enabled = true,
                selectedContainerColor = MaterialTheme.colorScheme.errorContainer,
                selectedContentColor = MaterialTheme.colorScheme.onErrorContainer,
                selectedBorderColor = MaterialTheme.colorScheme.error,
                onClick = {},
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Preview
@Composable
private fun ExpressiveOptionCardDarkPreview() {
    MerrPatentenTheme(darkTheme = true) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            ExpressiveOptionCard(
                title = "Saktë",
                iconSelected = Icons.Rounded.CheckCircle,
                iconUnselected = Icons.Outlined.CheckCircle,
                isChecked = false,
                enabled = true,
                selectedContainerColor = MaterialTheme.semanticColors.successContainer,
                selectedContentColor = MaterialTheme.semanticColors.onSuccessContainer,
                selectedBorderColor = MaterialTheme.semanticColors.success,
                onClick = {},
                modifier = Modifier.weight(1f),
            )
            ExpressiveOptionCard(
                title = "Gabuar",
                iconSelected = Icons.Rounded.Cancel,
                iconUnselected = Icons.Outlined.Cancel,
                isChecked = true,
                enabled = true,
                selectedContainerColor = MaterialTheme.colorScheme.errorContainer,
                selectedContentColor = MaterialTheme.colorScheme.onErrorContainer,
                selectedBorderColor = MaterialTheme.colorScheme.error,
                onClick = {},
                modifier = Modifier.weight(1f),
            )
        }
    }
}
