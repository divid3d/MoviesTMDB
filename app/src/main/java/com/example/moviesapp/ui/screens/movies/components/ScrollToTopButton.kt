package com.example.moviesapp.ui.screens.movies.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.example.moviesapp.ui.theme.Black500
import com.example.moviesapp.ui.theme.spacing

@Composable
fun ScrollToTop(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    IconButton(
        modifier = modifier
            .clip(shape = CircleShape)
            .background(Black500)
            .rotate(90f)
            .padding(MaterialTheme.spacing.medium),
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.size(48.dp),
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = null
        )
    }
}