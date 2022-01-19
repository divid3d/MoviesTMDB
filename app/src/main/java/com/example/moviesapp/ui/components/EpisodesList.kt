package com.example.moviesapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.moviesapp.model.Episode
import com.example.moviesapp.ui.theme.spacing

@Composable
fun EpisodesList(
    modifier: Modifier = Modifier,
    episodes: List<Episode>,
    onEpisodeClick: (Int) -> Unit = {}
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
    ) {
        items(episodes) { episode ->
//            PresentableItem(
//                presentableState = PresentableState.Result(episode),
//                size = MaterialTheme.sizes.presentableItemSmall,
//                onClick = { onEpisodeClick(episode.id) }
//            )
        }
    }
}