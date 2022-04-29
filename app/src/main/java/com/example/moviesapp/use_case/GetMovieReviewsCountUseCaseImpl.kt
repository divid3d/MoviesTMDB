package com.example.moviesapp.use_case

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.api.awaitApiResponse
import com.example.moviesapp.repository.movie.MovieRepository
import com.example.moviesapp.use_case.interfaces.GetMovieReviewsCountUseCase
import javax.inject.Inject

class GetMovieReviewsCountUseCaseImpl @Inject constructor(
    private val movieRepository: MovieRepository
) : GetMovieReviewsCountUseCase {
    override suspend operator fun invoke(movieId: Int): ApiResponse<Int> {
        val response = movieRepository
            .movieReview(movieId)
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