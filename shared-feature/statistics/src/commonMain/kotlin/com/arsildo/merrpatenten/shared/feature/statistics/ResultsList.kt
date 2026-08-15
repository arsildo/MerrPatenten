package com.arsildo.merrpatenten.shared.feature.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.HighlightOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.ERRORS_ALLOWED
import com.arsildo.merrpatenten.shared.core.designsystem.Green
import com.arsildo.merrpatenten.shared.core.designsystem.GreenContainer
import com.arsildo.merrpatenten.shared.core.designsystem.OnGreenContainer
import com.arsildo.merrpatenten.shared.core.designsystem.OnRedContainer
import com.arsildo.merrpatenten.shared.core.designsystem.Red
import com.arsildo.merrpatenten.shared.core.designsystem.RedContainer
import com.arsildo.merrpatenten.shared.core.model.ExamResult

import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ResultList(
    results: List<ExamResult>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val pairs = results.chunked(2)
        pairs.forEach { pair ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ResultItem(
                    result = pair[0],
                    modifier = Modifier.weight(1f)
                )
                if (pair.size > 1) {
                    ResultItem(
                        result = pair[1],
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Surface(
                        modifier = Modifier.weight(1f),
                        color = androidx.compose.ui.graphics.Color.Transparent
                    ) {}
                }
            }
        }
    }
}

@Composable
internal fun ResultItem(
    result: ExamResult,
    modifier: Modifier = Modifier,
) {
    val errors = result.errors
    val isPassed = errors <= ERRORS_ALLOWED
    val containerColor = if (isPassed) GreenContainer else RedContainer
    val contentColor = if (isPassed) OnGreenContainer else OnRedContainer
    val icon = if (isPassed) Icons.Rounded.CheckCircle else Icons.Rounded.HighlightOff
    val iconTint = if (isPassed) Green else Red

    Surface(
        shape = MaterialTheme.shapes.large,
        color = containerColor,
        contentColor = contentColor,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                val errorLabel = stringResource(if (errors == 1) Res.string.error_singular else Res.string.errors_plural)
                Text(
                    text = "$errors $errorLabel",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${result.time} min",
                    style = MaterialTheme.typography.bodySmall,
                    color = contentColor.copy(alpha = 0.8f)
                )
            }
            Surface(
                shape = CircleShape,
                color = iconTint.copy(alpha = 0.15f),
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier
                        .padding(5.dp)
                        .size(22.dp)
                )
            }
        }
    }
}
