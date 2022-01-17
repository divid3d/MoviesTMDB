package com.example.moviesapp.ui.screens.tv.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.layout.lerp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.rememberImagePainter
import com.example.moviesapp.model.TvSeries
import com.example.moviesapp.other.formatted
import com.example.moviesapp.other.getMaxSize
import com.example.moviesapp.ui.components.PosterPlaceholder
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
@Composable
fun NowAiringSection(
    modifier: Modifier = Modifier,
    title: String,
    state: LazyPagingItems<TvSeries>,
    onTvSeriesClick: (Int) -> Unit = {}
) {
    val pagerState = rememberPagerState()

    val isRefreshing by derivedStateOf {
        state.loadState.refresh is LoadState.Loading
    }

    val selectedTvSeries by derivedStateOf {
        val snapshot = state.itemSnapshotList

        if (snapshot.isNotEmpty()) snapshot.getOrNull(pagerState.currentPage) else null
    }

    val backdropScale =
        remember(selectedTvSeries) { Animatable(1f) }

    LaunchedEffect(selectedTvSeries) {
        backdropScale.animateTo(
            1.3f,
            animationSpec = tween(durationMillis = 10000, easing = LinearEasing)
        )
    }

    Box(modifier = modifier) {
        Crossfade(
            modifier = Modifier
                .matchParentSize()
                .blur(16.dp),
            targetState = selectedTvSeries
        ) { tvSeries ->
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .scale(backdropScale.value),
                painter = rememberImagePainter(
                    data = tvSeries?.backdropUrl,
                    builder = {
//                        fadeIn(
//                            initialAlpha = 0.3f,
//                            animationSpec = spring()
//                        )
//                        fadeOut()
                    }
                ),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(128.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, MaterialTheme.colors.background),
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        )
        {
            Text(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
                text = title,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            HorizontalPager(
                modifier = Modifier
                    .fillMaxWidth(),
                count = state.itemCount,
                state = pagerState
            ) { page ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.medium)
                ) {
                    val infoVisible = if (isRefreshing) false else pagerState.currentPage == page
                    if (isRefreshing) {
                        PosterPlaceholder(
                            modifier = Modifier
                                .width(160.dp)
                                .height(250.dp)
                        )
                    } else {
                        state[page]?.let { tvSeries ->
                            BoxWithConstraints(
                                modifier = Modifier
                                    .width(160.dp)
                                    .height(250.dp)
                            ) {
                                val (_, maxHeight) = getMaxSize()

                                Card(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .graphicsLayer {
                                            // Calculate the absolute offset for the current page from the
                                            // scroll position. We use the absolute value which allows us to mirror
                                            // any effects for both directions
                                            val pageOffset =
                                                calculateCurrentOffsetForPage(page).absoluteValue

                                            // We animate the scaleX + scaleY, between 85% and 100%
                                            lerp(
                                                start = ScaleFactor(0.7f, 0.7f),
                                                stop = ScaleFactor(1f, 1f),
                                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                            ).also { scale ->
                                                scaleX = scale.scaleX
                                                scaleY = scale.scaleY

                                                translationY = maxHeight * (1f - scale.scaleY) / 2
                                            }

                                            alpha = lerp(
                                                start = ScaleFactor(0.5f, 0.5f),
                                                stop = ScaleFactor(1f, 1f),
                                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                            ).scaleX
                                        }
                                        .clickable { onTvSeriesClick(tvSeries.id) },
                                    shape = MaterialTheme.shapes.medium,
                                    backgroundColor = Color.LightGray
                                ) {
                                    Image(
                                        painter = rememberImagePainter(
                                            data = tvSeries.posterUrl,
                                            builder = {
                                                fadeIn(
                                                    initialAlpha = 0.3f,
                                                    animationSpec = spring()
                                                )
                                            }
                                        ),
                                        contentDescription = null,
                                        contentScale = ContentScale.FillBounds
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                            AnimatedVisibility(
                                modifier = Modifier.weight(1f),
                                visible = infoVisible,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                Column(modifier = modifier.fillMaxWidth()) {
                                    Text(
                                        text = tvSeries.name,
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text(
                                        text = tvSeries.firstAirDate.formatted(),
                                        style = TextStyle(
                                            fontSize = 12.sp,
                                            color = Color.White,
                                            fontWeight = FontWeight.Light
                                        )
                                    )
                                    Text(
                                        text = tvSeries.overview,
                                        style = TextStyle(
                                            fontSize = 12.sp,
                                            color = Color.White
                                        ),
                                        maxLines = 5,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}