package com.arsildo.merrpatenten.shared.feature.bookmarks.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonShapes
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.icons.Trash
import com.arsildo.merrpatenten.shared.feature.bookmarks.BookmarksViewModel
import merrpatenten.shared_core.design_system.generated.resources.Res
import merrpatenten.shared_core.design_system.generated.resources.bookmarks_clear_category
import merrpatenten.shared_core.design_system.generated.resources.bookmarks_clear_category_desc
import merrpatenten.shared_core.design_system.generated.resources.results_confirm
import merrpatenten.shared_core.design_system.generated.resources.results_dismiss
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun ClearBookmarksBottomSheetRoute(
    modifier: Modifier = Modifier,
    viewModel: BookmarksViewModel = koinViewModel(),
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    ClearBookmarksBottomSheet(
        modifier = modifier,
        onConfirm = {
            viewModel.clearCategoryBookmarks()
            onConfirm()
        },
        onDismiss = onDismiss,
    )
}

@Composable
internal fun ClearBookmarksBottomSheet(modifier: Modifier = Modifier, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    val hapticFeedback = LocalHapticFeedback.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .onPreviewKeyEvent { event ->
                if (event.type != KeyEventType.KeyDown) return@onPreviewKeyEvent false
                when (event.key) {
                    Key.Escape, Key.Back -> {
                        onDismiss()
                        true
                    }

                    else -> false
                }
            }
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.errorContainer,
            modifier = Modifier.size(64.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Trash,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.size(32.dp),
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(Res.string.bookmarks_clear_category),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = stringResource(Res.string.bookmarks_clear_category_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    onConfirm()
                },
                shapes = ButtonShapes(
                    shape = MaterialTheme.shapes.medium,
                    pressedShape = MaterialTheme.shapes.small,
                ),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                ),
            ) {
                Text(
                    text = stringResource(Res.string.results_confirm),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            FilledTonalButton(
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onDismiss()
                },
                shapes = ButtonShapes(
                    shape = MaterialTheme.shapes.medium,
                    pressedShape = MaterialTheme.shapes.small,
                ),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
            ) {
                Text(
                    text = stringResource(Res.string.results_dismiss),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }

        Spacer(
            modifier = Modifier
                .size(16.dp)
                .navigationBarsPadding(),
        )
    }
}

@Preview
@Composable
private fun ClearBookmarksBottomSheetPreview() = MerrPatentenTheme {
    Surface {
        ClearBookmarksBottomSheet(
            onConfirm = {},
            onDismiss = {},
        )
    }
}

@Preview
@Composable
private fun ClearBookmarksBottomSheetDarkPreview() = MerrPatentenTheme(darkTheme = true) {
    Surface {
        ClearBookmarksBottomSheet(
            onConfirm = {},
            onDismiss = {},
        )
    }
}
