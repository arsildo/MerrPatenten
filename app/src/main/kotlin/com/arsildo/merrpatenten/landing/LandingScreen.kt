package com.arsildo.merrpatenten.landing

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoGraph
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.arsildo.merrpatenten.R
import com.arsildo.merrpatenten.Destinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(
    navController: NavController,
    viewModel: LandingViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Merr Patenten") },
                actions = {
                    IconButton(
                        onClick = { navController.navigate(Destinations.PREFERENCES_ROUTE) },
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Menu,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                ),
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = "Statistics") },
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.AutoGraph,
                        contentDescription = null
                    )
                },
                onClick = { navController.navigate(Destinations.STATISTICS_ROUTE) },
                contentColor = MaterialTheme.colorScheme.onTertiary,
                containerColor = MaterialTheme.colorScheme.tertiary,
                elevation = FloatingActionButtonDefaults.loweredElevation()
            )
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column {
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = MaterialTheme.shapes.extraLarge,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "A1 A2 B1 B",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Text(text = "40 Pyetje")
                                Text(text = "40 Minuta")
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Text(text = "4 Gabime")
                                Text(text = "Versioni 2023")
                            }
                        }
                        Button(
                            onClick = { navController.navigate(Destinations.EXAM_ROUTE) },
                            contentPadding = PaddingValues(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = stringResource(id = R.string.startExam))
                        }
                    }
                }
            }
        }
    }

    val context = LocalContext.current
    val onBackPressedTwiceEnabled by viewModel.confirmAppExit.collectAsStateWithLifecycle(
        initialValue = true
    )
    var timeWhenPressed by remember { mutableStateOf(0L) }
    BackHandler {
        if (onBackPressedTwiceEnabled) onBackPressedTwice(
            timeWhenPressed = timeWhenPressed,
            context = context,
            updateTimeWhenPressed = { timeWhenPressed = System.currentTimeMillis() }
        ) else endApplication(context = context)
    }
}


@Composable
fun Loader() {
    val image by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.vehicles))
    val progress by animateLottieCompositionAsState(
        composition = image,
        restartOnPlay = true
    )
    LottieAnimation(
        composition = image,
        progress = { progress },
        contentScale = ContentScale.FillHeight,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .fillMaxHeight(.3f)
    )
}

private fun onBackPressedTwice(
    timeWhenPressed: Long,
    context: Context,
    updateTimeWhenPressed: () -> Unit
) {
    val activityContext = context as Activity
    if (timeWhenPressed + 2000 > System.currentTimeMillis()) activityContext.finish()
    else Toast.makeText(context, "Swipe back once more to leave the app.", Toast.LENGTH_LONG).show()
    updateTimeWhenPressed()
}

private fun endApplication(context: Context) {
    val activityContext = context as Activity
    activityContext.finish()
}