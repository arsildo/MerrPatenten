package com.arsildo.merr_patenten.display.screens.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NavigateBefore
import androidx.compose.material.icons.rounded.NavigateNext
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun PagerNavigation(
    onPreviousClicked: () -> Unit,
    onNextClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        PagerNavigationButton(
            modifier = Modifier.weight(.45f),
            icon = Icons.Rounded.NavigateBefore,
            onClick = onPreviousClicked
        )
        Divider(Modifier.width(16.dp), color = MaterialTheme.colors.background)
        PagerNavigationButton(
            modifier = Modifier.weight(.45f),
            icon = Icons.Rounded.NavigateNext,
            onClick = onNextClicked
        )
    }
}

@Composable
fun PagerNavigationButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.background,
            contentColor = MaterialTheme.colors.primary
        ),
        border = BorderStroke(1.dp, MaterialTheme.colors.primary),
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .fillMaxWidth()
            .height((64 + 16).dp)
    ) {
        Icon(icon, contentDescription = null, modifier.fillMaxHeight(.8f))
    }
}