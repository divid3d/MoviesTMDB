package com.example.moviesapp.use_case

import com.example.moviesapp.repository.config.ConfigRepository
import com.example.moviesapp.use_case.interfaces.GetMovieGenresUseCase
import javax.inject.Inject

class GetMovieGenresUseCaseImpl @Inject constructor(
    private val configRepository: ConfigRepository
) : GetMovieGenresUseCase {
    override operator fun invoke() = configRepository.getMovieGenres()
}