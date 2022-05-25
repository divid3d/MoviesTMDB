package com.example.moviesapp.ui.screens.details

import android.os.Parcelable
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.R
import com.example.moviesapp.model.*
import com.example.moviesapp.other.*
import com.example.moviesapp.ui.components.buttons.LikeButton
import com.example.moviesapp.ui.components.dialogs.ErrorDialog
import com.example.moviesapp.ui.components.others.AppBar
import com.example.moviesapp.ui.components.sections.*
import com.example.moviesapp.ui.screens.destinations.*
import com.example.moviesapp.ui.screens.details.components.MovieDetailsInfoSection
import com.example.moviesapp.ui.screens.details.components.MovieDetailsTopContent
import com.example.moviesapp.ui.theme.Black300
import com.example.moviesapp.ui.theme.Black700
import com.example.moviesapp.ui.theme.spacing
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@OptIn(ExperimentalAnimationApi::class)
object MovieDetailsScreenTransitions : DestinationStyle.Animated {
    override fun AnimatedContentScope<NavBackStackEntry>.enterTransition(): EnterTransition? {
        return when (initialState.destination.route) {
            MoviesScreenDestination.route,
            FavouritesScreenDestination.route,
            SearchScreenDestination.route -> slideIntoContainer(
                towards = AnimatedContentScope.SlideDirection.Up,
                animationSpec = tween(300)
            )
            else -> null
        }
    }

    override fun AnimatedContentScope<NavBackStackEntry>.popEnterTransition(): EnterTransition? {
        return when (initialState.destination.route) {
            MoviesScreenDestination.route,
            FavouritesScreenDestination.route,
            SearchScreenDestination.route -> slideIntoContainer(
                towards = AnimatedContentScope.SlideDirection.Up,
                animationSpec = tween(300)
            )
            else -> null
        }
    }

    override fun AnimatedContentScope<NavBackStackEntry>.exitTransition(): ExitTransition? {
        return when (targetState.destination.route) {
            MoviesScreenDestination.route,
            FavouritesScreenDestination.route,
            SearchScreenDestination.route -> slideOutOfContainer(
                towards = AnimatedContentScope.SlideDirection.Down,
                animationSpec = tween(300)
            )
            else -> null
        }
    }

    override fun AnimatedContentScope<NavBackStackEntry>.popExitTransition(): ExitTransition? {
        return when (targetState.destination.route) {
            MoviesScreenDestination.route,
            FavouritesScreenDestination.route,
            SearchScreenDestination.route -> slideOutOfContainer(
                towards = AnimatedContentScope.SlideDirection.Down,
                animationSpec = tween(300)
            )
            else -> null
        }
    }
}

@Parcelize
data class MovieDetailsScreenArgs(
    val movieId: Int,
    val startRoute: String
) : Parcelable

@Destination(
    navArgsDelegate = MovieDetailsScreenArgs::class,
    style = MovieDetailsScreenTransitions::class
)
@Composable
fun AnimatedVisibilityScope.MovieDetailsScreen(
    viewModel: MoviesDetailsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsState()
    val onBackClicked: () -> Unit = { navigator.navigateUp() }
    val onFavouriteClicked: (details: MovieDetails) -> Unit = { details ->
        if (uiState.additionalMovieDetailsInfo.isFavourite) {
            viewModel.onUnlikeClick(details)
        } else {
            viewModel.onLikeClick(details)
        }
    }
    val onCloseClicked: () -> Unit = {
        navigator.popBackStack(uiState.startRoute, inclusive = false)
    }
    val onExternalIdClicked = { id: ExternalId ->
        openExternalId(
            context = context,
            externalId = id
        )
    }
    val onShareClicked = { details: ShareDetails ->
        shareImdb(
            context = context,
            details = details
        )
    }
    val onVideoClicked = { video: Video ->
        openVideo(
            context = context,
            video = video
        )
    }
    val onMemberClicked = { personId: Int ->
        val destination = PersonDetailsScreenDestination(
            personId = personId,
            startRoute = uiState.startRoute
        )

        navigator.navigate(destination)
    }
    val onMovieClicked = { movieId: Int ->
        val destination = MovieDetailsScreenDestination(
            movieId = movieId,
            startRoute = uiState.startRoute
        )

        navigator.navigate(destination)
    }

    val onReviewsClicked: () -> Unit = {
        val movieId = uiState.movieDetails?.id

        if (movieId != null) {
            val destination = ReviewsScreenDestination(
                startRoute = uiState.startRoute,
                mediaId = movieId,
                type = MediaType.Movie
            )

            navigator.navigate(destination)
        }
    }


    val onSimilarMoreClicked = {
        val movieId = uiState.movieDetails?.id

        if (movieId != null) {
            val destination = RelatedMoviesScreenDestination(
                movieId = movieId,
                type = RelationType.Similar,
                startRoute = uiState.startRoute
            )

            navigator.navigate(destination)
        }
    }

    val onRecommendationsMoreClicked = {
        val movieId = uiState.movieDetails?.id

        if (movieId != null) {
            val destination = RelatedMoviesScreenDestination(
                movieId = movieId,
                type = RelationType.Recommended,
                startRoute = uiState.startRoute
            )

            navigator.navigate(destination)
        }
    }

    MovieDetailsScreenContent(
        uiState = uiState,
        onBackClicked = onBackClicked,
        onExternalIdClicked = onExternalIdClicked,
        onShareClicked = onShareClicked,
        onVideoClicked = onVideoClicked,
        onFavouriteClicked = onFavouriteClicked,
        onCloseClicked = onCloseClicked,
        onMemberClicked = onMemberClicked,
        onMovieClicked = onMovieClicked,
        onSimilarMoreClicked = onSimilarMoreClicked,
        onRecommendationsMoreClicked = onRecommendationsMoreClicked,
        onReviewsClicked = onReviewsClicked
    )
}

