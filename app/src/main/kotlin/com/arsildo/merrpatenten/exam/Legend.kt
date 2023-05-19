package com.arsildo.merrpatenten.exam

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Fullscreen
import androidx.compose.material.icons.rounded.Looks3
import androidx.compose.material.icons.rounded.Looks4
import androidx.compose.material.icons.rounded.Looks5
import androidx.compose.material.icons.rounded.LooksTwo
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.theme.Red

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Legend(
    pagerState: PagerState,
    timer: String,
    endExamVisible: Boolean,
    onMapClick: () -> Unit,
    onShowEndExamButton: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .safeContentPadding(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${pagerState.settledPage + 1}/40",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.requiredWidth(width = 64.dp)
        )

        TextButton(
            onClick = onMapClick,
        ) {
            Icon(
                imageVector = Icons.Rounded.LooksTwo,
                contentDescription = null
            )
            Icon(
                imageVector = Icons.Rounded.Looks3,
                contentDescription = null
            )
            Icon(
                imageVector = Icons.Rounded.Looks4,
                contentDescription = null
            )
            Icon(
                imageVector = Icons.Rounded.Looks5,
                contentDescription = null
            )
        }

        Text(
            text = timer,
            style = MaterialTheme.typography.titleMedium
        )


        val rotationState by animateFloatAsState(
            targetValue = if (endExamVisible) 180f else 0f,
            label = "",
            animationSpec = tween(durationMillis = 512)
        )

        val color by animateColorAsState(
            targetValue = if (rotationState == 0f) Red else MaterialTheme.colorScheme.primary,
            label = ""
        )

        IconButton(
            onClick = onShowEndExamButton,
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = color
            )
        ) {
            Icon(
                imageVector = if (endExamVisible) Icons.Rounded.Fullscreen else Icons.Rounded.Close,
                contentDescription = null,
                modifier = Modifier.rotate(rotationState)
            )
        }

    }
}