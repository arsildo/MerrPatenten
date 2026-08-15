package com.arsildo.merrpatenten.shared.feature.statistics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.ERRORS_ALLOWED
import com.arsildo.merrpatenten.shared.core.designsystem.semanticColors
import com.arsildo.merrpatenten.shared.core.model.ExamResult
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import kotlin.math.round

@Composable
fun PerformanceGraph(
    results: List<ExamResult>,
    modifier: Modifier = Modifier,
) {
    if (results.isEmpty()) return

    val successColor = MaterialTheme.semanticColors.success
    val errorColor = MaterialTheme.colorScheme.error
    val passingZoneColor = MaterialTheme.semanticColors.successContainer.copy(alpha = 0.35f)

    Surface(
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header with legend
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = stringResource(Res.string.performance_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LegendItem(color = successColor, label = stringResource(Res.string.performance_passed))
                    LegendItem(color = errorColor, label = stringResource(Res.string.performance_failed))
                }
            }

            val graphHeight = 240.dp
            val axisColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            val correctColor = successColor

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                // Y-Axis Labels
                Column(
                    modifier = Modifier
                        .height(graphHeight)
                        .padding(end = 8.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.End
                ) {
                    GraphAxisText(text = "40")
                    GraphAxisText(text = "30")
                    GraphAxisText(text = "20")
                    GraphAxisText(text = "10")
                    GraphAxisText(text = "0")
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(graphHeight)
                            .clip(MaterialTheme.shapes.large)
                            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    ) {
                        // Passing Zone background highlight (Errors 0 to 4 is at the bottom 10% of height)
                        val passZoneHeight = size.height / 40f * ERRORS_ALLOWED.toFloat()
                        drawRect(
                            color = passingZoneColor,
                            topLeft = Offset(0f, size.height - passZoneHeight),
                            size = Size(size.width, passZoneHeight)
                        )

                        // Grid lines
                        for (i in 1..3) {
                            val y = size.height - (size.height / 4f * i)
                            drawLine(
                                color = axisColor,
                                start = Offset(0f, y),
                                end = Offset(size.width, y),
                                strokeWidth = 1.dp.toPx()
                            )
                        }

                        // Data points
                        for (i in results.indices) {
                            val timeStr = results[i].time
                            val minutes = if (timeStr.length >= 2) {
                                "${timeStr[0]}${timeStr[1]}".toIntOrNull() ?: 0
                            } else 0

                            val xCoord = minutes.coerceIn(0, 40)
                            val yCoord = results[i].errors.coerceIn(0, 40)

                            val xPos = (size.width / 40f) * xCoord.toFloat()
                            val yPos = size.height - ((size.height / 40f) * yCoord.toFloat())

                            val isPassed = results[i].errors <= ERRORS_ALLOWED
                            val dotColor = if (isPassed) correctColor else errorColor

                            // Draw subtle outer halo
                            drawCircle(
                                color = dotColor.copy(alpha = 0.25f),
                                radius = 10.dp.toPx(),
                                center = Offset(xPos, yPos)
                            )
                            // Draw main point
                            drawCircle(
                                color = dotColor,
                                radius = 6.dp.toPx(),
                                center = Offset(xPos, yPos)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // X-Axis Labels
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        GraphAxisText(text = "0'")
                        GraphAxisText(text = "10'")
                        GraphAxisText(text = "20'")
                        GraphAxisText(text = "30'")
                        GraphAxisText(text = "40'")
                    }
                }
            }
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            color = color,
            modifier = Modifier.size(8.dp)
        ) {}
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun GraphAxisText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
fun AverageMistakes(previousExamResults: List<ExamResult>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        val average = calculateAverage(previousExamResults)
        val prefix = stringResource(Res.string.performance_average_prefix)
        val middle = stringResource(Res.string.performance_average_middle)
        val suffix = stringResource(Res.string.performance_average_suffix)
        Text(
            buildAnnotatedString {
                append(prefix)
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        color = if (average > ERRORS_ALLOWED)
                            MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                ) { append("$average") }
                append("$middle${previousExamResults.size}$suffix")
            },
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

private fun calculateAverage(data: List<ExamResult>): Double {
    if (data.isEmpty()) return 0.0
    var sum = 0.0
    data.forEach { sum += it.errors }
    val avg = sum / data.size
    return round(avg * 100) / 100.0
}
