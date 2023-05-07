package com.arsildo.merr_patenten.exam

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NavigateBefore
import androidx.compose.material.icons.rounded.NavigateNext
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun PagerNavigation(
    modifier: Modifier = Modifier,
    onPreviousPageClick: () -> Unit,
    onNextPageClick: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {

        PagerNavigationButton(
            onClick = onPreviousPageClick,
            modifier = Modifier.weight(1f),
            icon = Icons.Rounded.NavigateBefore
        )
        PagerNavigationButton(
            onClick = onNextPageClick,
            modifier = Modifier.weight(1f),
            icon = Icons.Rounded.NavigateNext
        )

    }

}

@Composable
private fun PagerNavigationButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    onClick: () -> Unit,
) {

    ElevatedButton(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(),
        contentPadding = PaddingValues(horizontal = 32.dp, vertical = (16 + 4).dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )
    }

}