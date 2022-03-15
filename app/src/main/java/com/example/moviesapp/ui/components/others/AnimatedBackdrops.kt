package com.example.moviesapp.ui.components.others

import android.util.Size
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationEndReason
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.size.OriginalSize
import coil.size.Scale
import coil.transform.BlurTransformation
import com.example.moviesapp.other.ImageUrlParser
import com.example.moviesapp.other.getMaxSizeInt

@Composable
fun AnimatedBackdrops(
    paths: List<String>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var currentBackdropPathIndex by remember {
        mutableStateOf(0)
    }

    val currentBackdropPath by derivedStateOf {
        paths.getOrNull(currentBackdropPathIndex)
    }

    BoxWithConstraints(modifier = modifier) {
        val (maxWidth, maxHeight) = getMaxSizeInt()

        Crossfade(
            modifier = Modifier.fillMaxSize(),
            animationSpec = tween(1000),
            targetState = currentBackdropPath
        ) { path ->
            val backgroundPainter = rememberTmdbImagePainter(
                path = path,
                type = ImageUrlParser.ImageType.Backdrop,
                preferredSize = Size(maxWidth, maxHeight),
                builder = {
                    size(OriginalSize)
                    scale(Scale.FILL)
                    transformations(
                        BlurTransformation(
                            context = context,
                            radius = 18f,
                            sampling = 6f
                        )
                    )
                }
            )

            val backdropScale = remember { Animatable(1f) }

            LaunchedEffect(path) {
                val result = backdropScale.animateTo(
                    targetValue = 1.6f,
                    animationSpec = tween(durationMillis = 10000, easing = LinearEasing)
                )

                when (result.endReason) {
                    AnimationEndReason.Finished -> {
                        val backdropCount = paths.count()
                        val nextIndex = currentBackdropPathIndex + 1

                        currentBackdropPathIndex = if (nextIndex >= backdropCount) 0 else nextIndex
                    }
                    else -> Unit
                }
            }

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

}