@Composable
fun MovieDetailsScreenContent(
    uiState: MovieDetailsScreenUiState,
    onBackClicked: () -> Unit,
    onExternalIdClicked: (id: ExternalId) -> Unit,
    onShareClicked: (details: ShareDetails) -> Unit,
    onVideoClicked: (video: Video) -> Unit,
    onFavouriteClicked: (details: MovieDetails) -> Unit,
    onCloseClicked: () -> Unit,
    onMemberClicked: (personId: Int) -> Unit,
    onMovieClicked: (movieId: Int) -> Unit,
    onSimilarMoreClicked: () -> Unit,
    onRecommendationsMoreClicked: () -> Unit,
    onReviewsClicked: () -> Unit
) {
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()

    val similarMoviesState = uiState.associatedMovies.similar.collectAsLazyPagingItems()
    val moviesRecommendationState =
        uiState.associatedMovies.recommendations.collectAsLazyPagingItems()
    val otherDirectorMovies =
        uiState.associatedMovies.directorMovies.movies.collectAsLazyPagingItems()

    val scrollState = rememberScrollState()
    val scrollToStart = {
        coroutineScope.launch {
            scrollState.animateScrollTo(0)
        }
    }

    val imdbExternalId by derivedStateOf {
        uiState.associatedContent.externalIds?.filterIsInstance<ExternalId.Imdb>()?.firstOrNull()
    }

    var showErrorDialog by remember { mutableStateOf(false) }

    var topSectionHeight: Float? by remember { mutableStateOf(null) }
    val appbarHeight = density.run { 56.dp.toPx() }
    val topSectionScrollLimitValue: Float? = topSectionHeight?.minus(appbarHeight)

    LaunchedEffect(uiState.error) {
        showErrorDialog = uiState.error != null
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
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
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
                presentable = uiState.movieDetails,
                backdrops = uiState.associatedContent.backdrops,
                scrollState = scrollState,
                scrollValueLimit = topSectionScrollLimitValue
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Black300,
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(MaterialTheme.spacing.small)
                ) {
                    MovieDetailsTopContent(
                        modifier = Modifier.fillMaxWidth(),
                        movieDetails = uiState.movieDetails
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Crossfade(
                    modifier = Modifier.fillMaxWidth(),
                    targetState = uiState.associatedContent.externalIds
                ) { ids ->
                    if (ids != null) {
                        ExternalIdsSection(
                            modifier = Modifier.fillMaxWidth(),
                            externalIds = ids,
                            onExternalIdClick = onExternalIdClicked
                        )
                    }
                }
            }

            MovieDetailsInfoSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium)
                    .animateContentSize(),
                movieDetails = uiState.movieDetails,
                watchAtTime = uiState.additionalMovieDetailsInfo.watchAtTime,
                imdbExternalId = imdbExternalId,
                onShareClicked = onShareClicked
            )

            AnimatedContentContainer(
                modifier = Modifier.fillMaxWidth(),
                visible = uiState.additionalMovieDetailsInfo.watchProviders != null
            ) {
                if (uiState.additionalMovieDetailsInfo.watchProviders != null) {
                    WatchProvidersSection(
                        modifier = Modifier.fillMaxWidth(),
                        watchProviders = uiState.additionalMovieDetailsInfo.watchProviders,
                        title = stringResource(R.string.available_at)
                    )
                }
            }

            AnimatedContentContainer(
                modifier = Modifier.fillMaxWidth(),
                visible = !uiState.additionalMovieDetailsInfo.credits?.cast.isNullOrEmpty()
            ) {
                uiState.additionalMovieDetailsInfo.credits?.cast?.ifNotNullAndEmpty { members ->
                    MemberSection(
                        modifier = Modifier.fillMaxWidth(),
                        title = stringResource(R.string.movie_details_cast),
                        members = members,
                        contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium),
                        onMemberClick = onMemberClicked
                    )
                }
            }

            AnimatedContentContainer(
                modifier = Modifier.fillMaxWidth(),
                visible = !uiState.additionalMovieDetailsInfo.credits?.crew.isNullOrEmpty()
            ) {
                uiState.additionalMovieDetailsInfo.credits?.crew.ifNotNullAndEmpty { members ->
                    MemberSection(
                        modifier = Modifier.fillMaxWidth(),
                        title = stringResource(R.string.movie_details_crew),
                        members = members,
                        contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium),
                        onMemberClick = onMemberClicked
                    )
                }
            }

            AnimatedContentContainer(
                modifier = Modifier.fillMaxWidth(),
                visible = uiState.associatedMovies.collection?.run { parts.isNotEmpty() } == true
            ) {
                val movieCollection = uiState.associatedMovies.collection

                if (movieCollection != null && movieCollection.parts.isNotEmpty()) {
                    PresentableListSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.surface)
                            .padding(vertical = MaterialTheme.spacing.small),
                        title = movieCollection.name,
                        list = movieCollection.parts.sortedBy { part -> part.releaseDate },
                        selectedId = uiState.movieDetails?.id,
                        onPresentableClick = { movieId ->
                            if (movieId != uiState.movieDetails?.id) {
                                onMovieClicked(movieId)
                            } else {
                                scrollToStart()
                            }
                        }
                    )
                }
            }

            Crossfade(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                targetState = otherDirectorMovies
            ) { otherDirectorMovies ->
                if (otherDirectorMovies.hasItems()) {
                    PresentableSection(
                        modifier = Modifier.fillMaxWidth(),
                        title = stringResource(
                            id = R.string.movie_details_director_movies,
                            uiState.associatedMovies.directorMovies.directorName
                        ),
                        state = otherDirectorMovies,
                        showLoadingAtRefresh = false,
                        showMoreButton = false,
                        onMoreClick = onSimilarMoreClicked,
                        onPresentableClick = { movieId ->
                            if (movieId != uiState.movieDetails?.id) {
                                onMovieClicked(movieId)
                            } else {
                                scrollToStart()
                            }
                        }
                    )
                }
            }

            Crossfade(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                targetState = moviesRecommendationState
            ) { movieRecommendation ->
                if (movieRecommendation.hasItems()) {
                    PresentableSection(
                        modifier = Modifier.fillMaxWidth(),
                        title = stringResource(R.string.movie_details_recommendations),
                        state = movieRecommendation,
                        showLoadingAtRefresh = false,
                        onMoreClick = onRecommendationsMoreClicked,
                        onPresentableClick = { movieId ->
                            if (movieId != uiState.movieDetails?.id) {
                                onMovieClicked(movieId)
                            } else {
                                scrollToStart()
                            }
                        }
                    )
                }
            }

            Crossfade(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                targetState = similarMoviesState
            ) { similarMovies ->
                if (similarMovies.hasItems()) {
                    PresentableSection(
                        modifier = Modifier.fillMaxWidth(),
                        title = stringResource(R.string.movie_details_similar),
                        state = similarMovies,
                        showLoadingAtRefresh = false,
                        onMoreClick = onSimilarMoreClicked,
                        onPresentableClick = { movieId ->
                            if (movieId != uiState.movieDetails?.id) {
                                onMovieClicked(movieId)
                            } else {
                                scrollToStart()
                            }
                        }
                    )
                }
            }

            Crossfade(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                targetState = uiState.associatedContent.videos
            ) { videos ->
                videos.ifNotNullAndEmpty { value ->
                    VideosSection(
                        modifier = Modifier.fillMaxWidth(),
                        title = stringResource(R.string.season_details_videos_label),
                        videos = value,
                        contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium),
                        onVideoClicked = onVideoClicked
                    )
                }
            }

            AnimatedVisibility(
                modifier = Modifier.fillMaxWidth(),
                visible = uiState.additionalMovieDetailsInfo.reviewsCount > 0
            ) {
                ReviewSection(
                    modifier = Modifier.fillMaxWidth(),
                    count = uiState.additionalMovieDetailsInfo.reviewsCount,
                    onClick = onReviewsClicked
                )
            }

            Spacer(
                modifier = Modifier.windowInsetsBottomHeight(
                    insets = WindowInsets(bottom = MaterialTheme.spacing.medium)
                )
            )
        }

        AppBar(
            modifier = Modifier.align(Alignment.TopCenter),
            title = stringResource(R.string.movie_details_label),
            backgroundColor = Black700,
            scrollState = scrollState,
            transparentScrollValueLimit = topSectionScrollLimitValue,
            action = {
                IconButton(onClick = onBackClicked) {
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
                        isFavourite = uiState.additionalMovieDetailsInfo.isFavourite,
                        onClick = {
                            val details = uiState.movieDetails

                            if (details != null) {
                                onFavouriteClicked(details)
                            }
                        }
                    )
                    IconButton(
                        onClick = onCloseClicked
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

@Composable
fun AnimatedContentContainer(
    modifier: Modifier = Modifier,
    visible: Boolean,
    content: @Composable BoxScope.() -> Unit = {}
) {
    val visibleState = remember { MutableTransitionState(false) }
    visibleState.targetState = visible

    val transition = updateTransition(visibleState, label = "visibility transition")
    val alpha by transition.animateFloat(
        transitionSpec = {
            if (false isTransitioningTo true) {
                tween(delayMillis = 300)
            } else {
                spring()
            }
        },
        label = "alpha transition"
    ) { isVisible -> if (isVisible) 1f else 0f }


    Box(
        modifier = modifier
            .alpha(alpha)
            .animateContentSize(tween(300)), content = content
    )
}