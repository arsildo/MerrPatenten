package com.arsildo.merrpatenten.disclaimer

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.R
import com.arsildo.merrpatenten.utils.DPSHTRR_HELP

@Composable
fun DisclaimerMessage() {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current
        val desc = stringResource(R.string.disclaimer_description, DPSHTRR_HELP).dropLast(n = 5)
        val tag = stringResource(R.string.disclaimer_description).takeLast(n = 5)
        val annotatedString = buildAnnotatedString {
            withStyle(
                style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)
            ) { append(desc) }
            pushStringAnnotation(tag = tag, annotation = DPSHTRR_HELP)
            withStyle(
                style = SpanStyle(color = MaterialTheme.colorScheme.primary)
            ) { append(tag) }
            pop()

        }
        Text(
            text = stringResource(R.string.disclaimer_title),
            style = MaterialTheme.typography.headlineSmall,
        )
        ClickableText(
            text = annotatedString,
            style = MaterialTheme.typography.bodyMedium,
            onClick = { offset ->
                annotatedString.getStringAnnotations(
                    tag = tag,
                    start = offset,
                    end = offset
                ).firstOrNull()?.let {
                    val dpshtrrLink = Intent(Intent.ACTION_VIEW)
                    dpshtrrLink.data = Uri.parse(DPSHTRR_HELP)
                    context.startActivity(dpshtrrLink)
                }
            })
    }
}