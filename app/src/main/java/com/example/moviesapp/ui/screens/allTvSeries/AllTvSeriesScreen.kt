package com.example.moviesapp.ui.screens.allTvSeries

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.R
import com.example.moviesapp.model.TvSeriesType
import com.example.moviesapp.ui.components.AppBar
import com.example.moviesapp.ui.components.PresentableGridSection
import com.example.moviesapp.ui.screens.destinations.TvSeriesDetailsScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalFoundationApi::class, kotlinx.coroutines.FlowPreview::class)
@Destination
@Composable
fun AllTvSeriesScreen(
    tvSeriesType: TvSeriesType,
    navigator: DestinationsNavigator
) {
    val viewModel: AllTvSeriesViewModel = hiltViewModel()
    val tvSeries = viewModel.tvSeries.collectAsLazyPagingItems()

    val favouriteTvSeriesCount by viewModel.favouriteTvSeriesCount.collectAsState()

    val appbarTitle = when (tvSeriesType) {
        TvSeriesType.TopRated -> stringResource(R.string.all_tv_series_top_rated_label)
        TvSeriesType.AiringToday -> stringResource(R.string.all_tv_series_airing_today_label)
        TvSeriesType.Popular -> stringResource(R.string.all_tv_series_popular_label)
        TvSeriesType.Favourite -> stringResource(
            R.string.all_tv_series_favourites_label,
            favouriteTvSeriesCount
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AppBar(title = appbarTitle, action = {
            IconButton(onClick = { navigator.navigateUp() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        })
        PresentableGridSection(
            modifier = Modifier.fillMaxSize(),
            state = tvSeries
        ) { tvSeriesId ->
            navigator.navigate(
                TvSeriesDetailsScreenDestination(tvSeriesId)
            )
        }
    }
}