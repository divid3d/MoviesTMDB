package com.example.moviesapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.layout.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import coil.compose.rememberImagePainter
import coil.transform.BlurTransformation
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.ui.theme.Size
import com.example.moviesapp.ui.theme.sizes
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PresentableTopSection(
    modifier: Modifier = Modifier,
    title: String,
    state: LazyPagingItems<Presentable>,
    onPresentableClick: (Int) -> Unit = {}
) {
    val context = LocalContext.current

    val pagerState = rememberPagerState()
    val density = LocalDensity.current

    val selectedPresentable by derivedStateOf {
        val snapshot = state.itemSnapshotList

        if (snapshot.isNotEmpty()) snapshot.getOrNull(pagerState.currentPage) else null
    }

    val backdropScale = remember(selectedPresentable) { Animatable(1f) }

    val itemHeight = density.run { MaterialTheme.sizes.presentableItemBig.height.toPx() }

//    LaunchedEffect(selectedPresentable) {
//        backdropScale.animateTo(
//            targetValue = 1.3f,
//            animationSpec = tween(durationMillis = 10000, easing = LinearEasing)
//        )
//    }

    Box(modifier = modifier) {
        Crossfade(
            modifier = Modifier
                .matchParentSize()
                .scale(backdropScale.value),
            targetState = selectedPresentable
        ) { movie ->
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = rememberImagePainter(
                    data = movie?.backdropUrl,
                    builder = {
                        transformations(
                            BlurTransformation(
                                context = context,
                                radius = 16f,
                                sampling = 2f
                            )
                        )
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
                    .fillMaxWidth()
                    .defaultMinSize(
                        minHeight = MaterialTheme.sizes.presentableItemBig.height + MaterialTheme.spacing.medium + MaterialTheme.spacing.medium
                    ),
                count = state.itemCount,
                state = pagerState
            ) { page ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.medium)
                ) {
                    val presentable = state[page]
                    val presentableState = presentable?.let { PresentableState.Result(it) }
                        ?: PresentableState.Loading

                    PresentableTopSectionItem(
                        modifier = Modifier.fillMaxWidth(),
                        presentableState = presentableState,
                        isSelected = selectedPresentable == presentable,
                        onPresentableClick = {
                            presentable?.let {
                                onPresentableClick(it.id)
                            }
                        },
                        itemTransformations = {
                            val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                            lerp(
                                start = ScaleFactor(0.7f, 0.7f),
                                stop = ScaleFactor(1f, 1f),
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            ).also { scale ->
                                scaleX = scale.scaleX
                                scaleY = scale.scaleY

                                translationY = itemHeight * (1f - scale.scaleY) / 2
                            }

                            alpha = lerp(
                                start = ScaleFactor(0.3f, 0.3f),
                                stop = ScaleFactor(1f, 1f),
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            ).scaleX
                        },
                        contentTransformations = {
                            val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                            alpha = lerp(
                                start = ScaleFactor(0.1f, 0.1f),
                                stop = ScaleFactor(1f, 1f),
                                fraction = 1f - 2 * pageOffset.coerceIn(0f, 1f)
                            ).scaleX
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun PresentableTopSectionItem(
    modifier: Modifier = Modifier,
    presentableState: PresentableState,
    presentableSize: Size = MaterialTheme.sizes.presentableItemBig,
    onPresentableClick: () -> Unit = {},
    isSelected: Boolean,
    itemTransformations: GraphicsLayerScope.() -> Unit = {},
    contentTransformations: GraphicsLayerScope.() -> Unit = {}
) {

    Row(
        modifier = modifier
    ) {
        PresentableItem(
            presentableState = presentableState,
            size = presentableSize,
            showTitle = false,
            showScore = false,
            onClick = onPresentableClick,
            transformations = itemTransformations
        )
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
        AnimatedVisibility(
            modifier = Modifier.weight(1f),
            enter = fadeIn(),
            exit = fadeOut(),
            visible = isSelected
        ) {
            if (presentableState is PresentableState.Result) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .graphicsLayer { contentTransformations() }
                ) {
                    Text(
                        text = presentableState.presentable.title,
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = presentableState.presentable.overview,
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
