package com.example.moviesapp.use_case

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.api.awaitApiResponse
import com.example.moviesapp.model.Image
import com.example.moviesapp.repository.tv.TvSeriesRepository
import com.example.moviesapp.use_case.interfaces.GetEpisodeStillsUseCase
import javax.inject.Inject

class GetEpisodeStillsUseCaseImpl @Inject constructor(
    private val tvSeriesRepository: TvSeriesRepository
) : GetEpisodeStillsUseCase {
    override suspend operator fun invoke(
        tvSeriesId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): ApiResponse<List<Image>> {
        val response = tvSeriesRepository.episodeImages(
            tvSeriesId = tvSeriesId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber
        ).awaitApiResponse()

        return when (response) {
            is ApiResponse.Success -> {
                val stills = response.data?.stills ?: emptyList()
                ApiResponse.Success(stills)
            }
            is ApiResponse.Failure -> ApiResponse.Failure(response.apiError)
            is ApiResponse.Exception -> ApiResponse.Exception(response.exception)
        }
    }
}