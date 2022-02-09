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
import com.example.moviesapp.ui.components.PresentableSection
import com.example.moviesapp.ui.components.PresentableTopSection
import com.example.moviesapp.ui.components.SectionDivider
import com.example.moviesapp.ui.components.dialogs.ExitDialog
import com.example.moviesapp.ui.screens.destinations.BrowseMoviesScreenDestination
import com.example.moviesapp.ui.screens.destinations.DiscoverMoviesScreenDestination
import com.example.moviesapp.ui.screens.destinations.MovieDetailsScreenDestination
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(start = true)
@Composable
fun MoviesScreen(
    viewModel: MoviesViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
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
            trending,
            nowPlaying
        ).any { lazyPagingItems -> lazyPagingItems.itemCount > 0 && lazyPagingItems.loadState.refresh is LoadState.Loading }
    }

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

    val refreshAllPagingData = {
        listOf(
            discover,
            upcoming,
            topRated,
            trending,
            nowPlaying
        ).forEach { lazyPagingItems -> lazyPagingItems.refresh() }
    }

    val navigateToMovieDetails: (Int) -> Unit = { movieId ->
        navigator.navigate(MovieDetailsScreenDestination(movieId))
    }

    val navigateToBrowseMovies: (MovieType) -> Unit = { type ->
        navigator.navigate(BrowseMoviesScreenDestination(type))
    }

    val navigateToDiscoverMovies = {
        navigator.navigate(DiscoverMoviesScreenDestination)
    }

    SwipeRefresh(
        state = swipeRefreshState,
        indicator = { state, trigger ->
            SwipeRefreshIndicator(
                state = state,
                refreshTriggerDistance = trigger,
                fade = true,
                scale = true,
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.primary
            )
        },
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
                title = stringResource(R.string.explore_movies),
                state = discover,
                onPresentableClick = navigateToMovieDetails,
                onMoreClick = navigateToDiscoverMovies
            )
            SectionDivider(
                modifier = Modifier.padding(
                    top = MaterialTheme.spacing.medium,
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
                    navigateToBrowseMovies(MovieType.Upcoming)
                }
            )
            SectionDivider(
                modifier = Modifier.padding(
                    top = MaterialTheme.spacing.medium,
                    bottom = MaterialTheme.spacing.small
                )
            )
            PresentableSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                title = stringResource(R.string.trending_movies),
                state = trending,
                onPresentableClick = navigateToMovieDetails,
                onMoreClick = {
                    navigateToBrowseMovies(MovieType.Trending)
                }
            )
            SectionDivider(
                modifier = Modifier.padding(
                    top = MaterialTheme.spacing.medium,
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
                    navigateToBrowseMovies(MovieType.TopRated)
                }
            )
            if (favourites.isNotEmpty()) {
                SectionDivider(
                    modifier = Modifier.padding(
                        top = MaterialTheme.spacing.medium,
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
                        navigateToBrowseMovies(MovieType.Favourite)
                    }
                )
            }
            if (recentlyBrowsed.isNotEmpty()) {
                SectionDivider(
                    modifier = Modifier.padding(
                        top = MaterialTheme.spacing.medium,
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
                        navigateToBrowseMovies(MovieType.RecentlyBrowsed)
                    }
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }
    }
}