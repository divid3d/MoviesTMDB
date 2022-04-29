package com.example.moviesapp.use_case

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.api.awaitApiResponse
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.MovieDetails
import com.example.moviesapp.repository.movie.MovieRepository
import com.example.moviesapp.use_case.interfaces.GetMovieDetailsUseCase
import javax.inject.Inject

class GetMovieDetailsUseCaseImpl @Inject constructor(
    private val movieRepository: MovieRepository
) : GetMovieDetailsUseCase {
    override suspend operator fun invoke(
        movieId: Int,
        deviceLanguage: DeviceLanguage
    ): ApiResponse<MovieDetails> {
        return movieRepository.movieDetails(
            movieId = movieId,
            isoCode = deviceLanguage.languageCode
        ).awaitApiResponse()
    }
}