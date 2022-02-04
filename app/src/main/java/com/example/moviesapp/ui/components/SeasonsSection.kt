package com.example.moviesapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.moviesapp.model.PresentableItemState
import com.example.moviesapp.model.Season
import com.example.moviesapp.ui.theme.spacing

@Composable
fun SeasonsSection(
    modifier: Modifier = Modifier,
    title: String,
    seasons: List<Season>,
    onSeasonClick: (Int) -> Unit = {}
) {
    val state = rememberLazyListState()

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = MaterialTheme.spacing.medium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionLabel(
                modifier = Modifier.weight(1f),
                text = title
            )
        }
        Box {
            LazyRow(
                state = state,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MaterialTheme.spacing.small),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium)
            ) {
                items(seasons) { season ->
                    PresentableItem(
                        presentableState = PresentableItemState.Result(season),
                        showScore = false,
                        onClick = { onSeasonClick(season.seasonNumber) }
                    )
                }
            }
        }
    }

}