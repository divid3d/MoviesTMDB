package com.example.moviesapp.model

import java.util.*

data class FilterState(
    val selectedGenres: List<Genre> = emptyList(),
    val availableGenres: List<Genre> = emptyList(),
    val selectedWatchProviders: List<ProviderSource> = emptyList(),
    val availableWatchProviders: List<ProviderSource> = emptyList(),
    val showOnlyWithPoster: Boolean = false,
    val showOnlyWithScore: Boolean = false,
    val showOnlyWithOverview: Boolean = false,
    val voteRange: VoteRange = VoteRange(),
    val releaseDateRange: ReleaseDateRange = ReleaseDateRange()
) {
    fun clear(): FilterState = copy(
        selectedGenres = emptyList(),
        selectedWatchProviders = emptyList(),
        showOnlyWithPoster = false,
        showOnlyWithScore = false,
        showOnlyWithOverview = false,
        voteRange = VoteRange(),
        releaseDateRange = ReleaseDateRange()
    )
}


data class VoteRange(
    val default: ClosedFloatingPointRange<Float> = 0f..10f,
    val current: ClosedFloatingPointRange<Float> = default
)

data class ReleaseDateRange(
    val from: Date? = null,
    val to: Date? = null
)