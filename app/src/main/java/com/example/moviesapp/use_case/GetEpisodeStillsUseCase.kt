package com.example.moviesapp.use_case

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.api.awaitApiResponse
import com.example.moviesapp.model.Image
import com.example.moviesapp.model.ImagesResponse
import com.example.moviesapp.repository.tv.TvSeriesRepository
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetEpisodeStillsUseCase @Inject constructor(
    private val tvSeriesRepository: TvSeriesRepository
) {
    suspend operator fun invoke(
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