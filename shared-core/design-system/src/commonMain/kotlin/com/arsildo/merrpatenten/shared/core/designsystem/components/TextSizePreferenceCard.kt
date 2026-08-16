package com.arsildo.merrpatenten.shared.core.designsystem.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FormatSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.QuestionTextSize
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TextSizePreferenceCard(
    modifier: Modifier = Modifier,
    selectedSize: QuestionTextSize,
    onSizeSelected: (QuestionTextSize) -> Unit,
    index: Int = 0,
    count: Int = 1,
) {
    val hapticFeedback = LocalHapticFeedback.current
    Surface(
        shape = ListItemDefaults.segmentedShapes(index = index, count = count).shape,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.FormatSize,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(Res.string.preferences_question_text_size),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = stringResource(Res.string.preferences_question_text_size_desc),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                QuestionTextSize.entries.forEachIndexed { idx, size ->
                    SegmentedButton(
                        selected = selectedSize == size,
                        onClick = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentTick)
                            onSizeSelected(size)
                        },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = idx,
                            count = QuestionTextSize.entries.size
                        ),
                        icon = {
                            SegmentedButtonDefaults.Icon(active = selectedSize == size)
                        },
                        label = {
                            Text(
                                text = stringResource(size.labelRes)
                            )
                        }
                    )
                }
            }
        }
    }
}
