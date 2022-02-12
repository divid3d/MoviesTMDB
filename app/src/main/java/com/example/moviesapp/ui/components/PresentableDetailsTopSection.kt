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
import coil.transform.BlurTransformation
import com.example.moviesapp.model.Image
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.model.PresentableItemState
import com.example.moviesapp.other.ImageUrlParser
import com.example.moviesapp.other.getMaxSizeInt
import com.example.moviesapp.ui.theme.sizes
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun PresentableDetailsTopSection(
    modifier: Modifier = Modifier,
    presentable: Presentable?,
    backdrops: List<Image> = emptyList(),
    content: @Composable ColumnScope.() -> Unit = {}
) {
    val context = LocalContext.current

    val presentableItemState by derivedStateOf {
        presentable?.let { PresentableItemState.Result(it) } ?: PresentableItemState.Loading
    }

    val availableBackdropPaths: List<String> by derivedStateOf {
        buildList {
            add(presentable?.backdropPath)
            addAll(backdrops.map { backdrop -> backdrop.filePath })
        }.filterNotNull()
    }

    var currentBackdropPathIndex by remember(availableBackdropPaths) {
        mutableStateOf(0)
    }

    val currentBackdropPath by derivedStateOf {
        availableBackdropPaths.getOrNull(currentBackdropPathIndex)
    }

    val backdropScale = remember(currentBackdropPath) { Animatable(1f) }

    LaunchedEffect(currentBackdropPath) {
        val result = backdropScale.animateTo(
            targetValue = 1.6f,
            animationSpec = tween(durationMillis = 10000, easing = LinearEasing)
        )

        when (result.endReason) {
            AnimationEndReason.Finished -> {
                val backdropCount = availableBackdropPaths.count()
                val nextIndex = currentBackdropPathIndex + 1

                currentBackdropPathIndex = if (nextIndex >= backdropCount) 0 else nextIndex
            }
            else -> Unit
        }
    }

    Box(modifier = modifier.clip(RectangleShape)) {
        BoxWithConstraints(modifier = Modifier.matchParentSize()) {
            val (maxWidth, maxHeight) = getMaxSizeInt()

            Crossfade(
                modifier = Modifier.fillMaxSize(),
                targetState = currentBackdropPath
            ) { path ->
                val backgroundPainter = rememberTmdbImagePainter(
                    path = path,
                    type = ImageUrlParser.ImageType.Backdrop,
                    preferredSize = android.util.Size(maxWidth, maxHeight),
                    builder = {
                        fadeIn(animationSpec = spring())
                        transformations(
                            BlurTransformation(
                                context = context,
                                radius = 16f,
                                sampling = 6f
                            )
                        )
                    }
                )

                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(backdropScale.value),
                    painter = backgroundPainter,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
            }

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
                    showAdult = true,
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