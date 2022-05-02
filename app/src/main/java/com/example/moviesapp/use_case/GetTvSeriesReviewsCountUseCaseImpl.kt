package com.example.moviesapp.use_case

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.api.awaitApiResponse
import com.example.moviesapp.repository.tv.TvSeriesRepository
import com.example.moviesapp.use_case.interfaces.GetTvSeriesReviewsCountUseCase
import javax.inject.Inject

class GetTvSeriesReviewsCountUseCaseImpl @Inject constructor(
    private val tvSeriesRepository: TvSeriesRepository
) : GetTvSeriesReviewsCountUseCase {
    override suspend fun invoke(tvSeriesId: Int): ApiResponse<Int> {
        val response = tvSeriesRepository
            .tvSeriesReview(tvSeriesId)
            .awaitApiResponse()

        return when (response) {
            is ApiResponse.Success -> {
                val reviewsCount = response.data?.totalResults ?: 0

                ApiResponse.Success(reviewsCount)
            }
            is ApiResponse.Failure -> ApiResponse.Failure(response.apiError)
            is ApiResponse.Exception -> ApiResponse.Exception(response.exception)
        }
    }
}