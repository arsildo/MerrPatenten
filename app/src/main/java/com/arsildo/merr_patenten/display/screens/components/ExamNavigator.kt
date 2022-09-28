package com.arsildo.merr_patenten.display.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Looks3
import androidx.compose.material.icons.rounded.Looks4
import androidx.compose.material.icons.rounded.LooksOne
import androidx.compose.material.icons.rounded.LooksTwo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ExamNavigator(
    currentPage: Int,
    countDownTimer: String,
    onToggleClicked: () -> Unit,
    onMapClicked: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${currentPage + 1}/40",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.weight(.3f)
        )
        TextButton(
            onClick = onMapClicked,
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colors.primary,
                backgroundColor = MaterialTheme.colors.background,
            ),
            modifier = Modifier.weight(.3f)
        ) {
            Icon(Icons.Rounded.LooksOne, contentDescription = null)
            Icon(Icons.Rounded.LooksTwo, contentDescription = null)
            Icon(Icons.Rounded.Looks3, contentDescription = null)
            Icon(Icons.Rounded.Looks4, contentDescription = null)
        }
        Text(
            text = countDownTimer,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.weight(.3f)
        )
        IconButton(
            onClick = onToggleClicked,
            modifier = Modifier.weight(.1f)
        ) {
            Icon(
                Icons.Rounded.Close,
                contentDescription = null,
                tint = MaterialTheme.colors.onBackground,
            )
        }
    }
}