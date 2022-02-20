package com.example.moviesapp.ui.screens.details.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.moviesapp.R
import com.example.moviesapp.model.TvSeriesDetails
import com.example.moviesapp.ui.components.LabeledText
import com.example.moviesapp.ui.theme.spacing

@Composable
fun TvSeriesDetailsTopContent(
    modifier: Modifier = Modifier,
    tvSeriesDetails: TvSeriesDetails?
) {
    Crossfade(
        modifier = modifier,
        targetState = tvSeriesDetails
    ) { details ->
        if (details != null) {
            Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)) {
                tvSeriesDetails?.let { details ->
                    LabeledText(
                        label = stringResource(R.string.tv_series_details_type),
                        text = stringResource(details.type.getLabel())
                    )

                    LabeledText(
                        label = stringResource(R.string.tv_series_details_status),
                        text = stringResource(details.status.getLabel())
                    )

                    LabeledText(
                        label = stringResource(R.string.tv_series_details_in_production),
                        text = stringResource(if (details.inProduction) R.string.yes else R.string.no)
                    )
                }
            }
        }
    }

}