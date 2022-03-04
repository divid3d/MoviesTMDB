package com.example.moviesapp.ui.components.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.moviesapp.R

@Composable
fun NoPhotoPresentableItem(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colors.surface)
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.primary.copy(alpha = 0.5f),
                shape = MaterialTheme.shapes.medium
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_outline_no_photography_24),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colors.primary.copy(alpha = 0.3f))
        )
    }
}