package com.example.moviesapp.ui.screens.details.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.moviesapp.R
import com.example.moviesapp.model.TvSeriesDetails
import com.example.moviesapp.other.yearRangeString
import com.example.moviesapp.ui.components.AdditionalInfoText
import com.example.moviesapp.ui.components.ExpandableText
import com.example.moviesapp.ui.components.GenresSection
import com.example.moviesapp.ui.theme.spacing

@Composable
fun TvSeriesDetailsInfoSection(
    modifier: Modifier = Modifier,
    tvSeriesDetails: TvSeriesDetails?,
    nextEpisodeDaysRemaining: Long?
) {
    val otherOriginalTitle: Boolean by derivedStateOf {
        tvSeriesDetails?.run { !originalName.isNullOrEmpty() && title != originalName } ?: false
    }

    Crossfade(
        modifier = modifier,
        targetState = tvSeriesDetails
    ) { details ->
        if (details != null) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
                Column {
                    Text(
                        text = details.name,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    details.originalName?.let { name ->
                        if (otherOriginalTitle) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = name
                            )
                        }
                    }
                    AdditionalInfoText(
                        modifier = Modifier.fillMaxWidth(),
                        infoTexts = details.run {
                            listOfNotNull(
                                yearRangeString(
                                    from = firstAirDate,
                                    to = lastAirDate
                                ),
                                nextEpisodeDaysRemaining?.let { days ->
                                    when (days) {
                                        0L -> stringResource(R.string.next_episode_today_text)
                                        1L -> stringResource(R.string.next_episode_tomorrow_text)
                                        else -> stringResource(
                                            R.string.next_episode_days_text,
                                            days
                                        )
                                    }
                                }
                            )
                        }
                    )
                }

                if (details.genres.isNotEmpty()) {
                    GenresSection(
                        genres = details.genres
                    )
                }

                Column(
                    modifier = Modifier.padding(top = MaterialTheme.spacing.small),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
                ) {
                    details.tagline.let { tagline ->
                        if (tagline.isNotEmpty()) {
                            Text(
                                text = "\"$tagline\"",
                                fontStyle = FontStyle.Italic,
                                fontSize = 12.sp
                            )
                        }
                    }

                    ExpandableText(
                        modifier = Modifier.fillMaxWidth(),
                        text = details.overview
                    )
                }
            }
        }
    }

}