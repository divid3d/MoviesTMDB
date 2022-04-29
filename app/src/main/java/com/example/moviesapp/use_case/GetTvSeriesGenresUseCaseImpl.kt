package com.example.moviesapp.use_case

import com.example.moviesapp.repository.config.ConfigRepository
import com.example.moviesapp.use_case.interfaces.GetTvSeriesGenresUseCase
import javax.inject.Inject

class GetTvSeriesGenresUseCaseImpl @Inject constructor(
    private val configRepository: ConfigRepository
) : GetTvSeriesGenresUseCase {
    override operator fun invoke() = configRepository.getTvSeriesGenres()
}