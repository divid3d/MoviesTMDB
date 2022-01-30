package com.example.moviesapp.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import com.example.moviesapp.R

@Composable
fun LikeButton(
    modifier: Modifier = Modifier,
    isFavourite: Boolean,
    onClick: () -> Unit = {}
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Crossfade(targetState = isFavourite) { favourite ->
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