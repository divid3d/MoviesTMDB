package com.example.moviesapp.ui.screens.seasons

import androidx.compose.runtime.Stable
import com.example.moviesapp.model.AggregatedCredits
import com.example.moviesapp.model.Image
import com.example.moviesapp.model.SeasonDetails
import com.example.moviesapp.model.Video
import com.example.moviesapp.ui.screens.destinations.TvScreenDestination

@Stable
data class SeasonDetailsScreenUiState(
    val startRoute: String,
    val seasonDetails: SeasonDetails?,
    val aggregatedCredits: AggregatedCredits?,
    val videos: List<Video>?,
    val episodeCount: Int?,
    val episodeStills: Map<Int, List<Image>>,
    val error: String?
) {
    companion object {
        val default: SeasonDetailsScreenUiState = SeasonDetailsScreenUiState(
            startRoute = TvScreenDestination.route,
            seasonDetails = null,
            aggregatedCredits = null,
            videos = null,
            episodeCount = null,
            episodeStills = emptyMap(),
            error = null
        )
    }
}