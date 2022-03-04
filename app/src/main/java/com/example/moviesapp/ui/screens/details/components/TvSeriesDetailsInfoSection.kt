package com.example.moviesapp.ui.screens.details.components

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.moviesapp.R
import com.example.moviesapp.model.ExternalId
import com.example.moviesapp.model.ShareDetails
import com.example.moviesapp.model.TvSeriesDetails
import com.example.moviesapp.other.yearRangeString
import com.example.moviesapp.ui.components.sections.GenresSection
import com.example.moviesapp.ui.components.texts.AdditionalInfoText
import com.example.moviesapp.ui.components.texts.ExpandableText
import com.example.moviesapp.ui.theme.spacing

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TvSeriesDetailsInfoSection(
    modifier: Modifier = Modifier,
    tvSeriesDetails: TvSeriesDetails?,
    nextEpisodeDaysRemaining: Long?,
    imdbExternalId: ExternalId.Imdb? = null,
    onShareClicked: (ShareDetails) -> Unit = {}
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
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

                    AnimatedVisibility(
                        visible = imdbExternalId != null,
                        enter = fadeIn() + scaleIn(initialScale = 0.7f),
                        exit = fadeOut() + scaleOut()
                    ) {
                        IconButton(
                            modifier = Modifier.background(
                                color = MaterialTheme.colors.surface,
                                shape = CircleShape
                            ),
                            onClick = {
                                imdbExternalId?.let { id ->
                                    val shareDetails = ShareDetails(
                                        title = details.title,
                                        imdbId = id
                                    )

                                    onShareClicked(shareDetails)
                                }
                            }
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_baseline_share_24),
                                contentDescription = "share",
                                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
                            )
                        }
                    }
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