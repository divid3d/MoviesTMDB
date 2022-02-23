package com.example.moviesapp.ui.screens.discover

import androidx.paging.PagingData
import com.example.moviesapp.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class DiscoverTvSeriesScreenUiState(
    val sortInfo: SortInfo,
    val filterState: TvSeriesFilterState,
    val tvSeries: Flow<PagingData<TvSeries>>
) {
    companion object {
        val default: DiscoverTvSeriesScreenUiState
            get() = DiscoverTvSeriesScreenUiState(
                sortInfo = SortInfo.default,
                filterState = TvSeriesFilterState.default,
                tvSeries = emptyFlow()
            )
    }
}

data class TvSeriesFilterState(
    val selectedGenres: List<Genre>,
    val availableGenres: List<Genre> = emptyList(),
    val selectedWatchProviders: List<ProviderSource>,
    val availableWatchProviders: List<ProviderSource>,
    val showOnlyWithPoster: Boolean,
    val showOnlyWithScore: Boolean,
    val showOnlyWithOverview: Boolean,
    val voteRange: VoteRange = VoteRange(),
    val airDateRange: DateRange = DateRange()
) {
    companion object {
        val default: TvSeriesFilterState
            get() = TvSeriesFilterState(
                selectedGenres = emptyList(),
                availableGenres = emptyList(),
                selectedWatchProviders = emptyList(),
                availableWatchProviders = emptyList(),
                showOnlyWithPoster = false,
                showOnlyWithScore = false,
                showOnlyWithOverview = false,
                voteRange = VoteRange(),
                airDateRange = DateRange()
            )
    }

    fun clear(): TvSeriesFilterState = copy(
        selectedGenres = emptyList(),
        selectedWatchProviders = emptyList(),
        showOnlyWithPoster = false,
        showOnlyWithScore = false,
        showOnlyWithOverview = false,
        voteRange = VoteRange(),
        airDateRange = DateRange()
    )
}