package com.example.moviesapp.ui.screens.seasons

import com.example.moviesapp.model.Image
import com.example.moviesapp.model.SeasonDetails
import com.example.moviesapp.model.Video
import com.example.moviesapp.ui.screens.destinations.TvScreenDestination

data class SeasonDetailsScreenUiState(
    val startRoute: String,
    val seasonDetails: SeasonDetails?,
    val videos: List<Video>?,
    val episodeCount: Int?,
    val episodeStills: Map<Int, List<Image>>,
    val error: String?
) {
    companion object {
        val default: SeasonDetailsScreenUiState
            get() = SeasonDetailsScreenUiState(
                startRoute = TvScreenDestination.route,
                seasonDetails = null,
                videos = null,
                episodeCount = null,
                episodeStills = emptyMap(),
                error = null
            )
    }
}