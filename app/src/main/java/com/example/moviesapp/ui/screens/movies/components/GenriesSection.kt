package com.example.moviesapp.ui.screens.movies.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.moviesapp.model.Genre
import com.example.moviesapp.ui.theme.Black500
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

@Composable
fun GenreChip(
    modifier: Modifier = Modifier,
    text: String
) {
    Box(
        modifier = modifier
            .background(shape = RoundedCornerShape(50f), color = Black500)
            .padding(
                horizontal = MaterialTheme.spacing.small,
                vertical = MaterialTheme.spacing.extraSmall
            )
    ) {
        Text(
            text = text,
            style = TextStyle(color = Color.White, fontSize = 12.sp)
        )
    }
}