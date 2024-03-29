package com.arsildo.merrpatenten.preferences

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesScreen(
    navController: NavController,
    viewModel: PreferencesViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AnimateColorSchemeTransition {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Preferences") },
                    navigationIcon = {
                        IconButton(onClick = navController::popBackStack) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                )
            },
            contentColor = MaterialTheme.colorScheme.primary
        ) { contentPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .padding(horizontal = 16.dp)
            ) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    modifier = Modifier.padding(vertical = 32.dp)
                ) {

                    PreferenceCard(
                        title = "Remember your exam results",
                        subtitle = "Save exam results and use that information to generate statistics based on your recent performances. (10 last exams)",
                        checked = uiState.saveStats,
                        onCheckedChange = viewModel::setSaveStats
                    )

                    PreferenceCard(
                        title = "Confirm app exit",
                        subtitle = "Swipe back twice to leave the app.",
                        checked = uiState.confirmAppExit,
                        onCheckedChange = viewModel::setConfirmAppExit
                    )

                    PreferenceCard(
                        title = "Follow System",
                        subtitle = "Get your color scheme for your system settings.",
                        checked = uiState.followSystemColors,
                        onCheckedChange = viewModel::setFollowSystem
                    )

                    PreferenceCard(
                        title = "Dark Mode",
                        subtitle = "Color scheme used by this application",
                        checked = uiState.colorScheme,
                        enabled = !uiState.followSystemColors,
                        onCheckedChange = viewModel::setColorScheme
                    )

                    PreferenceCard(
                        title = "Material You",
                        subtitle = "Dynamic colors generated from your wallpaper. Only supported in Android 12 and later.",
                        checked = uiState.dynamicColorScheme,
                        onCheckedChange = viewModel::setDynamicColorScheme
                    )

                }

            }
        }
    }
}

@Composable
fun PreferenceCard(
    title: String,
    subtitle: String = "",
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(.8f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelLarge,
            )
        }
        Switch(
            checked = checked,
            enabled = enabled,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable // This composable animates between theme transitions
fun AnimateColorSchemeTransition(content: @Composable () -> Unit) {
    val animation: AnimationSpec<Color> = spring(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessLow
    )
    val colors = MaterialTheme.colorScheme.copy(
        background = animateColorAsState(
            targetValue = MaterialTheme.colorScheme.background,
            animationSpec = animation,
            label = "Background Color Transition"
        ).value,
        primary = animateColorAsState(
            targetValue = MaterialTheme.colorScheme.primary,
            animationSpec = animation,
            label = "Primary Color Transition"
        ).value,
        secondary = animateColorAsState(
            targetValue = MaterialTheme.colorScheme.primary,
            animationSpec = animation,
            label = "Secondary Color Transition"
        ).value,
    )
    MaterialTheme(colorScheme = colors, content = content)
}