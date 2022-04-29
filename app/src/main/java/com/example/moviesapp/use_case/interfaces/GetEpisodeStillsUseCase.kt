package com.example.moviesapp.use_case.interfaces

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.model.Image

interface GetEpisodeStillsUseCase {
    suspend operator fun invoke(
        tvSeriesId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): ApiResponse<List<Image>>
}