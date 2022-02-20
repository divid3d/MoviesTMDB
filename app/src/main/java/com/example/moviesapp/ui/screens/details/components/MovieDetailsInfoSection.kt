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
import com.example.moviesapp.model.MovieDetails
import com.example.moviesapp.other.formattedRuntime
import com.example.moviesapp.other.timeString
import com.example.moviesapp.other.yearString
import com.example.moviesapp.ui.components.AdditionalInfoText
import com.example.moviesapp.ui.components.ExpandableText
import com.example.moviesapp.ui.components.GenresSection
import com.example.moviesapp.ui.theme.spacing
import java.util.*

@Composable
fun MovieDetailsInfoSection(
    modifier: Modifier = Modifier,
    movieDetails: MovieDetails?,
    watchAtTime: Date?
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
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
                Column {
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