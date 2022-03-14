package com.example.moviesapp.ui.screens.tv

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.example.moviesapp.model.RecentlyBrowsedTvSeries
import com.example.moviesapp.model.TvSeries
import com.example.moviesapp.model.TvSeriesFavourite
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Stable
data class TvScreenUiState(
    val tvSeriesState: TvSeriesState,
    val favourites: Flow<PagingData<TvSeriesFavourite>>,
    val recentlyBrowsed: Flow<PagingData<RecentlyBrowsedTvSeries>>
) {
    companion object {
        val default: TvScreenUiState
            get() = TvScreenUiState(
                tvSeriesState = TvSeriesState.default,
                favourites = emptyFlow(),
                recentlyBrowsed = emptyFlow()
            )
    }
}

data class TvSeriesState(
    val onTheAir: Flow<PagingData<TvSeries>>,
    val discover: Flow<PagingData<TvSeries>>,
    val topRated: Flow<PagingData<TvSeries>>,
    val trending: Flow<PagingData<TvSeries>>,
    val airingToday: Flow<PagingData<TvSeries>>
) {
    companion object {
        val default: TvSeriesState
            get() = TvSeriesState(
                onTheAir = emptyFlow(),
                discover = emptyFlow(),
                topRated = emptyFlow(),
                trending = emptyFlow(),
                airingToday = emptyFlow()
            )
    }
}