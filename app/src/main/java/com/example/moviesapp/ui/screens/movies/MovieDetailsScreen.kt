package com.example.moviesapp.ui.screens.movies

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.R
import com.example.moviesapp.model.MediaType
import com.example.moviesapp.model.MovieRelationInfo
import com.example.moviesapp.model.RelationType
import com.example.moviesapp.other.formattedMoney
import com.example.moviesapp.other.formattedRuntime
import com.example.moviesapp.other.timeString
import com.example.moviesapp.other.yearString
import com.example.moviesapp.ui.components.*
import com.example.moviesapp.ui.components.dialogs.ErrorDialog
import com.example.moviesapp.ui.screens.destinations.MovieDetailsScreenDestination
import com.example.moviesapp.ui.screens.destinations.MoviesScreenDestination
import com.example.moviesapp.ui.screens.destinations.RelatedMoviesDestination
import com.example.moviesapp.ui.screens.destinations.ReviewsScreenDestination
import com.example.moviesapp.ui.screens.reviews.ReviewsScreenNavArgs
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.insets.navigationBarsHeight
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun MovieDetailsScreen(
    viewModel: MoviesDetailsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    movieId: Int,
    startRoute: String = MoviesScreenDestination.route
) {
    val movieDetails by viewModel.movieDetails.collectAsState()
    val isFavourite by viewModel.isFavourite.collectAsState()

    val watchAtTime by viewModel.watchAtTime.collectAsState()

    val credits by viewModel.credits.collectAsState()
    val backdrops by viewModel.backdrops.collectAsState()
    val movieCollection by viewModel.movieCollection.collectAsState()

    val similarMoviesState = viewModel.similarMoviesPagingDataFlow?.collectAsLazyPagingItems()
    val moviesRecommendationState =
        viewModel.moviesRecommendationPagingDataFlow?.collectAsLazyPagingItems()
    val hasReviews by viewModel.hasReviews.collectAsState()

    val otherOriginalTitle: Boolean by derivedStateOf {
        movieDetails?.run { originalTitle.isNotEmpty() && title != originalTitle } ?: false
    }

    val watchAtTimeString = watchAtTime?.let { time ->
        stringResource(R.string.movie_details_watch_at, time.timeString())
    }

    val scrollState = rememberScrollState()

    var showErrorDialog by remember { mutableStateOf(false) }
    val error: String? by viewModel.error.collectAsState()

    LaunchedEffect(error) {
        showErrorDialog = error != null
    }

    BackHandler(showErrorDialog) {
        showErrorDialog = false
    }

    if (showErrorDialog) {
        ErrorDialog(
            onDismissRequest = {
                showErrorDialog = false
            },
            onConfirmClick = {
                showErrorDialog = false
            }
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            PresentableDetailsTopSection(
                modifier = Modifier.fillMaxWidth(),
                presentable = movieDetails,
                backdrops = backdrops
            ) {
                movieDetails?.let { details ->
                    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)) {
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

                        if (details.genres.isNotEmpty()) {
                            GenresSection(
                                modifier = Modifier.padding(top = MaterialTheme.spacing.small),
                                genres = details.genres
                            )
                        }
                    }
                }
            }

            movieDetails?.let { details ->
                Column(
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.medium)
                        .animateContentSize(),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                ) {
                    Column {
                        Text(
                            text = details.title,
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        if (otherOriginalTitle) {
                            Text(text = details.originalTitle)
                        }
                        AdditionalInfoText(
                            modifier = Modifier.fillMaxWidth(),
                            infoTexts = details.run {
                                listOfNotNull(
                                    releaseDate.yearString(),
                                    runtime?.formattedRuntime(),
                                    watchAtTimeString
                                )
                            }
                        )
                    }

                    Column {
                        details.tagline?.let { tagline ->
                            if (tagline.isNotEmpty()) {
                                Text(
                                    text = "\"$tagline\"",
                                    style = TextStyle(fontStyle = FontStyle.Italic)
                                )
                            }
                        }

                        ExpandableText(
                            modifier = Modifier.fillMaxWidth(),
                            text = details.overview
                        )
                    }
                }

                SectionDivider(
                    modifier = Modifier.padding(top = MaterialTheme.spacing.large)
                )
            }

            movieCollection?.let { collection ->
                if (collection.parts.isNotEmpty()) {
                    PresentableListSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.surface)
                            .padding(vertical = MaterialTheme.spacing.small)
                            .animateContentSize(),
                        title = collection.name,
                        list = collection.parts.sortedBy { part -> part.releaseDate },
                        selectedId = movieId
                    ) { id ->
                        if (movieId != id) {
                            navigator.navigate(
                                MovieDetailsScreenDestination(
                                    movieId = id,
                                    startRoute = startRoute
                                )
                            )
                        }
                    }
                }
            }

            credits?.cast?.let { castMembers ->
                if (castMembers.isNotEmpty()) {
                    MemberSection(
                        modifier = Modifier
                            .padding(top = MaterialTheme.spacing.small)
                            .fillMaxWidth()
                            .animateContentSize(),
                        title = "Obsada",
                        members = castMembers,
                        contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium)
                    )

                    SectionDivider(
                        modifier = Modifier.padding(top = MaterialTheme.spacing.medium)
                    )
                }
            }

            credits?.crew?.let { crewMembers ->
                if (crewMembers.isNotEmpty()) {
                    MemberSection(
                        modifier = Modifier
                            .padding(top = MaterialTheme.spacing.small)
                            .fillMaxWidth()
                            .animateContentSize(),
                        title = "Ekipa filmowa",
                        members = crewMembers,
                        contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium)
                    )

                    SectionDivider(
                        modifier = Modifier.padding(top = MaterialTheme.spacing.medium)
                    )
                }
            }


            similarMoviesState?.let { lazyPagingItems ->
                PresentableSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MaterialTheme.spacing.medium)
                        .animateContentSize(),
                    title = stringResource(R.string.movie_details_similar),
                    state = lazyPagingItems,
                    onMoreClick = {
                        val movieRelationInfo = MovieRelationInfo(
                            movieId = movieId,
                            type = RelationType.Similar
                        )

                        navigator.navigate(
                            RelatedMoviesDestination(
                                movieRelationInfo = movieRelationInfo
                            )
                        )
                    }
                ) { movieId ->
                    navigator.navigate(
                        MovieDetailsScreenDestination(
                            movieId = movieId,
                            startRoute = startRoute
                        )
                    )
                }

                SectionDivider(
                    modifier = Modifier.padding(top = MaterialTheme.spacing.medium)
                )
            }

            moviesRecommendationState?.let { lazyPagingItems ->
                PresentableSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MaterialTheme.spacing.small)
                        .animateContentSize(),
                    title = stringResource(R.string.movie_details_recommendations),
                    state = lazyPagingItems,
                    onMoreClick = {
                        val movieRelationInfo = MovieRelationInfo(
                            movieId = movieId,
                            type = RelationType.Recommended
                        )

                        navigator.navigate(
                            RelatedMoviesDestination(
                                movieRelationInfo = movieRelationInfo
                            )
                        )
                    }
                ) { movieId ->
                    navigator.navigate(
                        MovieDetailsScreenDestination(
                            movieId = movieId,
                            startRoute = startRoute
                        )
                    )
                }
            }

            if (hasReviews) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SectionDivider(
                        modifier = Modifier.padding(top = MaterialTheme.spacing.medium)
                    )

                    ReviewSection(modifier = Modifier.fillMaxWidth()) {
                        val args = ReviewsScreenNavArgs(
                            mediaId = movieId,
                            type = MediaType.Movie
                        )

                        navigator.navigate(
                            ReviewsScreenDestination(args)
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.navigationBarsHeight(additional = MaterialTheme.spacing.large)
            )
        }
        AppBar(
            modifier = Modifier.align(Alignment.TopCenter),
            title = stringResource(R.string.movie_details_label),
            backgroundColor = Color.Black.copy(0.7f),
            action = {
                IconButton(onClick = { navigator.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "go back",
                        tint = MaterialTheme.colors.primary
                    )
                }
            },
            trailing = {
                Row(modifier = Modifier.padding(end = MaterialTheme.spacing.small)) {
                    LikeButton(
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
                    IconButton(
                        onClick = {
                            navigator.popBackStack(startRoute, inclusive = false)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "close",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
            }
        )
    }

}