package com.example.moviesapp.ui.screens.discoverMovies.components

import androidx.compose.foundation.Image
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.moviesapp.R

@Composable
fun FilterFloatingButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    FloatingActionButton(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.primary,
        onClick = onClick
    ) {
        Image(
            painter = painterResource(R.drawable.ic_baseline_filter_list_24),
            contentDescription = "filter"
        )
    }
}