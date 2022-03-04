package com.example.moviesapp.ui.components.sections

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.moviesapp.model.Genre
import com.example.moviesapp.ui.components.chips.GenreChip
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun GenresSection(
    modifier: Modifier = Modifier,
    genres: List<Genre>
) {
    FlowRow(
        modifier = modifier,
        mainAxisSpacing = MaterialTheme.spacing.extraSmall,
        crossAxisSpacing = MaterialTheme.spacing.extraSmall
    ) {
        genres.map { genre ->
            GenreChip(text = genre.name)
        }
    }
}