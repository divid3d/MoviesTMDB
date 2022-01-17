package com.example.moviesapp.ui.screens.movies.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.moviesapp.model.MovieDetails
import com.example.moviesapp.ui.components.PosterPlaceholder
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun MovieDetailsTopSection(
    modifier: Modifier = Modifier,
    movieDetails: MovieDetails?
) {
    val isLoading by derivedStateOf {
        movieDetails == null
    }

    Box(modifier = modifier) {
        Image(
            modifier = Modifier
                .matchParentSize()
                .blur(16.dp),
            painter = rememberImagePainter(
                data = movieDetails?.backdropUrl,
                builder = {
                    fadeIn(animationSpec = spring())
                }
            ),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(128.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, MaterialTheme.colors.background),
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 56.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.medium)
            ) {
                Crossfade(targetState = isLoading) { state ->
                    if (state) {
                        PosterPlaceholder(
                            modifier = Modifier
                                .width(160.dp)
                                .height(250.dp)
                        )
                    } else {
                        Card(
                            modifier = Modifier
                                .width(160.dp)
                                .height(250.dp),
                            shape = MaterialTheme.shapes.medium,
                            backgroundColor = Color.LightGray
                        ) {
                            Image(
                                painter = rememberImagePainter(
                                    data = movieDetails?.posterUrl,
                                    builder = {
                                        fadeIn(animationSpec = spring())
                                    }
                                ),
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                Column(modifier = Modifier.weight(1f)) {
                    movieDetails?.let { details ->
                        Text(details.budget.toString())
                        Text(details.status)
                        Text(details.popularity.toString())
                        Text(details.voteAverage.toString())
                        Text(details.voteCount.toString())
                        Text(details.revenue.toString())
                        GenresSection(genres = details.genres)
                    }
                }
            }
        }
    }
}