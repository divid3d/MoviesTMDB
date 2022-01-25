package com.example.moviesapp.ui.screens.movies

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.R
import com.example.moviesapp.model.MovieType
import com.example.moviesapp.other.isNotEmpty
import com.example.moviesapp.ui.components.ExitDialog
import com.example.moviesapp.ui.components.PresentableSection
import com.example.moviesapp.ui.components.PresentableTopSection
import com.example.moviesapp.ui.components.SectionDivider
import com.example.moviesapp.ui.screens.destinations.AllMoviesScreenDestination
import com.example.moviesapp.ui.screens.destinations.MovieDetailsScreenDestination
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(start = true)
@Composable
fun MoviesScreen(
    navigator: DestinationsNavigator
) {
    val viewModel: MoviesViewModel = hiltViewModel()
    val context = LocalContext.current

    var showExitDialog by remember { mutableStateOf(false) }

    val dismissDialog = {
        showExitDialog = false
    }

    val discover = viewModel.discover.collectAsLazyPagingItems()
    val upcoming = viewModel.upcoming.collectAsLazyPagingItems()
    val topRated = viewModel.topRated.collectAsLazyPagingItems()
    val trending = viewModel.trending.collectAsLazyPagingItems()
    val nowPlaying = viewModel.nowPlaying.collectAsLazyPagingItems()
    val favourites = viewModel.favourites.collectAsLazyPagingItems()
    val recentlyBrowsed = viewModel.recentBrowsed.collectAsLazyPagingItems()

    BackHandler {
        showExitDialog = true
    }

    if (showExitDialog) {
        ExitDialog(
            onDismissRequest = dismissDialog,
            onCancelClick = dismissDialog,
            onConfirmClick = {
                val activity = (context as? Activity)
                activity?.finish()
            }
        )
    }

    val isRefreshing by derivedStateOf {
        listOf(
            discover,
            upcoming,
            topRated,
            nowPlaying
        ).any { lazyPagingItems -> lazyPagingItems.itemCount > 0 && lazyPagingItems.loadState.refresh is LoadState.Loading }
    }

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

    val refreshAllPagingData = {
        listOf(
            discover,
            upcoming,
            topRated,
            nowPlaying
        ).forEach { lazyPagingItems -> lazyPagingItems.refresh() }
    }

    val navigateToMovieDetails: (Int) -> Unit = { movieId ->
        navigator.navigate(MovieDetailsScreenDestination(movieId))
    }

    val navigateToAllMovies: (MovieType) -> Unit = { type ->
        navigator.navigate(AllMoviesScreenDestination(type))
    }

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = refreshAllPagingData
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            PresentableTopSection(
                modifier = Modifier
                    .fillMaxWidth(),
                title = stringResource(R.string.now_playing_movies),
                state = nowPlaying,
                onPresentableClick = navigateToMovieDetails
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            PresentableSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                title = stringResource(R.string.popular_movies),
                state = discover,
                onPresentableClick = navigateToMovieDetails,
                onMoreClick = { navigateToAllMovies(MovieType.Popular) }
            )
            SectionDivider(
                modifier = Modifier.padding(
                    start = MaterialTheme.spacing.medium,
                    top = MaterialTheme.spacing.medium,
                    end = MaterialTheme.spacing.medium,
                    bottom = MaterialTheme.spacing.small
                )
            )
            PresentableSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                title = stringResource(R.string.upcoming_movies),
                state = upcoming,
                onPresentableClick = navigateToMovieDetails,
                onMoreClick = {
                    navigateToAllMovies(MovieType.Upcoming)
                }
            )
            SectionDivider(
                modifier = Modifier.padding(
                    start = MaterialTheme.spacing.medium,
                    top = MaterialTheme.spacing.medium,
                    end = MaterialTheme.spacing.medium,
                    bottom = MaterialTheme.spacing.small
                )
            )
            PresentableSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                title = "Zyskujące na popularności",
                state = trending,
                onPresentableClick = navigateToMovieDetails,
                onMoreClick = {
                    //navigateToAllMovies(MovieType.Upcoming)
                }
            )
            SectionDivider(
                modifier = Modifier.padding(
                    start = MaterialTheme.spacing.medium,
                    top = MaterialTheme.spacing.medium,
                    end = MaterialTheme.spacing.medium,
                    bottom = MaterialTheme.spacing.small
                )
            )
            PresentableSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                title = stringResource(R.string.top_rated_movies),
                state = topRated,
                onPresentableClick = navigateToMovieDetails,
                onMoreClick = {
                    navigateToAllMovies(MovieType.TopRated)
                }
            )
            if (favourites.isNotEmpty()) {
                SectionDivider(
                    modifier = Modifier.padding(
                        start = MaterialTheme.spacing.medium,
                        top = MaterialTheme.spacing.medium,
                        end = MaterialTheme.spacing.medium,
                        bottom = MaterialTheme.spacing.small
                    )
                )
                PresentableSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                    title = stringResource(R.string.favourite_movies),
                    state = favourites,
                    onPresentableClick = navigateToMovieDetails,
                    onMoreClick = {
                        navigateToAllMovies(MovieType.Favourite)
                    }
                )
            }
            if (recentlyBrowsed.isNotEmpty()) {
                SectionDivider(
                    modifier = Modifier.padding(
                        start = MaterialTheme.spacing.medium,
                        top = MaterialTheme.spacing.medium,
                        end = MaterialTheme.spacing.medium,
                        bottom = MaterialTheme.spacing.small
                    )
                )
                PresentableSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                    title = stringResource(R.string.recently_browsed_movies),
                    state = recentlyBrowsed,
                    onPresentableClick = navigateToMovieDetails,
                    onMoreClick = {
                        navigateToAllMovies(MovieType.RecentlyBrowsed)
                    }
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }
    }
}