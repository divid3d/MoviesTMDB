package com.example.moviesapp.model

data class MovieFilterState(
    val selectedGenres: List<Genre> = emptyList(),
    val availableGenres: List<Genre> = emptyList(),
    val selectedWatchProviders: List<ProviderSource> = emptyList(),
    val availableWatchProviders: List<ProviderSource> = emptyList(),
    val showOnlyWithPoster: Boolean = false,
    val showOnlyWithScore: Boolean = false,
    val showOnlyWithOverview: Boolean = false,
    val voteRange: VoteRange = VoteRange(),
    val releaseDateRange: DateRange = DateRange()
) {
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