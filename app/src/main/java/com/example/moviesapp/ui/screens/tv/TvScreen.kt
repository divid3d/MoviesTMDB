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
import com.example.moviesapp.ui.components.PresentableSection
import com.example.moviesapp.ui.components.PresentableTopSection
import com.example.moviesapp.ui.components.SectionDivider
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

    val topRatedTvSeriesState = viewModel.topRatedTvSeriesPagingDataFlow.collectAsLazyPagingItems()
    val onTheAirTvSeriesState = viewModel.onTheAirTvSeriesPagingDataFlow.collectAsLazyPagingItems()
    val popularTvSeriesState = viewModel.popularTvSeriesPagingDataFlow.collectAsLazyPagingItems()
    val airingTodayTvSeriesState =
        viewModel.airingTodayTvSeriesPagingDataFlow.collectAsLazyPagingItems()


    val isRefreshing by derivedStateOf {
        listOf(
            topRatedTvSeriesState,
            onTheAirTvSeriesState,
            popularTvSeriesState,
            airingTodayTvSeriesState
        ).any { lazyPagingItems -> lazyPagingItems.itemCount > 0 && lazyPagingItems.loadState.refresh is LoadState.Loading }
    }

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

    val refreshAllPagingData = {
        listOf(
            topRatedTvSeriesState,
            onTheAirTvSeriesState,
            popularTvSeriesState,
            airingTodayTvSeriesState
        ).forEach { lazyPagingItems -> lazyPagingItems.refresh() }
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
                state = onTheAirTvSeriesState
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            PresentableSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                title = stringResource(R.string.top_rated_tv_series),
                state = topRatedTvSeriesState
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
                state = airingTodayTvSeriesState
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
                state = popularTvSeriesState
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }
    }
}

