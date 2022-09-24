package com.arsildo.merr_patenten.display.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.arsildo.merr_patenten.display.screens.components.ScreenLayout
import com.arsildo.merr_patenten.logic.navigation.Destinations

@Composable
fun MainScreen(
    navController: NavController,
) {
    ScreenLayout {
        CategoryCard(
            onStartExamClicked = {
                navController.navigate(route = Destinations.Exam.route)
            }
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
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Category", fontSize = 32.sp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Icon(
                    Icons.Rounded.DirectionsCar,
                    contentDescription = null,
                    modifier = Modifier.size(128.dp)
                )
                Column() {
                    Text(text = "1")
                    Text(text = "2")
                    Text(text = "3")
                    Text(text = "4")
                }
            }
            Button(
                onClick = onStartExamClicked,
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colors.onPrimary,
                    backgroundColor = MaterialTheme.colors.primary,
                ),
                shape = MaterialTheme.shapes.medium,
                elevation = ButtonDefaults.elevation(0.dp)
            ) {
                Text(text = "Start")
            }
        }
    }
}