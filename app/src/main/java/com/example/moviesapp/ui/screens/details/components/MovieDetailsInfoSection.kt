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
import com.example.moviesapp.model.MovieDetails
import com.example.moviesapp.model.ShareDetails
import com.example.moviesapp.other.formattedRuntime
import com.example.moviesapp.other.timeString
import com.example.moviesapp.other.yearString
import com.example.moviesapp.ui.components.sections.GenresSection
import com.example.moviesapp.ui.components.texts.AdditionalInfoText
import com.example.moviesapp.ui.components.texts.ExpandableText
import com.example.moviesapp.ui.theme.spacing
import java.util.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MovieDetailsInfoSection(
    modifier: Modifier = Modifier,
    movieDetails: MovieDetails?,
    watchAtTime: Date?,
    imdbExternalId: ExternalId.Imdb? = null,
    onShareClicked: (ShareDetails) -> Unit = {}
) {
    val otherOriginalTitle: Boolean by derivedStateOf {
        movieDetails?.run { originalTitle.isNotEmpty() && title != originalTitle } ?: false
    }

    val watchAtTimeString = watchAtTime?.let { time ->
        stringResource(R.string.movie_details_watch_at, time.timeString())
    }

    Crossfade(
        modifier = modifier,
        targetState = movieDetails
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
                            text = details.title,
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        if (otherOriginalTitle) {
                            Text(text = details.originalTitle)
                        }
                        AdditionalInfoText(
                            modifier = Modifier.fillMaxWidth(),
                            infoTexts = details.run {
                                listOfNotNull(
                                    releaseDate?.yearString(),
                                    runtime?.formattedRuntime(),
                                    watchAtTimeString
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
                    details.tagline?.let { tagline ->
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