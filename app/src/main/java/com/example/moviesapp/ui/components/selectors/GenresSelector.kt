package com.example.moviesapp.ui.components.selectors

import androidx.compose.animation.animateContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.moviesapp.model.Genre
import com.example.moviesapp.ui.components.chips.SelectableGenreChip
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun GenresSelector(
    modifier: Modifier = Modifier,
    genres: List<Genre>,
    selectedGenres: List<Genre>,
    onGenreClick: (Genre) -> Unit = {}
) {
    FlowRow(
        modifier = modifier,
        mainAxisSpacing = MaterialTheme.spacing.extraSmall,
        crossAxisSpacing = MaterialTheme.spacing.extraSmall
    ) {
        genres.sortedBy { genre ->
            genre.name
        }.map { genre ->
            SelectableGenreChip(
                modifier = Modifier.animateContentSize(),
                text = genre.name,
                selected = genre in selectedGenres,
                onClick = { onGenreClick(genre) }
            )
        }
    }
}