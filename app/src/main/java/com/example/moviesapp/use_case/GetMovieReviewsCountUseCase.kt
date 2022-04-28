package com.example.moviesapp.use_case

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.api.awaitApiResponse
import com.example.moviesapp.repository.movie.MovieRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMovieReviewsCountUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int): ApiResponse<Int> {
        val response = movieRepository.movieReview(movieId).awaitApiResponse()

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