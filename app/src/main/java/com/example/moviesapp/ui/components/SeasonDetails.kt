package com.example.moviesapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.moviesapp.model.TvSeasonsResponse
import com.example.moviesapp.ui.theme.spacing

@Composable
fun SeasonDetails(
    modifier: Modifier = Modifier,
    season: TvSeasonsResponse
) {
    val context = LocalContext.current

    Box(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            PresentableItem(
                presentableState = PresentableItemState.Result(season),
                showScore = false,
                showTitle = false
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.medium)
            ) {
                Text(text = season.name)
                season.airDate?.let { date ->
                    Text(text = date)
                }
                Text(text = season.overview, maxLines = 5)
                Text(text = season.voteAverage.toString())
                Text(text = season.voteCount.toString())
            }
        }

    }

}