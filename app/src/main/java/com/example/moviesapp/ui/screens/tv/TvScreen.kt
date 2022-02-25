package com.example.moviesapp.ui.screens.tv

import androidx.compose.animation.animateContentSize
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
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.R
import com.example.moviesapp.model.TvSeriesType
import com.example.moviesapp.other.isNotEmpty
import com.example.moviesapp.ui.components.PresentableSection
import com.example.moviesapp.ui.components.PresentableTopSection
import com.example.moviesapp.ui.components.SectionDivider
import com.example.moviesapp.ui.screens.destinations.BrowseTvSeriesScreenDestination
import com.example.moviesapp.ui.screens.destinations.DiscoverTvSeriesScreenDestination
import com.example.moviesapp.ui.screens.destinations.TvScreenDestination
import com.example.moviesapp.ui.screens.destinations.TvSeriesDetailsScreenDestination
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun TvScreen(
    viewModel: TvSeriesViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val uiState by viewModel.tvScreenUiState.collectAsState()

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
        onTvSeriesClicked = onTvSeriesClicked,
        onBrowseTvSeriesClicked = onBrowseTvSeriesClicked,
        onDiscoverTvSeriesClicked = onDiscoverTvSeriesClicked
    )
}

@Composable
fun TvScreenContent(
    uiState: TvScreenUiState,
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

    val scrollState = rememberScrollState()
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
        ).any { lazyPagingItems ->
            lazyPagingItems.itemCount > 0 && lazyPagingItems.loadState.refresh is LoadState.Loading
        }
    }

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

    val refreshAllPagingData = {
        listOf(
            topRatedLazyItems,
            discoverLazyItems,
            onTheAirLazyItems,
            trendingLazyItems,
            airingTodayLazyItems
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
                title = stringResource(R.string.now_airing_tv_series),
                state = onTheAirLazyItems,
                scrollState = scrollState,
                scrollValueLimit = topSectionScrollLimitValue,
                onPresentableClick = onTvSeriesClicked
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            PresentableSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                title = stringResource(R.string.explore_tv_series),
                state = discoverLazyItems,
                onPresentableClick = onTvSeriesClicked,
                onMoreClick = onDiscoverTvSeriesClicked
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
                title = stringResource(R.string.top_rated_tv_series),
                state = topRatedLazyItems,
                onPresentableClick = onTvSeriesClicked,
                onMoreClick = { onBrowseTvSeriesClicked(TvSeriesType.TopRated) }
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
                title = stringResource(R.string.trending_tv_series),
                state = trendingLazyItems,
                onPresentableClick = onTvSeriesClicked,
                onMoreClick = { onBrowseTvSeriesClicked(TvSeriesType.Trending) }
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
                title = stringResource(R.string.today_airing_tv_series),
                state = airingTodayLazyItems,
                onPresentableClick = onTvSeriesClicked,
                onMoreClick = { onBrowseTvSeriesClicked(TvSeriesType.AiringToday) }
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
                    title = stringResource(R.string.favourites_tv_series),
                    state = favouritesLazyItems,
                    onPresentableClick = onTvSeriesClicked,
                    onMoreClick = { onBrowseTvSeriesClicked(TvSeriesType.Favourite) }
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



