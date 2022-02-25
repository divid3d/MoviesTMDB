@file:OptIn(ExperimentalAnimationApi::class)

package com.example.moviesapp.ui.screens.movies

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
import com.example.moviesapp.ui.screens.destinations.MoviesScreenDestination
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination(start = true)
@Composable
fun AnimatedVisibilityScope.MoviesScreen(
    viewModel: MoviesViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val uiState by viewModel.uiState.collectAsState()

    val onMovieClicked = { movieId: Int ->
        val destination = MovieDetailsScreenDestination(
            movieId = movieId,
            startRoute = MoviesScreenDestination.route
        )

        navigator.navigate(destination)
    }

    val onBrowseMoviesClicked = { type: MovieType ->
        navigator.navigate(BrowseMoviesScreenDestination(type))
    }

    val onDiscoverMoviesClicked = {
        navigator.navigate(DiscoverMoviesScreenDestination)
    }

    MoviesScreenContent(
        uiState = uiState,
        onMovieClicked = onMovieClicked,
        onBrowseMoviesClicked = onBrowseMoviesClicked,
        onDiscoverMoviesClicked = onDiscoverMoviesClicked
    )
}

@Composable
fun MoviesScreenContent(
    uiState: MovieScreenUiState,
    onMovieClicked: (movieId: Int) -> Unit,
    onBrowseMoviesClicked: (type: MovieType) -> Unit,
    onDiscoverMoviesClicked: () -> Unit
) {
    val context = LocalContext.current
    val density = LocalDensity.current

    val discoverLazyItems = uiState.moviesState.discover.collectAsLazyPagingItems()
    val upcomingLazyItems = uiState.moviesState.upcoming.collectAsLazyPagingItems()
    val topRatedLazyItems = uiState.moviesState.topRated.collectAsLazyPagingItems()
    val trendingLazyItems = uiState.moviesState.trending.collectAsLazyPagingItems()
    val nowPlayingLazyItems = uiState.moviesState.trending.collectAsLazyPagingItems()
    val favouritesLazyItems = uiState.favourites.collectAsLazyPagingItems()
    val recentlyBrowsedLazyItems = uiState.recentlyBrowsed.collectAsLazyPagingItems()

    val scrollState = rememberScrollState()
    var topSectionHeight: Float? by remember { mutableStateOf(null) }
    val appbarHeight = density.run { 56.dp.toPx() }
    val topSectionScrollLimitValue: Float? = topSectionHeight?.minus(appbarHeight)

    var showExitDialog by remember { mutableStateOf(false) }

    val dismissDialog = {
        showExitDialog = false
    }

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
            discoverLazyItems,
            upcomingLazyItems,
            topRatedLazyItems,
            trendingLazyItems,
            nowPlayingLazyItems
        ).any { lazyPagingItems ->
            lazyPagingItems.itemCount > 0 && lazyPagingItems.loadState.refresh is LoadState.Loading
        }
    }

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

    val refreshAllPagingData = {
        listOf(
            discoverLazyItems,
            upcomingLazyItems,
            topRatedLazyItems,
            trendingLazyItems,
            nowPlayingLazyItems
        ).forEach { lazyPagingItems -> lazyPagingItems.refresh() }
    }

    LaunchedEffect(isRefreshing) {
        swipeRefreshState.isRefreshing = isRefreshing
    }

    SwipeRefresh(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background),
        state = swipeRefreshState,
        indicator = { state, trigger ->
            SwipeRefreshIndicator(
                modifier = Modifier.statusBarsPadding(),
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
                .verticalScroll(scrollState),
        ) {
            PresentableTopSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        topSectionHeight = coordinates.size.height.toFloat()
                    },
                title = stringResource(R.string.now_playing_movies),
                state = nowPlayingLazyItems,
                scrollState = scrollState,
                scrollValueLimit = topSectionScrollLimitValue,
                onPresentableClick = onMovieClicked
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            PresentableSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                title = stringResource(R.string.explore_movies),
                state = discoverLazyItems,
                onPresentableClick = onMovieClicked,
                onMoreClick = onDiscoverMoviesClicked
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
                state = upcomingLazyItems,
                onPresentableClick = onMovieClicked,
                onMoreClick = { onBrowseMoviesClicked(MovieType.Upcoming) }
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
                state = trendingLazyItems,
                onPresentableClick = onMovieClicked,
                onMoreClick = { onBrowseMoviesClicked(MovieType.Trending) }
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
                state = topRatedLazyItems,
                onPresentableClick = onMovieClicked,
                onMoreClick = { onBrowseMoviesClicked(MovieType.TopRated) }
            )
            if (favouritesLazyItems.isNotEmpty()) {
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
                    state = favouritesLazyItems,
                    onPresentableClick = onMovieClicked,
                    onMoreClick = { onBrowseMoviesClicked(MovieType.Favourite) }
                )
            }
            if (recentlyBrowsedLazyItems.isNotEmpty()) {
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
                    state = recentlyBrowsedLazyItems,
                    onPresentableClick = onMovieClicked,
                    onMoreClick = { onBrowseMoviesClicked(MovieType.RecentlyBrowsed) }
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }
    }
}