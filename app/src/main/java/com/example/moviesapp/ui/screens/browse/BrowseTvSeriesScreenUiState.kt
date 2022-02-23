package com.example.moviesapp.ui.screens.browse

import androidx.paging.PagingData
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.model.TvSeriesType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class BrowseTvSeriesScreenUiState(
    val selectedTvSeriesType: TvSeriesType,
    val tvSeries: Flow<PagingData<Presentable>>,
    val favouriteMoviesCount: Int
) {
    companion object {
        fun getDefault(selectedTvSeriesType: TvSeriesType): BrowseTvSeriesScreenUiState {
            return BrowseTvSeriesScreenUiState(
                selectedTvSeriesType = selectedTvSeriesType,
                tvSeries = emptyFlow(),
                favouriteMoviesCount = 0
            )
        }
    }
}