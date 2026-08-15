package com.arsildo.merrpatenten.shared.feature.statistics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.ERRORS_ALLOWED
import com.arsildo.merrpatenten.shared.core.model.ExamResult
import kotlin.math.round

@Composable
fun PerformanceGraph(
    results: List<ExamResult>
) {
    val graphSize = 360.dp
    if (results.isNotEmpty()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.height(graphSize),
            ) {
                val correctColor = MaterialTheme.colorScheme.onPrimaryContainer
                val errorColor = MaterialTheme.colorScheme.error
                Column(
                    modifier = Modifier
                        .height(graphSize - 20.dp)
                        .padding(end = 4.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    GraphTextDivider(text = "40")
                    GraphTextDivider(text = "30")
                    GraphTextDivider(text = "20")
                    GraphTextDivider(text = "10")
                    GraphTextDivider(text = "0")
                }
                HorizontalDivider(
                    modifier = Modifier
                        .width(2.dp)
                        .fillMaxHeight(0.95f),
                    color = MaterialTheme.colorScheme.primary
                )
                Column {
                    Canvas(
                        modifier = Modifier
                            .size(graphSize - 20.dp)
                            .clipToBounds()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        drawRect(
                            color = correctColor.copy(0.25f),
                            size = Size(size.width, -size.height / 10),
                            topLeft = Offset(0f, size.height)
                        )

                        val xCoordinates = mutableListOf<Int>()
                        val yCoordinates = mutableListOf<Int>()

                        for (i in results.indices) {
                            val timeStr = results[i].time
                            val minutes = if (timeStr.length >= 2) {
                                "${timeStr[0]}${timeStr[1]}".toIntOrNull() ?: 0
                            } else 0
                            yCoordinates.add(results[i].errors)
                            xCoordinates.add(minutes)
                        }

                        val points = mutableListOf<Offset>()
                        for (i in 0 until xCoordinates.size) {
                            points.add(Offset(xCoordinates[i].toFloat(), yCoordinates[i].toFloat()))
                        }

                        for (i in points.indices) {
                            val xAxis = size.width / 40 * xCoordinates[i]
                            val yAxis = size.height - (size.height / 40 * yCoordinates[i])
                            drawCircle(
                                center = Offset(x = xAxis, y = yAxis),
                                color = if (results[i].errors > ERRORS_ALLOWED) errorColor else correctColor,
                                radius = 16f
                            )
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier
                            .height(2.dp)
                            .width(graphSize - 20.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Row(
                        modifier = Modifier.width(graphSize - 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        GraphTextDivider(text = "0'")
                        GraphTextDivider(text = "10'")
                        GraphTextDivider(text = "20'")
                        GraphTextDivider(text = "30'")
                        GraphTextDivider(text = "40'")
                    }
                }
            }
        }
    }
}

@Composable
private fun GraphTextDivider(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun AverageMistakes(previousExamResults: List<ExamResult>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        val average = calculateAverage(previousExamResults)
        Text(
            buildAnnotatedString {
                append("Mesatarisht ")
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        color = if (average > ERRORS_ALLOWED)
                            MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                ) { append("$average") }
                append(" gabime në ${previousExamResults.size} provimet e fundit.")
            },
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleSmall
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
