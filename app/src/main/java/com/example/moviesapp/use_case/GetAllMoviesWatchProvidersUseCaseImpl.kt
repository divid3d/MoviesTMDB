package com.example.moviesapp.use_case

import com.example.moviesapp.repository.config.ConfigRepository
import com.example.moviesapp.use_case.interfaces.GetAllMoviesWatchProvidersUseCase
import javax.inject.Inject

class GetAllMoviesWatchProvidersUseCaseImpl @Inject constructor(
    private val configRepository: ConfigRepository
): GetAllMoviesWatchProvidersUseCase{
    override operator fun invoke() = configRepository.getAllMoviesWatchProviders()
}