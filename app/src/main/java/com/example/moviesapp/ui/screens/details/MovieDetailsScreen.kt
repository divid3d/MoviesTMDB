package com.example.moviesapp.ui.screens.details

import android.os.Parcelable
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.R
import com.example.moviesapp.model.ExternalId
import com.example.moviesapp.model.MediaType
import com.example.moviesapp.model.RelationType
import com.example.moviesapp.other.*
import com.example.moviesapp.ui.components.*
import com.example.moviesapp.ui.components.dialogs.ErrorDialog
import com.example.moviesapp.ui.screens.destinations.MovieDetailsScreenDestination
import com.example.moviesapp.ui.screens.destinations.PersonDetailsScreenDestination
import com.example.moviesapp.ui.screens.destinations.RelatedMoviesScreenDestination
import com.example.moviesapp.ui.screens.destinations.ReviewsScreenDestination
import com.example.moviesapp.ui.screens.details.components.MovieDetailsInfoSection
import com.example.moviesapp.ui.screens.details.components.MovieDetailsTopContent
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.insets.navigationBarsHeight
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieDetailsScreenArgs(
    val movieId: Int,
    val startRoute: String
) : Parcelable

@Destination(navArgsDelegate = MovieDetailsScreenArgs::class)
@Composable
fun MovieDetailsScreen(
    viewModel: MoviesDetailsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    navBackStackEntry: NavBackStackEntry
) {
    val navArgs: MovieDetailsScreenArgs by derivedStateOf {
        MovieDetailsScreenDestination.argsFrom(navBackStackEntry)
    }

    val context = LocalContext.current
    val density = LocalDensity.current

    val movieDetails by viewModel.movieDetails.collectAsState()
    val isFavourite by viewModel.isFavourite.collectAsState()

    val watchAtTime by viewModel.watchAtTime.collectAsState()

    val credits by viewModel.credits.collectAsState()
    val watchProviders by viewModel.watchProviders.collectAsState()
    val backdrops by viewModel.backdrops.collectAsState()
    val videos by viewModel.videos.collectAsState()
    val movieCollection by viewModel.movieCollection.collectAsState()
    val externalIds by viewModel.externalIds.collectAsState()

    val similarMoviesState = viewModel.similarMoviesPagingDataFlow.collectAsLazyPagingItems()
    val moviesRecommendationState =
        viewModel.moviesRecommendationPagingDataFlow.collectAsLazyPagingItems()
    val hasReviews by viewModel.hasReviews.collectAsState()

    val scrollState = rememberScrollState()

    val imdbExternalId by derivedStateOf {
        externalIds?.filterIsInstance<ExternalId.Imdb>()?.firstOrNull()
    }

    var showErrorDialog by remember { mutableStateOf(false) }
    val error: String? by viewModel.error.collectAsState()

    var topSectionHeight: Float? by remember { mutableStateOf(null) }
    val appbarHeight = density.run { 56.dp.toPx() }
    val topSectionScrollLimitValue: Float? = topSectionHeight?.minus(appbarHeight)

    val navigateToDetails = { movieId: Int ->
        val destination = MovieDetailsScreenDestination(
            movieId = movieId,
            startRoute = navArgs.startRoute
        )

        navigator.navigate(destination)
    }

    val navigateToPersonDetails = { personId: Int ->
        val destination = PersonDetailsScreenDestination(
            personId = personId,
            startRoute = navArgs.startRoute
        )

        navigator.navigate(destination)
    }

    val navigateToSimilar = {
        val destination = RelatedMoviesScreenDestination(
            movieId = navArgs.movieId,
            type = RelationType.Similar,
            startRoute = navArgs.startRoute
        )

        navigator.navigate(destination)
    }

    val navigateToRecommendations = {
        val destination = RelatedMoviesScreenDestination(
            movieId = navArgs.movieId,
            type = RelationType.Recommended,
            startRoute = navArgs.startRoute
        )

        navigator.navigate(destination)
    }

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
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            PresentableDetailsTopSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        topSectionHeight = coordinates.size.height.toFloat()
                    },
                presentable = movieDetails,
                backdrops = backdrops,
                scrollState = scrollState,
                scrollValueLimit = topSectionScrollLimitValue
            ) {
                MovieDetailsTopContent(
                    modifier = Modifier.fillMaxWidth(),
                    movieDetails = movieDetails
                )

                Spacer(modifier = Modifier.weight(1f))

                Crossfade(
                    modifier = Modifier.fillMaxWidth(),
                    targetState = externalIds
                ) { ids ->
                    if (ids != null) {
                        ExternalIdsSection(
                            modifier = Modifier.fillMaxWidth(),
                            externalIds = ids
                        ) { externalId ->
                            openExternalId(
                                context = context,
                                externalId = externalId
                            )
                        }
                    }
                }
            }

            MovieDetailsInfoSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium)
                    .animateContentSize(),
                movieDetails = movieDetails,
                watchAtTime = watchAtTime,
                imdbExternalId = imdbExternalId,
                onShareClicked = { details ->
                    shareImdb(
                        context = context,
                        details = details
                    )
                }
            )

            Crossfade(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                targetState = watchProviders
            ) { providers ->
                if (providers != null) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                    ) {
                        SectionDivider(modifier = Modifier.fillMaxWidth())
                        WatchProvidersSection(
                            modifier = Modifier.fillMaxWidth(),
                            watchProviders = providers,
                            title = stringResource(R.string.available_at)
                        )
                    }
                }
            }

            Crossfade(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                targetState = credits?.cast
            ) { cast ->
                cast.ifNotNullAndEmpty { members ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                    ) {
                        SectionDivider(modifier = Modifier.fillMaxWidth())
                        MemberSection(
                            modifier = Modifier.fillMaxWidth(),
                            title = "Obsada",
                            members = members,
                            contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium),
                            onMemberClick = { id -> navigateToPersonDetails(id) }
                        )
                    }
                }
            }

            Crossfade(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                targetState = credits?.crew
            ) { crew ->
                crew.ifNotNullAndEmpty { members ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                    ) {
                        SectionDivider(modifier = Modifier.fillMaxWidth())
                        MemberSection(
                            modifier = Modifier.fillMaxWidth(),
                            title = "Ekipa filmowa",
                            members = members,
                            contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium),
                            onMemberClick = { id -> navigateToPersonDetails(id) }
                        )
                    }
                }
            }

            Crossfade(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.surface)
                    .animateContentSize(),
                targetState = movieCollection
            ) { movieCollection ->
                if (movieCollection != null && movieCollection.parts.isNotEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                    ) {
                        SectionDivider(modifier = Modifier.fillMaxWidth())
                        PresentableListSection(
                            modifier = Modifier.fillMaxWidth(),
                            title = movieCollection.name,
                            list = movieCollection.parts.sortedBy { part -> part.releaseDate },
                            selectedId = navArgs.movieId,
                            onPresentableClick = { id -> navigateToDetails(id) }
                        )
                    }
                }
            }

            Crossfade(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                targetState = similarMoviesState
            ) { similarMovies ->
                if (similarMovies.isNotEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                    ) {
                        SectionDivider(modifier = Modifier.fillMaxWidth())
                        PresentableSection(
                            modifier = Modifier.fillMaxWidth(),
                            title = stringResource(R.string.movie_details_similar),
                            state = similarMovies,
                            onMoreClick = navigateToSimilar,
                            onPresentableClick = { id -> navigateToDetails(id) }
                        )
                    }
                }
            }

            Crossfade(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                targetState = moviesRecommendationState
            ) { movieRecommendation ->
                if (movieRecommendation.isNotEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                    ) {
                        SectionDivider(modifier = Modifier.fillMaxWidth())
                        PresentableSection(
                            modifier = Modifier.fillMaxWidth(),
                            title = stringResource(R.string.movie_details_recommendations),
                            state = movieRecommendation,
                            onMoreClick = navigateToRecommendations,
                            onPresentableClick = { id -> navigateToDetails(id) }
                        )
                    }
                }
            }

            Crossfade(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                targetState = videos
            ) { videos ->
                videos.ifNotNullAndEmpty { value ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                    ) {
                        SectionDivider(modifier = Modifier.fillMaxWidth())
                        VideosSection(
                            modifier = Modifier.fillMaxWidth(),
                            title = stringResource(R.string.season_details_videos_label),
                            videos = value,
                            contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium)
                        ) { video ->
                            openVideo(
                                context = context,
                                video = video
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(
                modifier = Modifier.fillMaxWidth(),
                visible = hasReviews
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                ) {
                    SectionDivider(modifier = Modifier.fillMaxWidth())
                    ReviewSection(modifier = Modifier.fillMaxWidth()) {
                        val destination = ReviewsScreenDestination(
                            mediaId = navArgs.movieId,
                            type = MediaType.Movie
                        )

                        navigator.navigate(destination)
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
            scrollState = scrollState,
            transparentScrollValueLimit = topSectionScrollLimitValue,
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
                            navigator.popBackStack(navArgs.startRoute, inclusive = false)
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