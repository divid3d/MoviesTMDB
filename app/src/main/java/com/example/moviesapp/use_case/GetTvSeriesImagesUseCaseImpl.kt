package com.example.moviesapp.use_case

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.api.awaitApiResponse
import com.example.moviesapp.model.Image
import com.example.moviesapp.repository.tv.TvSeriesRepository
import com.example.moviesapp.use_case.interfaces.GetTvSeriesImagesUseCase
import javax.inject.Inject

class GetTvSeriesImagesUseCaseImpl @Inject constructor(
    private val tvSeriesRepository: TvSeriesRepository
) : GetTvSeriesImagesUseCase {
    override suspend fun invoke(tvSeriesId: Int): ApiResponse<List<Image>> {
        val response = tvSeriesRepository
            .tvSeriesImages(tvSeriesId)
            .awaitApiResponse()

        return when (response) {
            is ApiResponse.Success -> {
                val images = response.data?.backdrops ?: emptyList()

                ApiResponse.Success(images)
            }
            is ApiResponse.Failure -> ApiResponse.Failure(response.apiError)
            is ApiResponse.Exception -> ApiResponse.Exception(response.exception)
        }
    }
}