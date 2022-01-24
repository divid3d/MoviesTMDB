package com.example.moviesapp.ui.components

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.moviesapp.ui.theme.White500
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

@Composable
fun PosterPlaceholder(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .placeholder(
                visible = true,
                highlight = PlaceholderHighlight.shimmer(
                    highlightColor = White500, animationSpec = InfiniteRepeatableSpec(
                        animation = tween(durationMillis = 500, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    )
                ),
                color = MaterialTheme.colors.background,
                shape = MaterialTheme.shapes.medium
            )
    )
}
