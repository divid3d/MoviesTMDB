package com.example.moviesapp.ui.screens.tv

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.MainViewModel
import com.example.moviesapp.R
import com.example.moviesapp.model.TvSeriesType
import com.example.moviesapp.other.isAnyRefreshing
import com.example.moviesapp.other.isNotEmpty
import com.example.moviesapp.other.refreshAll
import com.example.moviesapp.ui.components.sections.PresentableSection
import com.example.moviesapp.ui.components.sections.PresentableTopSection
import com.example.moviesapp.ui.screens.destinations.BrowseTvSeriesScreenDestination
import com.example.moviesapp.ui.screens.destinations.DiscoverTvSeriesScreenDestination
import com.example.moviesapp.ui.screens.destinations.TvScreenDestination
import com.example.moviesapp.ui.screens.destinations.TvSeriesDetailsScreenDestination
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@Destination
@Composable
fun AnimatedVisibilityScope.TvScreen(
    mainViewModel: MainViewModel,
    viewModel: TvSeriesViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        mainViewModel.sameBottomBarRoute.collectLatest { sameRoute ->
            if (sameRoute == TvScreenDestination.route) {
                scrollState.animateScrollTo(0)
            }
        }
    }

    val onTvSeriesClicked: (Int) -> Unit = { tvSeriesId ->
        val destination = TvSeriesDetailsScreenDestination(
            tvSeriesId = tvSeriesId,
            startRoute = TvScreenDestination.route
        )

        navigator.navigate(destination)
    }

    val onBrowseTvSeriesClicked: (TvSeriesType) -> Unit = { type ->
        navigator.navigate(BrowseTvSeriesScreenDestination(type))
    }

    val onDiscoverTvSeriesClicked = {
        navigator.navigate(DiscoverTvSeriesScreenDestination)
    }

    TvScreenContent(
        uiState = uiState,
        scrollState = scrollState,
        onTvSeriesClicked = onTvSeriesClicked,
        onBrowseTvSeriesClicked = onBrowseTvSeriesClicked,
        onDiscoverTvSeriesClicked = onDiscoverTvSeriesClicked
    )
}

@Composable
fun TvScreenContent(
    uiState: TvScreenUiState,
    scrollState: ScrollState,
    onTvSeriesClicked: (tvSeriesId: Int) -> Unit,
    onBrowseTvSeriesClicked: (type: TvSeriesType) -> Unit,
    onDiscoverTvSeriesClicked: () -> Unit
) {
    val density = LocalDensity.current

    val topRatedLazyItems = uiState.tvSeriesState.topRated.collectAsLazyPagingItems()
    val discoverLazyItems = uiState.tvSeriesState.discover.collectAsLazyPagingItems()
    val onTheAirLazyItems = uiState.tvSeriesState.onTheAir.collectAsLazyPagingItems()
    val trendingLazyItems = uiState.tvSeriesState.trending.collectAsLazyPagingItems()
    val airingTodayLazyItems = uiState.tvSeriesState.airingToday.collectAsLazyPagingItems()
    val favouritesLazyItems = uiState.favourites.collectAsLazyPagingItems()
    val recentlyBrowsedLazyItems = uiState.recentlyBrowsed.collectAsLazyPagingItems()

    var topSectionHeight: Float? by remember { mutableStateOf(null) }
    val appbarHeight = density.run { 56.dp.toPx() }
    val topSectionScrollLimitValue: Float? = topSectionHeight?.minus(appbarHeight)

    val isRefreshing by derivedStateOf {
        listOf(
            topRatedLazyItems,
            discoverLazyItems,
            onTheAirLazyItems,
            trendingLazyItems,
            airingTodayLazyItems
        ).isAnyRefreshing()
    }

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

    val refreshAllPagingData = {
        listOf(
            topRatedLazyItems,
            discoverLazyItems,
            onTheAirLazyItems,
            trendingLazyItems,
            airingTodayLazyItems
        ).refreshAll()
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
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(top = MaterialTheme.spacing.large),
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
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            PresentableTopSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        topSectionHeight = coordinates.size.height.toFloat()
                    },
                title = stringResource(R.string.now_airing_tv_series),
                state = onTheAirLazyItems,
                scrollState = scrollState,
                scrollValueLimit = topSectionScrollLimitValue,
                onPresentableClick = onTvSeriesClicked,
                onMoreClick = {
                    onBrowseTvSeriesClicked(TvSeriesType.OnTheAir)
                }
            )

            PresentableSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                title = stringResource(R.string.explore_tv_series),
                state = discoverLazyItems,
                onPresentableClick = onTvSeriesClicked,
                onMoreClick = onDiscoverTvSeriesClicked
            )

            PresentableSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                title = stringResource(R.string.top_rated_tv_series),
                state = topRatedLazyItems,
                onPresentableClick = onTvSeriesClicked,
                onMoreClick = { onBrowseTvSeriesClicked(TvSeriesType.TopRated) }
            )

            PresentableSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                title = stringResource(R.string.trending_tv_series),
                state = trendingLazyItems,
                onPresentableClick = onTvSeriesClicked,
                onMoreClick = { onBrowseTvSeriesClicked(TvSeriesType.Trending) }
            )

            PresentableSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                title = stringResource(R.string.today_airing_tv_series),
                state = airingTodayLazyItems,
                onPresentableClick = onTvSeriesClicked,
                onMoreClick = { onBrowseTvSeriesClicked(TvSeriesType.AiringToday) }
            )

            if (favouritesLazyItems.isNotEmpty()) {
                PresentableSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                    title = stringResource(R.string.favourites_tv_series),
                    state = favouritesLazyItems,
                    onPresentableClick = onTvSeriesClicked,
                    onMoreClick = { onBrowseTvSeriesClicked(TvSeriesType.Favourite) }
                )
            }

            if (recentlyBrowsedLazyItems.isNotEmpty()) {
                PresentableSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                    title = stringResource(R.string.recently_browsed_tv_series),
                    state = recentlyBrowsedLazyItems,
                    onPresentableClick = onTvSeriesClicked,
                    onMoreClick = { onBrowseTvSeriesClicked(TvSeriesType.RecentlyBrowsed) }
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }
    }
}



