package com.arsildo.merr_patenten.display.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AutoDelete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.arsildo.merr_patenten.display.screens.components.ScreenLayout
import com.arsildo.merr_patenten.display.theme.Red
import com.arsildo.merr_patenten.logic.cache.ExamResult
import com.arsildo.merr_patenten.logic.cache.UserPreferences
import com.arsildo.merr_patenten.logic.navigation.Destinations
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.PI
import kotlin.math.cos

@Composable
fun StatisticsScreen(navController: NavController) {

    val dataStore = UserPreferences(LocalContext.current)
    val ifCacheStatistics = dataStore.getExamStatsPreference.collectAsState(initial = true).value
    val previousExamResults = dataStore.getExamResults.collectAsState(initial = listOf()).value

    ScreenLayout {
        Row(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row() {
                IconButton(
                    onClick = { navController.navigate(Destinations.Main.route) },
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(32.dp)
                        .padding(2.dp)
                ) {
                    Icon(
                        Icons.Rounded.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Text(
                    text = "Statistika",
                    color = MaterialTheme.colors.primary,
                    fontSize = 28.sp
                )
            }
            val scope = rememberCoroutineScope()
            if (previousExamResults.isNotEmpty())
                IconButton(
                    onClick = { scope.launch { dataStore.saveExamResults(listOf()) } },
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(32.dp)
                        .padding(2.dp)
                ) {
                    Icon(
                        Icons.Rounded.AutoDelete,
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier.fillMaxSize()
                    )
                }

        }
        if (!ifCacheStatistics) {
            Text(
                text = "Ju keni zgjedhur te mos ruani te dhenat.",
                color = MaterialTheme.colors.onBackground
            )
        } else {
            if (previousExamResults.isEmpty()) Text(
                text = "Nuk ka te dhena.",
                color = MaterialTheme.colors.primary
            )
            else LazyColumn(userScrollEnabled = true) {
                items(previousExamResults.size) {
                    ExamResultItem(
                        errors = previousExamResults[it].errors,
                        time = previousExamResults[it].time
                    )
                }
            }
            if (previousExamResults.isNotEmpty()) {
                Text(text = "1") // todo move up
            }
            AverageMistakes(previousExamResults)
            ExamResultsGraph()
        }

    }
}

@Composable
private fun ExamResultItem(errors: Int, time: String) {
    Row {
        Text(text = "G $errors ", color = MaterialTheme.colors.primary)
        Text(text = "K $time", color = MaterialTheme.colors.primary)
    }
}


@Composable
private fun ExamResultsGraph() {
    Row {
        Column(
            modifier = Modifier
                .height(252.dp)
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
                .height(252.dp)
                .width(2.dp),
            color = MaterialTheme.colors.primary
        )
        val primaryColor = MaterialTheme.colors.primary
        Column(modifier = Modifier.fillMaxWidth()) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(MaterialTheme.colors.primary.copy(.1f))
            ) {
                val height = size.height
                val width = size.width
                val points = mutableListOf<Offset>()

                for (x in 0..size.width.toInt()) {
                    val y =
                        (cos(x * (2f * PI / width)) * (height / 3f) + (height / 2)).toFloat()

                    points.add(Offset(x.toFloat(), y))
                }
                drawPoints(
                    points = points,
                    strokeWidth = 6f,
                    pointMode = PointMode.Points,
                    color = primaryColor
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp),
                color = MaterialTheme.colors.primary
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GraphTextDivider(text = "00:00")
                GraphTextDivider(text = "10:00")
                GraphTextDivider(text = "20:00")
                GraphTextDivider(text = "30:00")
                GraphTextDivider(text = "40:00")
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
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}


private fun calculateAverage(data: List<ExamResult>): Double {
    var sum = 0.0
    data.forEach { sum += it.errors }
    val formatDouble = DecimalFormat("#.##")
    formatDouble.roundingMode = RoundingMode.CEILING
    return formatDouble.format(sum / data.size).toDouble()
}
