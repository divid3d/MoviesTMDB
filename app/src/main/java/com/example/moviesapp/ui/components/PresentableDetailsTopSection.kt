package com.example.moviesapp.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.BlurTransformation
import com.example.moviesapp.model.Backdrop
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.model.PresentableItemState
import com.example.moviesapp.ui.theme.sizes
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun PresentableDetailsTopSection(
    modifier: Modifier = Modifier,
    presentable: Presentable?,
    backdrops: List<Backdrop> = emptyList(),
    content: @Composable ColumnScope.() -> Unit = {}
) {
    val context = LocalContext.current

    val presentableItemState by derivedStateOf {
        presentable?.let { PresentableItemState.Result(it) } ?: PresentableItemState.Loading
    }

    val availableBackdropUrls: List<String> by derivedStateOf {
        buildList {
            add(presentable?.backdropUrl)
            addAll(backdrops.map { backdrop -> backdrop.fileUrl })
        }.filterNotNull()
    }

    var currentBackdropUrlIndex by remember(availableBackdropUrls) {
        mutableStateOf(0)
    }

    val currentBackdropUrl by derivedStateOf {
        availableBackdropUrls.getOrNull(currentBackdropUrlIndex)
    }

    val backdropScale = remember(currentBackdropUrl) { Animatable(1f) }

    LaunchedEffect(currentBackdropUrl) {
        val result = backdropScale.animateTo(
            targetValue = 1.6f,
            animationSpec = tween(durationMillis = 10000, easing = LinearEasing)
        )

        when (result.endReason) {
            AnimationEndReason.Finished -> {
                val backdropCount = availableBackdropUrls.count()
                val nextIndex = currentBackdropUrlIndex + 1

                currentBackdropUrlIndex = if (nextIndex >= backdropCount) 0 else nextIndex
            }
            else -> Unit
        }
    }

    Box(modifier = modifier.clip(RectangleShape)) {
        Crossfade(
            modifier = Modifier.matchParentSize(),
            targetState = currentBackdropUrl
        ) { url ->
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .scale(backdropScale.value),
                painter = rememberImagePainter(
                    data = url,
                    builder = {
                        fadeIn(animationSpec = spring())
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
                .padding(top = 56.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.medium),
                verticalAlignment = Alignment.Top
            ) {
                PresentableItem(
                    size = MaterialTheme.sizes.presentableItemBig,
                    showScore = true,
                    showTitle = false,
                    presentableState = presentableItemState
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                Column(modifier = Modifier.weight(1f)) {
                    content()
                }
            }
        }
    }
}