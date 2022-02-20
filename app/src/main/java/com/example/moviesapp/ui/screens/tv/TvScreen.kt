package com.example.moviesapp.ui.screens.tv

import androidx.compose.animation.animateContentSize
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
    val density = LocalDensity.current

    val topRated = viewModel.topRated.collectAsLazyPagingItems()
    val discover = viewModel.discover.collectAsLazyPagingItems()
    val onTheAir = viewModel.onTheAir.collectAsLazyPagingItems()
    val trending = viewModel.trending.collectAsLazyPagingItems()
    val airingToday = viewModel.airingToday.collectAsLazyPagingItems()
    val favourites = viewModel.favourites.collectAsLazyPagingItems()
    val recentlyBrowsed = viewModel.recentlyBrowsed.collectAsLazyPagingItems()

    val scrollState = rememberScrollState()
    var topSectionHeight: Float? by remember { mutableStateOf(null) }
    val appbarHeight = density.run { 56.dp.toPx() }
    val topSectionScrollLimitValue: Float? = topSectionHeight?.minus(appbarHeight)

    val isRefreshing by derivedStateOf {
        listOf(
            discover,
            topRated,
            onTheAir,
            trending,
            airingToday
        ).any { lazyPagingItems ->
            lazyPagingItems.itemCount > 0 && lazyPagingItems.loadState.refresh is LoadState.Loading
        }
    }

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

    val refreshAllPagingData = {
        listOf(
            discover,
            topRated,
            onTheAir,
            trending,
            airingToday
        ).forEach { lazyPagingItems -> lazyPagingItems.refresh() }
    }

    val navigateToTvSeriesDetails: (Int) -> Unit = { tvSeriesId ->
        navigator.navigate(TvSeriesDetailsScreenDestination(tvSeriesId))
    }

    val navigateToBrowseTvSeries: (TvSeriesType) -> Unit = { type ->
        navigator.navigate(BrowseTvSeriesScreenDestination(type))
    }

    val navigateToDiscoverTvSeries = {
        navigator.navigate(DiscoverTvSeriesScreenDestination)
    }

    LaunchedEffect(isRefreshing) {
        swipeRefreshState.isRefreshing = isRefreshing
    }

    SwipeRefresh(
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
                state = onTheAir,
                scrollState = scrollState,
                scrollValueLimit = topSectionScrollLimitValue,
                onPresentableClick = navigateToTvSeriesDetails
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            PresentableSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                title = stringResource(R.string.explore_tv_series),
                state = discover,
                onPresentableClick = navigateToTvSeriesDetails,
                onMoreClick = navigateToDiscoverTvSeries
            )
            SectionDivider(
                modifier = Modifier.padding(
                    top = MaterialTheme.spacing.medium,
                    bottom = MaterialTheme.spacing.small
                )
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            PresentableSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                title = stringResource(R.string.top_rated_tv_series),
                state = topRated,
                onPresentableClick = navigateToTvSeriesDetails,
                onMoreClick = { navigateToBrowseTvSeries(TvSeriesType.TopRated) }
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
                state = trending,
                onPresentableClick = navigateToTvSeriesDetails,
                onMoreClick = { navigateToBrowseTvSeries(TvSeriesType.Trending) }
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
                state = airingToday,
                onPresentableClick = navigateToTvSeriesDetails,
                onMoreClick = { navigateToBrowseTvSeries(TvSeriesType.AiringToday) }
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
                    title = stringResource(R.string.favourites_tv_series),
                    state = favourites,
                    onPresentableClick = navigateToTvSeriesDetails,
                    onMoreClick = { navigateToBrowseTvSeries(TvSeriesType.Favourite) }
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
                    title = stringResource(R.string.recently_browsed_tv_series),
                    state = recentlyBrowsed,
                    onPresentableClick = navigateToTvSeriesDetails,
                    onMoreClick = { navigateToBrowseTvSeries(TvSeriesType.RecentlyBrowsed) }
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }
    }
}



