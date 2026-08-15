package com.arsildo.merrpatenten.shared.feature.imagedetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.getImageResource
import org.jetbrains.compose.resources.painterResource

data class ImageDetailsUiState(
    val imageId: Int = 0,
)

@Composable
fun ZoomableExamImageRoute(
    imageId: Int,
    onDismiss: () -> Unit,
) {
    val uiState = remember(imageId) { ImageDetailsUiState(imageId = imageId) }
    ZoomableExamImage(
        uiState = uiState,
        onDismiss = onDismiss,
    )
}

@Composable
fun ZoomableExamImage(
    uiState: ImageDetailsUiState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.Center
    ) {
        var scale by remember { mutableFloatStateOf(1f) }
        var rotation by remember { mutableFloatStateOf(0f) }
        val state = rememberTransformableState { zoomChange, _, rotationChange ->
            scale *= zoomChange
            rotation += rotationChange
        }
        Image(
            painter = painterResource(getImageResource(uiState.imageId)),
            contentDescription = null,
            modifier = Modifier
                .padding(16.dp)
                .aspectRatio(ratio = 1f)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    rotationZ = rotation
                )
                .transformable(state = state)
        )
        ExtendedFloatingActionButton(
            onClick = onDismiss,
            text = { Text(text = "Kthehu") },
            icon = {
                Icon(imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null)
            },
            elevation = FloatingActionButtonDefaults.loweredElevation(),
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}

@Preview
@Composable
private fun ZoomableExamImagePreview() {
    MerrPatentenTheme {
        ZoomableExamImage(
            uiState = ImageDetailsUiState(imageId = 1),
            onDismiss = {}
        )
    }
}

