package com.arsildo.merrpatenten.shared.feature.imagedetails.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.getImageResource
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

internal data class ImageDetailsUiState(val imageId: Int = 0)

@Composable
internal fun ZoomableExamImageRoute(imageId: Int, onDismiss: () -> Unit) {
    val uiState = remember(imageId) { ImageDetailsUiState(imageId = imageId) }
    ZoomableExamImage(
        uiState = uiState,
        onDismiss = onDismiss,
    )
}

@Composable
internal fun ZoomableExamImage(uiState: ImageDetailsUiState, onDismiss: () -> Unit, modifier: Modifier = Modifier) {
    val hapticFeedback = LocalHapticFeedback.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val handleDismiss = {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        onDismiss()
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .focusRequester(focusRequester)
            .focusable()
            .onPreviewKeyEvent { event ->
                if (event.type != KeyEventType.KeyDown) return@onPreviewKeyEvent false
                when (event.key) {
                    Key.Escape, Key.Back, Key.Z -> {
                        handleDismiss()
                        true
                    }

                    else -> false
                }
            }
            .clickable(onClick = handleDismiss),
        contentAlignment = Alignment.Center,
    ) {
        var scale by remember { mutableFloatStateOf(1f) }
        var rotation by remember { mutableFloatStateOf(0f) }
        val state = rememberTransformableState { zoomChange, panChange, rotationChange ->
            scale = (scale * zoomChange).coerceIn(0.5f, 5f)
            rotation += rotationChange
        }
        Image(
            painter = painterResource(getImageResource(uiState.imageId)),
            contentDescription = null,
            modifier = Modifier
                .padding(24.dp)
                .aspectRatio(ratio = 1f)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    rotationZ = rotation,
                )
                .transformable(state = state),
        )
        ExtendedFloatingActionButton(
            onClick = handleDismiss,
            text = {
                Text(
                    text = stringResource(Res.string.back),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                )
            },
            icon = {
                Icon(imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null)
            },
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 3.dp),
            shape = MaterialTheme.shapes.large,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 24.dp),
        )
    }
}

@Preview
@Composable
private fun ZoomableExamImagePreview() {
    MerrPatentenTheme {
        ZoomableExamImage(
            uiState = ImageDetailsUiState(imageId = 1),
            onDismiss = {},
        )
    }
}

@Preview
@Composable
private fun ZoomableExamImageDarkPreview() {
    MerrPatentenTheme(darkTheme = true) {
        ZoomableExamImage(
            uiState = ImageDetailsUiState(imageId = 1),
            onDismiss = {},
        )
    }
}
