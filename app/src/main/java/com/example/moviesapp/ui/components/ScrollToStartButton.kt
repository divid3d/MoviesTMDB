package com.example.moviesapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.example.moviesapp.ui.theme.Black500

@Composable
fun ScrollToStartButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    IconButton(
        modifier = modifier
            .clip(shape = CircleShape)
            .background(Black500),
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "scroll to start",
            tint = MaterialTheme.colors.primary
        )
    }
}