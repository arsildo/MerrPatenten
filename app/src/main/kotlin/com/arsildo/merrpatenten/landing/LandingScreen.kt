package com.arsildo.merrpatenten.landing

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Looks3
import androidx.compose.material.icons.filled.LooksOne
import androidx.compose.material.icons.filled.LooksTwo
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.material.icons.outlined.BusAlert
import androidx.compose.material.icons.outlined.CarRental
import androidx.compose.material.icons.outlined.ExposurePlus2
import androidx.compose.material.icons.outlined.FireTruck
import androidx.compose.material.icons.outlined.Looks3
import androidx.compose.material.icons.outlined.LooksOne
import androidx.compose.material.icons.outlined.LooksTwo
import androidx.compose.material.icons.outlined.PlusOne
import androidx.compose.material.icons.rounded.AutoGraph
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arsildo.merrpatenten.R
import com.arsildo.merrpatenten.theme.Red
import com.arsildo.merrpatenten.utils.DPSHTRR_HELP

@Composable
fun LandingScreen(
    viewModel: LandingViewModel = hiltViewModel(),
    onStartExamClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    onPreferencesClick: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = stringResource(id = R.string.statistics)) },
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.AutoGraph,
                        contentDescription = null
                    )
                },
                onClick = onStatisticsClick,
                contentColor = Color.White,
                containerColor = Red,
                elevation = FloatingActionButtonDefaults.loweredElevation(),
                shape = MaterialTheme.shapes.extraLarge
            )
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ExamTypeCard(
                    title = R.string.questionnaire_category_one,
                    description = "40 MINUTA | 40 PYETJE | 4 GABIME",
                    icon = Icons.Filled.LooksOne,
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    onClick = onStartExamClick
                )

                val toastMessage =
                    Toast.makeText(LocalContext.current, "Vjen se shpejti...", Toast.LENGTH_LONG)

                ExamTypeCard(
                    title = R.string.questionnaire_category_two,
                    description = "40 MINUTA | 40 PYETJE | 4 GABIME",
                    icon = Icons.Filled.LooksTwo,
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    ),
                    onClick = { toastMessage.show() }
                )
                ExamTypeCard(
                    title = R.string.questionnaire_category_three,
                    description = "10 MINUTA | 10 PYETJE | 1 GABIM",
                    icon = Icons.Filled.Looks3,
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    onClick = { toastMessage.show() }
                )
                val context = LocalContext.current
                HelpfulMaterialCard(
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    onClick = {
                        val dpshtrrLink = Intent(Intent.ACTION_VIEW)
                        dpshtrrLink.data = Uri.parse(DPSHTRR_HELP)
                        context.startActivity(dpshtrrLink)
                    }
                )
            }
            IconButton(
                onClick = onPreferencesClick,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = null
                )
            }
        }
    }

    val context = LocalContext.current
    val onBackPressedTwiceEnabled by viewModel.confirmAppExit.collectAsStateWithLifecycle(
        initialValue = true
    )
    var timeWhenPressed by remember { mutableLongStateOf(0L) }
    BackHandler {
        if (onBackPressedTwiceEnabled) onBackPressedTwice(
            timeWhenPressed = timeWhenPressed,
            context = context,
            updateTimeWhenPressed = { timeWhenPressed = System.currentTimeMillis() }
        ) else endApplication(context = context)
    }
}

private fun onBackPressedTwice(
    timeWhenPressed: Long,
    context: Context,
    updateTimeWhenPressed: () -> Unit
) {
    val activityContext = context as Activity
    if (timeWhenPressed + 2_000 > System.currentTimeMillis()) activityContext.finish()
    else Toast.makeText(context, "Swipe back once more to leave the app.", Toast.LENGTH_LONG).show()
    updateTimeWhenPressed()
}

private fun endApplication(context: Context) {
    val activityContext = context as Activity
    activityContext.finish()
}