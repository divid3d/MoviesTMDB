package com.example.moviesapp.ui.components.sections

import androidx.compose.animation.*
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.layout.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.paging.compose.LazyPagingItems
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.transform.BlurTransformation
import com.example.moviesapp.model.DetailPresentable
import com.example.moviesapp.model.DetailPresentableItemState
import com.example.moviesapp.other.BottomRoundedArcShape
import com.example.moviesapp.other.ImageUrlParser
import com.example.moviesapp.other.getMaxSizeInt
import com.example.moviesapp.other.isDark
import com.example.moviesapp.ui.components.items.DetailPresentableItem
import com.example.moviesapp.ui.components.others.rememberTmdbImagePainter
import com.example.moviesapp.ui.theme.Size
import com.example.moviesapp.ui.theme.sizes
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class, ExperimentalCoilApi::class)
@Composable
fun PresentableTopSection(
    modifier: Modifier = Modifier,
    title: String,
    state: LazyPagingItems<out DetailPresentable>,
    scrollState: ScrollState? = null,
    scrollValueLimit: Float? = null,
    onPresentableClick: (Int) -> Unit = {}
) {
    val context = LocalContext.current
    val density = LocalDensity.current

    val pagerState = rememberPagerState()

    var isDark by rememberSaveable { mutableStateOf(true) }

    val contentColor by animateColorAsState(targetValue = if (isDark) Color.White else Color.Black)

    val selectedPresentable by derivedStateOf {
        val snapshot = state.itemSnapshotList

        if (snapshot.isNotEmpty()) snapshot.getOrNull(pagerState.currentPage) else null
    }

    val currentScrollValue = scrollState?.value

    val ratio = if (currentScrollValue != null && scrollValueLimit != null) {
        (currentScrollValue / scrollValueLimit).coerceIn(0f, 1f)
    } else 0f

    val itemHeight = density.run { MaterialTheme.sizes.presentableItemBig.height.toPx() }

    Box(modifier = modifier.clip(RectangleShape)) {
        BoxWithConstraints(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    clip = true
                    shape = BottomRoundedArcShape(
                        ratio = ratio
                    )
                }
        ) {
            val (maxWidth, maxHeight) = getMaxSizeInt()

            Crossfade(
                modifier = Modifier.fillMaxSize(),
                targetState = selectedPresentable
            ) { movie ->
                val backdropScale = remember { Animatable(1f) }

                val backgroundPainter = rememberTmdbImagePainter(
                    path = movie?.backdropPath,
                    type = ImageUrlParser.ImageType.Backdrop,
                    preferredSize = android.util.Size(maxWidth, maxHeight),
                    builder = {
                        transformations(
                            BlurTransformation(
                                context = context,
                                radius = 16f,
                                sampling = 6f
                            )
                        )
                    }
                )

                val backgroundPainterState = backgroundPainter.state

                LaunchedEffect(backgroundPainterState) {
                    if (backgroundPainterState is ImagePainter.State.Success) {
                        isDark = backgroundPainterState.result.drawable.toBitmap().run { isDark() }

                        backdropScale.animateTo(
                            targetValue = 1.6f,
                            animationSpec = tween(durationMillis = 10000, easing = LinearEasing)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .matchParentSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colors.background
                                )
                            )
                        )
                )

                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(backdropScale.value),
                    painter = backgroundPainter,
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colors.background
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.spacing.medium)
                    .padding(top = MaterialTheme.spacing.small),
                text = title,
                color = contentColor,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold
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
                    val presentableItemState = presentable?.let {
                        DetailPresentableItemState.Result(it)
                    } ?: DetailPresentableItemState.Loading

                    PresentableTopSectionItem(
                        modifier = Modifier.fillMaxWidth(),
                        presentableItemState = presentableItemState,
                        isSelected = selectedPresentable == presentable,
                        contentColor = contentColor,
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
    presentableItemState: DetailPresentableItemState,
    presentableSize: Size = MaterialTheme.sizes.presentableItemBig,
    contentColor: Color = Color.White,
    onPresentableClick: () -> Unit = {},
    isSelected: Boolean,
    itemTransformations: GraphicsLayerScope.() -> Unit = {},
    contentTransformations: GraphicsLayerScope.() -> Unit = {}
) {
    Row(
        modifier = modifier
    ) {
        DetailPresentableItem(
            presentableState = presentableItemState,
            size = presentableSize,
            showTitle = false,
            showScore = false,
            showAdult = true,
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
            if (presentableItemState is DetailPresentableItemState.Result) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .graphicsLayer { contentTransformations() },
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                ) {
                    Text(
                        text = presentableItemState.presentable.title,
                        fontSize = 16.sp,
                        color = contentColor,
                        fontWeight = FontWeight.ExtraBold
                    )
                    presentableItemState.presentable.overview?.let { overview ->
                        Text(
                            text = overview,
                            fontSize = 12.sp,
                            color = contentColor,
                            maxLines = 5,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
