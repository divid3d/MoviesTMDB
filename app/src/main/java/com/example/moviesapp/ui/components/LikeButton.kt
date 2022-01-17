package com.example.moviesapp.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.moviesapp.R

@Composable
fun LikeButton(
    modifier: Modifier = Modifier,
    isFavourite: Boolean,
    onClick: () -> Unit = {}
) {
    IconButton(onClick = onClick) {
        Crossfade(targetState = isFavourite) { favourite ->
            if (favourite) {
                Image(
                    painter = painterResource(R.drawable.ic_heart),
                    contentDescription = null,
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.ic_heart_outline),
                    contentDescription = null,
                )
            }
        }
    }
}