package com.example.moviesapp.ui.screens.movies

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.ui.components.AppBar
import com.example.moviesapp.ui.components.LikeButton
import com.example.moviesapp.ui.components.PresentableSection
import com.example.moviesapp.ui.screens.destinations.MovieDetailsScreenDestination
import com.example.moviesapp.ui.screens.movies.components.CastSection
import com.example.moviesapp.ui.screens.movies.components.CrewSection
import com.example.moviesapp.ui.screens.movies.components.MovieDetailsTopSection
import com.example.moviesapp.ui.screens.movies.components.OverviewSection
import com.example.moviesapp.ui.theme.Black500
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsHeight
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun MovieDetailsScreen(
    navigator: DestinationsNavigator,
    movieId: Int
) {
    val viewModel: MoviesDetailsViewModel = hiltViewModel()

    val insets = LocalWindowInsets.current
    val density = LocalDensity.current

    val movieDetails by viewModel.movieDetails.collectAsState()
    val credits by viewModel.credits.collectAsState()

    val similarMoviesState = viewModel.similarMoviesPagingDataFlow?.collectAsLazyPagingItems()
    val moviesRecommendationState =
        viewModel.moviesRecommendationPagingDataFlow?.collectAsLazyPagingItems()

    val otherOriginalTitle: Boolean by derivedStateOf {
        movieDetails?.run { originalTitle.isNotEmpty() && title != originalTitle } ?: false
    }

    val scrollState = rememberScrollState()

    var topSectionHeight: Int? by remember(movieDetails) {
        mutableStateOf(null)
    }

    val statusBarHeight by remember {
        mutableStateOf(
            insets.systemBars.layoutInsets.top
        )
    }

    val appbarHeight by derivedStateOf {
        density.run {
            56.dp.roundToPx()
        }
    }

//    val topAppbarBackgroundColor by animateColorAsState(targetValue = topSectionHeight?.let { height ->
//
//        val ratio = scrollState.value.toFloat() / (height - statusBarHeight - appbarHeight)
//        val alpha = 0.5f + ratio * 0.5f
//        print("alpha: $alpha")
//        Color.Black.copy(alpha = alpha.coerceAtMost(1f))
//    } ?: Black500)

//    val topAppbarBackgroundColor by animateColorAsState(targetValue = topSectionHeight?.let { height ->
//
//        val ratio = scrollState.value.toFloat() / (height - statusBarHeight - appbarHeight)
//        if (ratio >= 1f) Color.Black else Black500
//    } ?: Black500)


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            MovieDetailsTopSection(
                modifier = Modifier
                    .fillMaxWidth(),
                movieDetails = movieDetails
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            movieDetails?.let { details ->
                Column(
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                ) {
                    details.title?.let { title ->
                        Text(
                            text = title,
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    if (otherOriginalTitle) {
                        Text(text = details.originalTitle)
                    }

                    details.tagline?.let { tagline ->
                        if (tagline.isNotEmpty()) {
                            Text(
                                text = "\"$tagline\"",
                                style = TextStyle(fontStyle = FontStyle.Italic)
                            )
                        }
                    }
                    OverviewSection(
                        overview = details.overview
                    )
                }
            }
            credits?.cast?.let { members ->
                CastSection(
                    modifier = Modifier.animateContentSize(),
                    cast = members,
                    contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium)
                )
            }
            credits?.crew?.let { members ->
                CrewSection(
                    modifier = Modifier.animateContentSize(),
                    crew = members,
                    contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium)
                )
            }
            similarMoviesState?.let { lazyPagingItems ->
                PresentableSection(
                    modifier = Modifier
                        .fillMaxWidth(),
                    title = "Podobne",
                    showMoreButton = false,
                    state = lazyPagingItems
                ) { movieId ->
                    navigator.navigate(
                        MovieDetailsScreenDestination(movieId)
                    )
                }
            }
            moviesRecommendationState?.let { lazyPagingItems ->
                PresentableSection(
                    modifier = Modifier
                        .fillMaxWidth(),
                    title = "Polecane",
                    showMoreButton = false,
                    state = lazyPagingItems
                ) { movieId ->
                    navigator.navigate(
                        MovieDetailsScreenDestination(movieId)
                    )
                }
            }
            Spacer(
                modifier = Modifier.navigationBarsHeight(additional = MaterialTheme.spacing.large)
            )
        }
        AppBar(
            modifier = Modifier.align(Alignment.TopCenter),
            title = "Szczegóły filmu",
            backgroundColor = Black500,
            action = {
                IconButton(onClick = { navigator.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            },
            trailing = {
                val isFavourite = movieDetails?.isFavourite == true

                AnimatedVisibility(
                    visible = movieDetails != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    LikeButton(
                        modifier = Modifier.padding(end = MaterialTheme.spacing.small),
                        isFavourite = isFavourite,
                        onClick = {
                            movieDetails?.let { details ->
                                if (isFavourite) {
                                    viewModel.onUnlikeClick(details)
                                } else {
                                    viewModel.onLikeClick(details)
                                }
                            }
                        }
                    )
                }
            }
        )
    }

}