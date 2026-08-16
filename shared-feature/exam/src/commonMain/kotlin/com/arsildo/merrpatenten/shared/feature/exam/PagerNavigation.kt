package com.arsildo.merrpatenten.shared.feature.exam

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme

import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun PagerNavigation(
    onPreviousPageClick: () -> Unit,
    onNextPageClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val hapticFeedback = LocalHapticFeedback.current
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilledTonalButton(
            onClick = {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentTick)
                onPreviousPageClick()
            },
            shapes = ButtonShapes(
                shape = MaterialTheme.shapes.large,
                pressedShape = MaterialTheme.shapes.small
            ),
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = stringResource(Res.string.previous_page)
                )
            }
        }

        ElevatedButton(
            onClick = {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentTick)
                onNextPageClick()
            },
            shapes = ButtonShapes(
                shape = MaterialTheme.shapes.large,
                pressedShape = MaterialTheme.shapes.small
            ),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = stringResource(Res.string.next_page)
                )
            }
        }
    }
}

@Preview
@Composable
private fun PagerNavigationPreview() {
    MerrPatentenTheme {
        PagerNavigation(
            onPreviousPageClick = {},
            onNextPageClick = {}
        )
    }
}
