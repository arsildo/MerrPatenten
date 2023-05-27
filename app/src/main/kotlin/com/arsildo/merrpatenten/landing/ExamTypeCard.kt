package com.arsildo.merrpatenten.landing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.arsildo.merrpatenten.R

@Composable
fun ExamTypeCard(
    onClick: () -> Unit,
) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        shape = MaterialTheme.shapes.extraLarge,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "A1 A2 B1 B",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight(500)
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Loader(
                        modifier = Modifier
                            .size((128 + 32).dp)
                            .padding(4.dp)
                    )
                    Column {
                        Text(text = "40 Minuta", style = MaterialTheme.typography.titleMedium)
                        Text(text = "40 Pyetje", style = MaterialTheme.typography.titleMedium)
                        Text(text = "4 Gabime", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy((4 + 2).dp)
            ) {
                Button(
                    onClick = onClick,
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.start_exam))
                }
                Text(
                    text = stringResource(id = R.string.questionnaire_version),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 8.dp)
                )
            }
        }
    }
}

@Composable
fun Loader(
    modifier: Modifier = Modifier
) {
    val image by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.car_checkmark))
    val progress by animateLottieCompositionAsState(
        composition = image,
        restartOnPlay = true,
    )
    LottieAnimation(
        composition = image,
        progress = { progress },
        contentScale = ContentScale.Fit,
        modifier = modifier,
    )
}