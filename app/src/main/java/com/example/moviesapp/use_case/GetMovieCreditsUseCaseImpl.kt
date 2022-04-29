package com.example.moviesapp.use_case

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.api.awaitApiResponse
import com.example.moviesapp.model.Credits
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.repository.movie.MovieRepository
import com.example.moviesapp.use_case.interfaces.GetMovieCreditsUseCase
import javax.inject.Inject

class GetMovieCreditsUseCaseImpl @Inject constructor(
    private val movieRepository: MovieRepository
) : GetMovieCreditsUseCase {
    override suspend operator fun invoke(
        movieId: Int,
        deviceLanguage: DeviceLanguage
    ): ApiResponse<Credits> {
        return movieRepository.movieCredits(
            movieId = movieId,
            isoCode = deviceLanguage.languageCode
        ).awaitApiResponse()
    }
}