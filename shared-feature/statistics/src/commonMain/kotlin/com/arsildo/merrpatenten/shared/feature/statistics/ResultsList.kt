package com.arsildo.merrpatenten.shared.feature.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.HighlightOff
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.ERRORS_ALLOWED
import com.arsildo.merrpatenten.shared.core.designsystem.semanticColors
import com.arsildo.merrpatenten.shared.core.model.ExamResult
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.stringResource

internal fun LazyGridScope.resultList(
    results: List<ExamResult>,
) {
    itemsIndexed(
        items = results,
        key = { _, item -> item.id },
        span = { _, _ -> GridItemSpan(maxLineSpan) }
    ) { index, result ->
        ResultItem(
            result = result,
            index = index,
            count = results.size
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun ResultItem(
    result: ExamResult,
    index: Int,
    count: Int,
    modifier: Modifier = Modifier,
) {
    val errors = result.errors
    val isPassed = errors <= ERRORS_ALLOWED
    val errorLabel = stringResource(if (errors == 1) Res.string.error_singular else Res.string.errors_plural)
    val statusText = stringResource(if (isPassed) Res.string.performance_passed else Res.string.performance_failed)
    val icon = if (isPassed) Icons.Rounded.CheckCircle else Icons.Rounded.HighlightOff
    val statusContainerColor = if (isPassed) MaterialTheme.semanticColors.successContainer else MaterialTheme.colorScheme.errorContainer
    val statusContentColor = if (isPassed) MaterialTheme.semanticColors.onSuccessContainer else MaterialTheme.colorScheme.onErrorContainer
    val statusIconTint = if (isPassed) MaterialTheme.semanticColors.success else MaterialTheme.colorScheme.error

    SegmentedListItem(
        selected = false,
        onClick = {},
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            selectedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        shapes = ListItemDefaults.segmentedShapes(index = index, count = count),
        content = {
            Text(
                text = "$errors $errorLabel",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        },
        supportingContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Schedule,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${result.time} min",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        trailingContent = {
            Surface(
                shape = MaterialTheme.shapes.small,
                color = statusContainerColor
            ) {
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = statusContentColor,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}
