package com.arsildo.merr_patenten.display.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Map
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier

@Composable
fun ExamNavigator(
    countDownTimer: MutableState<String>,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(text = "1/40", modifier = Modifier.weight(.3f))
        IconButton(
            onClick = onClick,
            modifier = Modifier.weight(.3f)
        ) {
            Icon(
                Icons.Rounded.Map, // TODO FIX
                contentDescription = null
            )
        }
        Text(
            text = countDownTimer.value,
            modifier = Modifier.weight(.3f)
        )
        IconButton(
            onClick = onClick,
            modifier = Modifier.weight(.1f)
        ) {
            Icon(
                Icons.Rounded.Close, // TODO Implement Functionality and Animation
                contentDescription = null,
            )
        }
    }
}