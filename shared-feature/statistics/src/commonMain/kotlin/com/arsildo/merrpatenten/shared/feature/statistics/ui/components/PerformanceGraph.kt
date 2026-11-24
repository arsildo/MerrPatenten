package com.arsildo.merrpatenten.shared.feature.statistics.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.ERRORS_ALLOWED
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.semanticColors
import com.arsildo.merrpatenten.shared.core.model.ExamResult
import kotlin.math.round
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun PerformanceGraph(results: List<ExamResult>, modifier: Modifier = Modifier) {
    if (results.isEmpty()) return

    val successColor = MaterialTheme.semanticColors.success
    val errorColor = MaterialTheme.colorScheme.error
    val passingZoneColor = MaterialTheme.semanticColors.successContainer.copy(alpha = 0.35f)
    val points = remember(results) {
        results.map { result ->
            val timeStr = result.time
            val minutes = if (timeStr.length >= 2) {
                "${timeStr[0]}${timeStr[1]}".toIntOrNull() ?: 0
            } else {
                0
            }
            GraphPoint(
                xCoord = minutes.coerceIn(0, 40),
                yCoord = result.errors.coerceIn(0, 40),
                isPassed = result.errors <= ERRORS_ALLOWED,
            )
        }
    }

    Surface(
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            // Header with legend
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = stringResource(Res.string.performance_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
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
                horizontalArrangement = Arrangement.Center,
            ) {
                // Y-Axis Labels
                Column(
                    modifier = Modifier
                        .height(graphHeight)
                        .padding(end = 8.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.End,
                ) {
                    GraphAxisText(text = "40")
                    GraphAxisText(text = "30")
                    GraphAxisText(text = "20")
                    GraphAxisText(text = "10")
                    GraphAxisText(text = "0")
                }

                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(graphHeight)
                            .clip(MaterialTheme.shapes.large)
                            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                            .drawWithCache {
                                val passZoneHeight = size.height / 40f * ERRORS_ALLOWED.toFloat()
                                val passZoneTopLeft = Offset(0f, size.height - passZoneHeight)
                                val passZoneSize = Size(size.width, passZoneHeight)
                                val gridStrokeWidth = 1.dp.toPx()
                                val haloRadius = 10.dp.toPx()
                                val dotRadius = 6.dp.toPx()
                                val correctHaloColor = correctColor.copy(alpha = 0.25f)
                                val errorHaloColor = errorColor.copy(alpha = 0.25f)
                                val gridLines = List(3) { index ->
                                    val y = size.height - (size.height / 4f * (index + 1))
                                    Offset(0f, y) to Offset(size.width, y)
                                }
                                val dots = points.map { point ->
                                    val xPos = (size.width / 40f) * point.xCoord.toFloat()
                                    val yPos = size.height - ((size.height / 40f) * point.yCoord.toFloat())
                                    CachedDot(
                                        center = Offset(xPos, yPos),
                                        color = if (point.isPassed) correctColor else errorColor,
                                        haloColor = if (point.isPassed) correctHaloColor else errorHaloColor,
                                    )
                                }

                                onDrawBehind {
                                    // Passing Zone background highlight (Errors 0 to 4 is at the bottom 10% of height)
                                    drawRect(
                                        color = passingZoneColor,
                                        topLeft = passZoneTopLeft,
                                        size = passZoneSize,
                                    )

                                    // Grid lines
                                    gridLines.forEach { (start, end) ->
                                        drawLine(
                                            color = axisColor,
                                            start = start,
                                            end = end,
                                            strokeWidth = gridStrokeWidth,
                                        )
                                    }

                                    // Data points
                                    dots.forEach { dot ->
                                        // Draw subtle outer halo
                                        drawCircle(
                                            color = dot.haloColor,
                                            radius = haloRadius,
                                            center = dot.center,
                                        )
                                        // Draw main point
                                        drawCircle(
                                            color = dot.color,
                                            radius = dotRadius,
                                            center = dot.center,
                                        )
                                    }
                                }
                            },
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // X-Axis Labels
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
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
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            shape = CircleShape,
            color = color,
            modifier = Modifier.size(8.dp),
        ) {}
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun GraphAxisText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
internal fun AverageMistakes(previousExamResults: List<ExamResult>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        val average = remember(previousExamResults) {
            calculateAverage(previousExamResults)
        }
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
                        color = if (average > ERRORS_ALLOWED) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.primary
                        },
                    ),
                ) { append("$average") }
                append("$middle${previousExamResults.size}$suffix")
            },
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

private data class GraphPoint(val xCoord: Int, val yCoord: Int, val isPassed: Boolean)

private data class CachedDot(val center: Offset, val color: Color, val haloColor: Color)

private fun calculateAverage(data: List<ExamResult>): Double {
    if (data.isEmpty()) return 0.0
    var sum = 0.0
    data.forEach { sum += it.errors }
    val avg = sum / data.size
    return round(avg * 100) / 100.0
}

@Preview
@Composable
private fun PerformanceGraphPreview() {
    MerrPatentenTheme {
        PerformanceGraph(
            results = listOf(
                ExamResult(id = 1, errors = 2, time = "34:20"),
                ExamResult(id = 2, errors = 6, time = "28:10"),
                ExamResult(id = 3, errors = 0, time = "31:45"),
                ExamResult(id = 4, errors = 3, time = "20:15"),
                ExamResult(id = 5, errors = 8, time = "15:00"),
            ),
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview
@Composable
private fun PerformanceGraphDarkPreview() {
    MerrPatentenTheme(darkTheme = true) {
        PerformanceGraph(
            results = listOf(
                ExamResult(id = 1, errors = 1, time = "34:20"),
                ExamResult(id = 2, errors = 2, time = "28:10"),
                ExamResult(id = 3, errors = 0, time = "31:45"),
            ),
            modifier = Modifier.padding(16.dp),
        )
    }
}
