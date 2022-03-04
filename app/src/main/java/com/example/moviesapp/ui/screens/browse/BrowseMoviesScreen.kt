@file:OptIn(ExperimentalFoundationApi::class)

package com.example.moviesapp.ui.screens.browse

import android.os.Parcelable
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.R
import com.example.moviesapp.model.MovieType
import com.example.moviesapp.ui.components.dialogs.InfoDialog
import com.example.moviesapp.ui.components.others.AppBar
import com.example.moviesapp.ui.components.sections.PresentableGridSection
import com.example.moviesapp.ui.screens.destinations.*
import com.example.moviesapp.ui.theme.spacing
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle
import kotlinx.parcelize.Parcelize

@OptIn(ExperimentalAnimationApi::class)
object BrowseMoviesScreenTransitions : DestinationStyle.Animated {
    override fun AnimatedContentScope<NavBackStackEntry>.enterTransition(): EnterTransition? {
        return when (initialState.destination.route) {
            MoviesScreenDestination.route -> slideIntoContainer(
                towards = AnimatedContentScope.SlideDirection.Up,
                animationSpec = tween(300)
            )
            else -> null
        }
    }

    override fun AnimatedContentScope<NavBackStackEntry>.popEnterTransition(): EnterTransition? {
        return when (initialState.destination.route) {
            MoviesScreenDestination.route -> slideIntoContainer(
                towards = AnimatedContentScope.SlideDirection.Up,
                animationSpec = tween(300)
            )
            else -> null
        }
    }

    override fun AnimatedContentScope<NavBackStackEntry>.exitTransition(): ExitTransition? {
        return when (targetState.destination.route) {
            TvScreenDestination.route,
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
            TvScreenDestination.route,
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
data class BrowseMoviesScreenArgs(
    val movieType: MovieType
) : Parcelable

@OptIn(ExperimentalFoundationApi::class, kotlinx.coroutines.FlowPreview::class)
@Destination(
    navArgsDelegate = BrowseMoviesScreenArgs::class,
    style = BrowseMoviesScreenTransitions::class
)
@Composable
fun AnimatedVisibilityScope.BrowseMoviesScreen(
    viewModel: BrowseMoviesViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val uiState by viewModel.uiState.collectAsState()
    val onBackClicked: () -> Unit = { navigator.navigateUp() }
    val onClearDialogConfirmClicked: () -> Unit = viewModel::onClearClicked
    val onMovieClicked = { movieId: Int ->
        val destination = MovieDetailsScreenDestination(
            movieId = movieId,
            startRoute = MoviesScreenDestination.route
        )

        navigator.navigate(destination)
    }

    BrowseMoviesScreenContent(
        uiState = uiState,
        onBackClicked = onBackClicked,
        onClearDialogConfirmClicked = onClearDialogConfirmClicked,
        onMovieClicked = onMovieClicked
    )
}


@Composable
fun BrowseMoviesScreenContent(
    uiState: BrowseMoviesScreenUiState,
    onBackClicked: () -> Unit,
    onClearDialogConfirmClicked: () -> Unit,
    onMovieClicked: (movieId: Int) -> Unit
) {
    val movies = uiState.movies.collectAsLazyPagingItems()

    val appbarTitle = when (uiState.selectedMovieType) {
        MovieType.Upcoming -> stringResource(R.string.all_movies_upcoming_label)
        MovieType.TopRated -> stringResource(R.string.all_movies_top_rated_label)
        MovieType.Favourite -> stringResource(
            R.string.all_movies_favourites_label,
            uiState.favouriteMoviesCount
        )
        MovieType.RecentlyBrowsed -> stringResource(R.string.all_movies_recently_browsed_label)
        MovieType.Trending -> stringResource(R.string.all_movies_trending_label)
    }

    val showClearButton =
        uiState.selectedMovieType == MovieType.RecentlyBrowsed && movies.itemSnapshotList.isNotEmpty()

    var showClearDialog by remember { mutableStateOf(false) }

    val showDialog = {
        showClearDialog = true
    }

    val dismissDialog = {
        showClearDialog = false
    }

    if (showClearDialog) {
        InfoDialog(
            infoText = stringResource(R.string.clear_recent_movies_dialog_text),
            onDismissRequest = dismissDialog,
            onCancelClick = dismissDialog,
            onConfirmClick = {
                onClearDialogConfirmClicked()
                dismissDialog()
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
        AppBar(
            title = appbarTitle,
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
                AnimatedVisibility(
                    visible = showClearButton,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    IconButton(
                        modifier = Modifier.padding(end = MaterialTheme.spacing.medium),
                        onClick = showDialog
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "clear recent",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
            }
        )

        PresentableGridSection(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                horizontal = MaterialTheme.spacing.small,
                vertical = MaterialTheme.spacing.medium,
            ),
            state = movies,
            onPresentableClick = onMovieClicked
        )
    }
}