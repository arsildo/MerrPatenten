package com.arsildo.merr_patenten.display.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Looks
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
            text = "${currentPage+1}/40",
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(.3f)
        )
        Button(
            onClick = onMapClicked,
            modifier = Modifier.weight(.3f)
        ) {
            Text(text = "Click")
        }
        Text(
            text = countDownTimer,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(.3f)
        )
        IconButton(
            onClick = onToggleClicked,
            modifier = Modifier.weight(.1f)
        ) {
            Icon(
                Icons.Rounded.Close, // TODO Implement Functionality and Animation
                contentDescription = null,
            )
        }
    }
}