package com.arsildo.merrpatenten.shared.core.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import coil3.compose.AsyncImage
import com.arsildo.merrpatenten.shared.core.designsystem.getImageResource
import com.arsildo.merrpatenten.shared.core.designsystem.getImageUri
import org.jetbrains.compose.resources.painterResource

/**
 * Decodes raster question/sign resources asynchronously off the main thread via Coil [AsyncImage]
 * (downsampled and memory-cached), avoiding main-thread `painterResource` jank during scrolling
 * and paging. Falls back to [painterResource] for vector fallback resources (`img_info`) and
 * Compose previews.
 */
@Composable
fun ResourceImage(
    imageId: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {
    val imageUri = remember(imageId) { getImageUri(imageId) }
    if (imageUri == null || LocalInspectionMode.current) {
        Image(
            painter = painterResource(getImageResource(imageId)),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier,
        )
    } else {
        AsyncImage(
            model = imageUri,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier,
        )
    }
}
