package com.example.moviesapp.use_case

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.api.awaitApiResponse
import com.example.moviesapp.model.Image
import com.example.moviesapp.repository.movie.MovieRepository
import com.example.moviesapp.use_case.interfaces.GetMovieBackdropsUseCase
import javax.inject.Inject

class GetMovieBackdropsUseCaseImpl @Inject constructor(
    private val movieRepository: MovieRepository
) : GetMovieBackdropsUseCase {
    override suspend operator fun invoke(movieId: Int): ApiResponse<List<Image>> {
        val response = movieRepository
            .movieImages(movieId)
            .awaitApiResponse()

        return when (response) {
            is ApiResponse.Success -> {
                val backdrops = response.data?.backdrops ?: emptyList()
                ApiResponse.Success(backdrops)
            }
            is ApiResponse.Failure -> ApiResponse.Failure(response.apiError)
            is ApiResponse.Exception -> ApiResponse.Exception(response.exception)
        }
    }
}