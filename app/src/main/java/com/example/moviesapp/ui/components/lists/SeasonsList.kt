package com.example.moviesapp.ui.components.lists

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.moviesapp.model.Season
import com.example.moviesapp.ui.theme.spacing

@Composable
fun SeasonsList(
    seasons: List<Season>,
    selectedSeasonId: Int?,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(MaterialTheme.spacing.default),
    onSeasonClick: (Int) -> Unit = {}
) {
    Column(modifier = modifier) {
        LazyRow(contentPadding = contentPadding) {
            items(seasons) { season ->
                SeasonButton(
                    selected = season.id == selectedSeasonId,
                    label = season.name,
                ) {
                    onSeasonClick(season.seasonNumber)
                }
            }
        }
    }
}

@Composable
fun SeasonButton(
    label: String,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) Color.White.copy(0.5f) else Color.Transparent
    )

    OutlinedButton(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor
        ),
        onClick = onClick
    ) {
        Text(text = label)
    }
}