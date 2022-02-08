package com.example.moviesapp.model

data class FilterState(
    val selectedGenres: List<Genre> = emptyList(),
    val availableGenres: List<Genre> = emptyList(),
    val showOnlyWithPoster: Boolean = false,
    val voteRange: VoteRange = VoteRange()
) {
    fun clear(): FilterState = copy(
        selectedGenres = emptyList(),
        showOnlyWithPoster = false,
        voteRange = VoteRange()
    )
}


data class VoteRange(
    val default: ClosedFloatingPointRange<Float> = 0f..10f,
    val current: ClosedFloatingPointRange<Float> = default
)