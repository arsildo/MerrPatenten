package com.arsildo.merr_patenten.display.screens

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.arsildo.merr_patenten.display.screens.components.BackNavigatorBar
import com.arsildo.merr_patenten.display.screens.components.ScreenLayout
import com.arsildo.merr_patenten.display.theme.Green
import com.arsildo.merr_patenten.display.theme.Red
import com.arsildo.merr_patenten.logic.cache.ExamResult
import com.arsildo.merr_patenten.logic.cache.UserPreferences
import com.arsildo.merr_patenten.logic.navigation.Destinations
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat


sealed class StatisticState {
    object Disabled : StatisticState()
    object Empty : StatisticState()
    object Statistics : StatisticState()
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatisticsScreen(navController: NavController) {

    val dataStore = UserPreferences(LocalContext.current)
    val ifCacheStatistics = dataStore.getExamStatsPreference.collectAsState(initial = true).value
    val previousExamResults = dataStore.getExamResults.collectAsState(initial = listOf()).value
    val viewState =
        if (ifCacheStatistics)
            if (previousExamResults.isEmpty()) StatisticState.Empty else StatisticState.Statistics
        else StatisticState.Disabled

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    ScreenLayout {

        BackNavigatorBar(
            title = "Statistika",
            onNavigateBackDestination = { navController.navigate(Destinations.Main.route) },
            trailingIcon = {
                if (previousExamResults.isNotEmpty())
                    Icon(
                        Icons.Outlined.DeleteSweep,
                        contentDescription = null,
                        tint = Red,
                        modifier = Modifier
                            .size(32.dp)
                            .combinedClickable(
                                onClick = {
                                    val message =
                                        "Mbani shtypur per te fshire rezultatet e meparshme."
                                    Toast
                                        .makeText(context, message, Toast.LENGTH_LONG)
                                        .show()
                                },
                                onLongClick = { scope.launch { dataStore.saveExamResults(listOf()) } },
                            )
                    )
            }
        )

        when (viewState) {
            is StatisticState.Statistics -> {
                ExamResultsGraph(results = previousExamResults)
                AverageMistakes(previousExamResults)
                PreviousResultsList(previousExamResults = previousExamResults)

            }

            is StatisticState.Disabled -> {
                Text(
                    text = "Ju keni zgjedhur te mos ruani te dhenat.",
                    color = MaterialTheme.colors.primary,
                )

            }

            is StatisticState.Empty -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Nuk ka te dhena.",
                        color = MaterialTheme.colors.primary,
                    )
                }

            }
        }
    }


}


@Composable
fun PreviousResultsList(previousExamResults: List<ExamResult>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "xG", color = MaterialTheme.colors.primary)
        Text(text = "Koha", color = MaterialTheme.colors.primary)
    }
    Divider(color = MaterialTheme.colors.primary)
    LazyColumn {
        items(previousExamResults.size) {
            ExamResultItem(
                errors = previousExamResults[it].errors,
                time = previousExamResults[it].time
            )
        }
    }
}

@Composable
private fun ExamResultItem(errors: Int, time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clip(MaterialTheme.shapes.small)
            .background(color = if (errors > 4) Red else Green)
            .padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "$errors", color = MaterialTheme.colors.onPrimary)
        Text(text = time, color = MaterialTheme.colors.onPrimary)
    }
}


@Composable
private fun ExamResultsGraph(results: List<ExamResult>) {
    if (results.isNotEmpty())
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.height(320.dp),
            ) {
                val primaryColor = MaterialTheme.colors.primary
                Column(
                    modifier = Modifier.height(300.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    GraphTextDivider(text = "40 ")
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
                            .background(MaterialTheme.colors.primary.copy(.2f))
                    ) {

                        drawRect(
                            color = Green.copy(.3f),
                            size = Size(size.width, -size.height / 10),
                            topLeft = Offset(0f, size.height)
                        )


                        val xCoordinates = mutableListOf<Int>()
                        val yCoordinates = mutableListOf<Int>()

                        for (i in results.indices) {
                            val minutes =
                                "${results[i].time[0]}" + "${results[i].time[1]}"
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
                                radius = 16f
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

@Composable
private fun GraphTextDivider(text: String) {
    Text(text = text, fontSize = 13.sp, color = MaterialTheme.colors.primary)
}

@Composable
private fun AverageMistakes(previousExamResults: List<ExamResult>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            buildAnnotatedString {
                append("Mesatarisht ")
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Medium,
                        color = Red
                    )
                ) { append("${calculateAverage(previousExamResults)}") }
                append(" gabime ne ${previousExamResults.size} provimet e fundit.")
            },
            color = MaterialTheme.colors.primary,
        )
    }
}


private fun calculateAverage(data: List<ExamResult>): Double {
    var sum = 0.0
    data.forEach { sum += it.errors }
    val formatDouble = DecimalFormat("#.##")
    formatDouble.roundingMode = RoundingMode.CEILING
    return formatDouble.format(sum / data.size).toDouble()
}
