package com.example.moviesapp.model

data class TvSeriesFilterState(
    val selectedGenres: List<Genre> = emptyList(),
    val availableGenres: List<Genre> = emptyList(),
    val selectedWatchProviders: List<ProviderSource> = emptyList(),
    val availableWatchProviders: List<ProviderSource> = emptyList(),
    val showOnlyWithPoster: Boolean = false,
    val showOnlyWithScore: Boolean = false,
    val showOnlyWithOverview: Boolean = false,
    val voteRange: VoteRange = VoteRange(),
    val airDateRange: DateRange = DateRange()
) {
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