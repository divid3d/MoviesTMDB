package com.example.moviesapp.ui.components.buttons

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import com.example.moviesapp.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LikeButton(
    isFavourite: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        AnimatedContent(
            targetState = isFavourite,
            contentAlignment = Alignment.Center,
            transitionSpec = {
                fadeIn(animationSpec = tween(200)) + scaleIn(
                    animationSpec = tween(200, delayMillis = 200),
                    initialScale = 0.8f
                ) with scaleOut(
                    animationSpec = tween(200),
                    targetScale = 0.8f
                )
            }
        ) { favourite ->
            if (favourite) {
                Image(
                    painter = painterResource(R.drawable.ic_heart),
                    contentDescription = "add to favourite",
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.ic_heart_outline),
                    contentDescription = "remove from favourites",
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
                )
            }
        }
    }
}