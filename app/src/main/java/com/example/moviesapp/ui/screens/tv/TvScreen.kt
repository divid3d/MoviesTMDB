package com.example.moviesapp.ui.screens.tv

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.R
import com.example.moviesapp.model.TvSeriesType
import com.example.moviesapp.other.isNotEmpty
import com.example.moviesapp.ui.components.PresentableSection
import com.example.moviesapp.ui.components.PresentableTopSection
import com.example.moviesapp.ui.components.SectionDivider
import com.example.moviesapp.ui.screens.destinations.AllTvSeriesScreenDestination
import com.example.moviesapp.ui.screens.destinations.TvSeriesDetailsScreenDestination
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun TvScreen(
    navigator: DestinationsNavigator
) {
    val viewModel: TvSeriesViewModel = hiltViewModel()

    val topRated = viewModel.topRated.collectAsLazyPagingItems()
    val onTheAir = viewModel.onTheAir.collectAsLazyPagingItems()
    val popular = viewModel.popular.collectAsLazyPagingItems()
    val airingToday = viewModel.airingToday.collectAsLazyPagingItems()
    val favourites = viewModel.favourites.collectAsLazyPagingItems()
    val recentlyBrowsed = viewModel.recentlyBrowsed.collectAsLazyPagingItems()

    val isRefreshing by derivedStateOf {
        listOf(
            topRated,
            onTheAir,
            popular,
            airingToday
        ).any { lazyPagingItems -> lazyPagingItems.itemCount > 0 && lazyPagingItems.loadState.refresh is LoadState.Loading }
    }

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

    val refreshAllPagingData = {
        listOf(
            topRated,
            onTheAir,
            popular,
            airingToday
        ).forEach { lazyPagingItems -> lazyPagingItems.refresh() }
    }

    val navigateToTvSeriesDetails: (Int) -> Unit = { tvSeriesId ->
        navigator.navigate(TvSeriesDetailsScreenDestination(tvSeriesId))
    }

    val navigateToAllTvSeries: (TvSeriesType) -> Unit = { type ->
        navigator.navigate(AllTvSeriesScreenDestination(type))
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
                title = stringResource(R.string.now_airing_tv_series),
                state = onTheAir,
                onPresentableClick = navigateToTvSeriesDetails
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            PresentableSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                title = stringResource(R.string.top_rated_tv_series),
                state = topRated,
                onPresentableClick = navigateToTvSeriesDetails,
                onMoreClick = { navigateToAllTvSeries(TvSeriesType.TopRated) }
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
                title = stringResource(R.string.today_airing_tv_series),
                state = airingToday,
                onPresentableClick = navigateToTvSeriesDetails,
                onMoreClick = { navigateToAllTvSeries(TvSeriesType.AiringToday) }
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
                title = stringResource(R.string.popular_tv_series),
                state = popular,
                onPresentableClick = navigateToTvSeriesDetails,
                onMoreClick = { navigateToAllTvSeries(TvSeriesType.Popular) }
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
                    title = stringResource(R.string.favourites_tv_series),
                    state = favourites,
                    onPresentableClick = navigateToTvSeriesDetails,
                    onMoreClick = { navigateToAllTvSeries(TvSeriesType.Favourite) }
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
                    title = stringResource(R.string.recently_browsed_tv_series),
                    state = recentlyBrowsed,
                    onPresentableClick = navigateToTvSeriesDetails,
                    onMoreClick = { navigateToAllTvSeries(TvSeriesType.RecentlyBrowsed) }
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }
    }
}

