package com.example.moviesapp.ui.screens.details.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.moviesapp.R
import com.example.moviesapp.model.MovieDetails
import com.example.moviesapp.other.formattedMoney
import com.example.moviesapp.ui.components.LabeledText
import com.example.moviesapp.ui.theme.spacing

@Composable
fun MovieDetailsTopContent(
    modifier: Modifier = Modifier,
    movieDetails: MovieDetails?
) {
    Crossfade(
        modifier = modifier,
        targetState = movieDetails
    ) { details ->
        if (details != null) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
                LabeledText(
                    label = stringResource(R.string.movie_details_status),
                    text = stringResource(details.status.getLabel())
                )

                if (details.budget > 0) {
                    LabeledText(
                        label = stringResource(R.string.movie_details_budget),
                        text = details.budget.formattedMoney()
                    )
                }

                if (details.revenue > 0) {
                    LabeledText(
                        label = stringResource(R.string.movie_details_boxoffice),
                        text = details.revenue.formattedMoney()
                    )
                }
            }
        }
    }
}