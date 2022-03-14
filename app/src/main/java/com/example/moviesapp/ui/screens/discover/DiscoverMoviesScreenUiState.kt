package com.example.moviesapp.ui.screens.discover

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.example.moviesapp.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Stable
data class DiscoverMoviesScreenUiState(
    val sortInfo: SortInfo,
    val filterState: MovieFilterState,
    val movies: Flow<PagingData<Movie>>
) {
    companion object {
        val default: DiscoverMoviesScreenUiState
            get() = DiscoverMoviesScreenUiState(
                sortInfo = SortInfo.default,
                filterState = MovieFilterState.default,
                movies = emptyFlow()
            )
    }
}

@Stable
data class SortInfo(
    val sortType: SortType,
    val sortOrder: SortOrder
) {
    companion object {
        val default: SortInfo
            get() = SortInfo(
                sortType = SortType.Popularity,
                sortOrder = SortOrder.Desc
            )
    }
}

@Stable
data class MovieFilterState(
    val selectedGenres: List<Genre>,
    val availableGenres: List<Genre>,
    val selectedWatchProviders: List<ProviderSource>,
    val availableWatchProviders: List<ProviderSource>,
    val showOnlyWithPoster: Boolean,
    val showOnlyWithScore: Boolean,
    val showOnlyWithOverview: Boolean,
    val voteRange: VoteRange,
    val releaseDateRange: DateRange
) {
    companion object {
        val default: MovieFilterState
            get() = MovieFilterState(
                selectedGenres = emptyList(),
                availableGenres = emptyList(),
                availableWatchProviders = emptyList(),
                selectedWatchProviders = emptyList(),
                showOnlyWithPoster = false,
                showOnlyWithScore = false,
                showOnlyWithOverview = false,
                voteRange = VoteRange(),
                releaseDateRange = DateRange()
            )
    }

    fun clear(): MovieFilterState = copy(
        selectedGenres = emptyList(),
        selectedWatchProviders = emptyList(),
        showOnlyWithPoster = false,
        showOnlyWithScore = false,
        showOnlyWithOverview = false,
        voteRange = VoteRange(),
        releaseDateRange = DateRange()
    )
}