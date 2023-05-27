package com.arsildo.merrpatenten.statistics

import android.annotation.SuppressLint
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
import androidx.compose.material3.Divider
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
import com.arsildo.merrpatenten.theme.Green
import com.arsildo.merrpatenten.theme.Red
import java.math.RoundingMode
import java.text.DecimalFormat

data class Results(
    val errors: Int,
    val time: String,
)

@Suppress("MagicNumber")
@Composable
fun PerformanceGraph(
    results: List<Results>
) {
    if (results.isNotEmpty())
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.height(320.dp),
            ) {
                val primaryColor = MaterialTheme.colorScheme.primary
                Column(
                    modifier = Modifier
                        .height(300.dp)
                        .padding(end = 4.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    GraphTextDivider(text = "40")
                    GraphTextDivider(text = "30")
                    GraphTextDivider(text = "20")
                    GraphTextDivider(text = "10")
                    GraphTextDivider(text = "0")
                }
                Divider(
                    modifier = Modifier
                        .width(2.dp)
                        .fillMaxHeight((.9f + .044f)),
                    color = primaryColor
                )
                Column {
                    Canvas(
                        modifier = Modifier
                            .size(300.dp)
                            .clipToBounds()
                            .background(Red.copy(.2f))
                    ) {

                        drawRect(
                            color = Green.copy(.2f),
                            size = Size(size.width, -size.height / 10),
                            topLeft = Offset(0f, size.height)
                        )


                        val xCoordinates = mutableListOf<Int>()
                        val yCoordinates = mutableListOf<Int>()

                        for (i in results.indices) {
                            val minutes = "${results[i].time[0]}" + "${results[i].time[1]}"
                            yCoordinates.add(i, results[i].errors)
                            xCoordinates.add(i, minutes.toInt())
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
                                color = if (results[i].errors > 4) Red else Green,
                                radius = (10f+4f)
                            )
                        }

                    }
                    Divider(
                        modifier = Modifier
                            .height(2.dp)
                            .width(300.dp),
                        color = primaryColor
                    )
                    Row(
                        modifier = Modifier.width(300.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        GraphTextDivider(text = "0m")
                        GraphTextDivider(text = "10m")
                        GraphTextDivider(text = "20m")
                        GraphTextDivider(text = "30m")
                        GraphTextDivider(text = "40m")
                    }
                }


            }
        }
}

@Composable
private fun GraphTextDivider(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun AverageMistakes(previousExamResults: List<Results>) {
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
                        color = if (average > 4) Red else Green
                    )
                ) { append("$average") }
                append(" gabime ne ${previousExamResults.size} provimet e fundit")
            },
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

private fun calculateAverage(data: List<Results>): Double {
    var sum = 0.0
    data.forEach { sum += it.errors }
    val formatDouble = DecimalFormat("#.##")
    formatDouble.roundingMode = RoundingMode.CEILING
    return formatDouble.format(sum / data.size).toDouble()
}