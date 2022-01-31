package com.example.moviesapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import coil.size.Scale
import com.example.moviesapp.model.Image
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun StillBrowser(
    modifier: Modifier = Modifier,
    stillUrls: List<Image>
) {
    val pagerState = rememberPagerState()

    Column(modifier = modifier) {
        HorizontalPager(
            modifier = Modifier.fillMaxWidth(),
            count = stillUrls.count(),
            state = pagerState
        ) { page ->
            val stillImage = stillUrls.getOrNull(page)

            Image(
                painter = rememberImagePainter(
                    data = stillImage?.fileUrl,
                    builder = {
                        size(OriginalSize)
                        scale(Scale.FIT)
                        crossfade(true)
                    }
                ),
                contentScale = ContentScale.FillWidth,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        if (stillUrls.count() > 1) {
            HorizontalPagerIndicator(
                pagerState = pagerState,
                activeColor = MaterialTheme.colors.primary,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp),
            )
        }
    }

}