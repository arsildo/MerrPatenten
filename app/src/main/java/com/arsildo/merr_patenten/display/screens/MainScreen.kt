package com.arsildo.merr_patenten.display.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.material.icons.rounded.Gite
import androidx.compose.material.icons.rounded.QueryStats
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.arsildo.merr_patenten.R
import com.arsildo.merr_patenten.display.screens.components.ScreenLayout
import com.arsildo.merr_patenten.logic.navigation.Destinations

@Composable
fun MainScreen(
    navController: NavController,
) {
    ScreenLayout(
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        CategoryCard(
            onStartExamClicked = {
                navController.navigate(route = Destinations.Exam.route)
            }
        )
        BottomActions(
            onGithubLinkClicked = { /*TODO*/ },
            onStatisticsClicked = { /*TODO*/ },
            onPreferencesClicked = { navController.navigate(Destinations.Preferences.route) }
        )
    }
}

@Composable
fun CategoryCard(onStartExamClicked: () -> Unit) {
    Card(
        contentColor = MaterialTheme.colors.onSurface,
        backgroundColor = MaterialTheme.colors.surface,
        shape = MaterialTheme.shapes.medium,
        elevation = 2.dp,
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = tween(
                        delayMillis = 64,
                        durationMillis = 128,
                        easing = LinearEasing
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "A1 A2 B1 B", fontSize = 32.sp, color = MaterialTheme.colors.primary)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Image(
                    painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = null,
                    modifier = Modifier.size(128.dp)
                )
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = "Versioni 2022")
                    Text(text = "40 Pyetje")
                    Text(text = "40 Minuta")
                    Text(text = "4 Gabime te lejuara")
                }
            }
            var additionalInfo by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onStartExamClicked,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colors.onPrimary,
                        backgroundColor = MaterialTheme.colors.primary,
                    ),
                    shape = MaterialTheme.shapes.medium,
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                    ),
                    modifier = Modifier.fillMaxWidth(.85f)
                ) {
                    Icon(
                        Icons.Rounded.Flag,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(text = "Fillo Provimin")
                }
                IconButton(
                    onClick = { additionalInfo = !additionalInfo },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val rotationState by animateFloatAsState(if (additionalInfo) 180f else 0f)
                    Icon(
                        Icons.Rounded.ExpandMore,
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier.rotate(rotationState)
                    )
                }
            }
            if (additionalInfo) {
                Text(text = "additional info")
            }
        }
    }
}

@Composable
fun BottomActions(
    onGithubLinkClicked: () -> Unit,
    onStatisticsClicked: () -> Unit,
    onPreferencesClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onGithubLinkClicked) {
            Icon(
                Icons.Rounded.Gite, // todo Change to Github Logo.
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(32.dp)
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onStatisticsClicked) {
                Icon(
                    Icons.Rounded.QueryStats,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(32.dp)
                )
            }
            IconButton(onClick = onPreferencesClicked) {
                Icon(
                    Icons.Rounded.Settings,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}