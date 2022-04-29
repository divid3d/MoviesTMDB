package com.example.moviesapp.use_case

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.api.awaitApiResponse
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.SeasonDetails
import com.example.moviesapp.repository.tv.TvSeriesRepository
import com.example.moviesapp.use_case.interfaces.GetSeasonDetailsUseCase
import javax.inject.Inject

class GetSeasonDetailsUseCaseImpl @Inject constructor(
    private val tvSeriesRepository: TvSeriesRepository
) : GetSeasonDetailsUseCase {
    override suspend operator fun invoke(
        tvSeriesId: Int,
        seasonNumber: Int,
        deviceLanguage: DeviceLanguage
    ): ApiResponse<SeasonDetails> {
        return tvSeriesRepository.seasonDetails(
            tvSeriesId = tvSeriesId,
            seasonNumber = seasonNumber,
            deviceLanguage = deviceLanguage
        ).awaitApiResponse()
    }
}