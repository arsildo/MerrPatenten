package com.arsildo.merrpatenten.preferences

import android.content.Intent
import android.net.Uri
import android.os.Build
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
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arsildo.merrpatenten.R
import com.arsildo.merrpatenten.utils.GITHUB_URL
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesScreen(
    viewModel: PreferencesViewModel = koinViewModel(),
    onBackPress: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AnimateColorSchemeTransition {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.preferences)) },
                    navigationIcon = {
                        IconButton(onClick = onBackPress) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        titleContentColor = MaterialTheme.colorScheme.onSecondary,
                        actionIconContentColor = MaterialTheme.colorScheme.onSecondary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSecondary,
                    ),
                )
            },
            floatingActionButton = {
                val context = LocalContext.current
                ExtendedFloatingActionButton(
                    onClick = {
                        val githubLink = Intent(Intent.ACTION_VIEW)
                        githubLink.data = Uri.parse(GITHUB_URL)
                        context.startActivity(githubLink)
                    },
                    text = { Text(text = "Github") },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.github),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                    },
                    elevation = FloatingActionButtonDefaults.loweredElevation(),
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    shape = MaterialTheme.shapes.extraLarge
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
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(top = 32.dp)
                ) {
                    PreferenceCard(
                        title = stringResource(id = R.string.preferences_navigation_buttons),
                        subtitle = stringResource(id = R.string.preferences_navigation_buttons_desc),
                        checked = uiState.immersiveMode,
                        onCheckedChange = viewModel::setImmersiveMode
                    )
                    PreferenceCard(
                        title = stringResource(id = R.string.preferences_store_stats),
                        subtitle = stringResource(id = R.string.preferences_store_stats_desc),
                        checked = uiState.saveStats,
                        onCheckedChange = viewModel::setSaveStats
                    )
                    PreferenceCard(
                        title = stringResource(id = R.string.preferences_double_press),
                        subtitle = stringResource(id = R.string.preferences_double_press_desc),
                        checked = uiState.confirmAppExit,
                        onCheckedChange = viewModel::setConfirmAppExit
                    )
                    PreferenceCard(
                        title = stringResource(id = R.string.preferences_follow_system_theme),
                        subtitle = stringResource(id = R.string.preferences_follow_system_theme_desc),
                        checked = uiState.followSystemColors,
                        onCheckedChange = viewModel::setFollowSystem
                    )
                    PreferenceCard(
                        title = stringResource(id = R.string.preferences_dark_mode),
                        subtitle = stringResource(id = R.string.preferences_dark_mode_desc),
                        checked = uiState.colorScheme,
                        enabled = !uiState.followSystemColors,
                        onCheckedChange = viewModel::setColorScheme
                    )
                    PreferenceCard(
                        title = stringResource(id = R.string.preferences_material_you),
                        subtitle = stringResource(id = R.string.preferences_material_you_desc),
                        checked = uiState.dynamicColorScheme,
                        enabled = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
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
                color = MaterialTheme.colorScheme.tertiary
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