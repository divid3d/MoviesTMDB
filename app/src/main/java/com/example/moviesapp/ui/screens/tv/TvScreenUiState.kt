package com.example.moviesapp.ui.screens.tv

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.example.moviesapp.model.DetailPresentable
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.model.RecentlyBrowsedTvSeries
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
        val default: TvScreenUiState = TvScreenUiState(
            tvSeriesState = TvSeriesState.default,
            favourites = emptyFlow(),
            recentlyBrowsed = emptyFlow()
        )
    }
}

data class TvSeriesState(
    val onTheAir: Flow<PagingData<DetailPresentable>>,
    val discover: Flow<PagingData<Presentable>>,
    val topRated: Flow<PagingData<Presentable>>,
    val trending: Flow<PagingData<Presentable>>,
    val airingToday: Flow<PagingData<Presentable>>
) {
    companion object {
        val default: TvSeriesState = TvSeriesState(
            onTheAir = emptyFlow(),
            discover = emptyFlow(),
            topRated = emptyFlow(),
            trending = emptyFlow(),
            airingToday = emptyFlow()
        )
    }
}