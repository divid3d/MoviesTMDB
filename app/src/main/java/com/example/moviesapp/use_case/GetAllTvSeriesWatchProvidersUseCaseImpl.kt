package com.example.moviesapp.use_case

import com.example.moviesapp.repository.config.ConfigRepository
import com.example.moviesapp.use_case.interfaces.GetAllTvSeriesWatchProvidersUseCase
import javax.inject.Inject

class GetAllTvSeriesWatchProvidersUseCaseImpl @Inject constructor(
    private val configRepository: ConfigRepository
) : GetAllTvSeriesWatchProvidersUseCase {
    override operator fun invoke() = configRepository.getAllTvSeriesWatchProviders()
}