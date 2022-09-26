package com.arsildo.merr_patenten.display.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arsildo.merr_patenten.logic.database.DatabaseModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Pager(
    pagerState: PagerState,
    list : List<DatabaseModel>
) {
    HorizontalPager(count = 40, state = pagerState) { page ->
        PagerItem(list[page])
    }
}

@Composable
fun PagerItem(model : DatabaseModel) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painterResource(id = model.image),
            contentDescription = null,
            modifier=Modifier.fillMaxWidth(.5f).height(256.dp).padding(bottom = 32.dp)
        )
        Text(
            text = model.question,
            textAlign = TextAlign.Center
        )
    }
}