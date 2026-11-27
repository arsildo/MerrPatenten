package com.arsildo.merrpatenten.shared.feature.mistakes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonShapes
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.icons.Trophy
import com.arsildo.merrpatenten.shared.core.designsystem.semanticColors
import com.arsildo.merrpatenten.shared.feature.mistakes.MistakesPracticeViewModel
import merrpatenten.shared_core.design_system.generated.resources.Res
import merrpatenten.shared_core.design_system.generated.resources.mistakes_practice_complete_desc
import merrpatenten.shared_core.design_system.generated.resources.mistakes_practice_complete_title
import merrpatenten.shared_core.design_system.generated.resources.results_confirm
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun MistakesPracticeResultBottomSheetRoute(
    modifier: Modifier = Modifier,
    category: String,
    viewModel: MistakesPracticeViewModel = koinViewModel(parameters = { parametersOf(category) }),
    onConfirmClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MistakesPracticeResultBottomSheet(
        modifier = modifier,
        totalAnsweredCount = uiState.totalAnsweredCount,
        masteredQuestionsCount = uiState.masteredQuestionsCount,
        onConfirmClick = onConfirmClick,
        onDismiss = onDismiss,
    )
}

@Composable
internal fun MistakesPracticeResultBottomSheet(
    modifier: Modifier = Modifier,
    totalAnsweredCount: Int,
    masteredQuestionsCount: Int,
    onConfirmClick: () -> Unit,
    onDismiss: () -> Unit,
) {
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
        Icon(
            imageVector = Trophy,
            contentDescription = null,
            tint = MaterialTheme.semanticColors.onSuccessContainer,
            modifier = Modifier
                .size(64.dp)
                .background(MaterialTheme.semanticColors.successContainer, CircleShape)
                .wrapContentSize(Alignment.Center)
                .size(32.dp),
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(Res.string.mistakes_practice_complete_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = stringResource(
                    Res.string.mistakes_practice_complete_desc,
                    totalAnsweredCount,
                    masteredQuestionsCount,
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }

        Button(
            onClick = {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onConfirmClick()
            },
            shapes = ButtonShapes(
                shape = MaterialTheme.shapes.medium,
                pressedShape = MaterialTheme.shapes.small,
            ),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
        ) {
            Text(
                text = stringResource(Res.string.results_confirm),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
            )
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
private fun MistakesPracticeResultBottomSheetPreview() = MerrPatentenTheme {
    Surface {
        MistakesPracticeResultBottomSheet(
            totalAnsweredCount = 12,
            masteredQuestionsCount = 8,
            onConfirmClick = {},
            onDismiss = {},
        )
    }
}

@Preview
@Composable
private fun MistakesPracticeResultBottomSheetDarkPreview() = MerrPatentenTheme(darkTheme = true) {
    Surface {
        MistakesPracticeResultBottomSheet(
            totalAnsweredCount = 12,
            masteredQuestionsCount = 8,
            onConfirmClick = {},
            onDismiss = {},
        )
    }
}
