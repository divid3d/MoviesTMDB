package com.example.moviesapp.use_case

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.api.awaitApiResponse
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.SeasonDetails
import com.example.moviesapp.repository.season.SeasonRepository
import com.example.moviesapp.use_case.interfaces.GetSeasonDetailsUseCase
import javax.inject.Inject

class GetSeasonDetailsUseCaseImpl @Inject constructor(
    private val seasonRepository: SeasonRepository
) : GetSeasonDetailsUseCase {
    override suspend operator fun invoke(
        tvSeriesId: Int,
        seasonNumber: Int,
        deviceLanguage: DeviceLanguage
    ): ApiResponse<SeasonDetails> {
        return seasonRepository.seasonDetails(
            tvSeriesId = tvSeriesId,
            seasonNumber = seasonNumber,
            deviceLanguage = deviceLanguage
        ).awaitApiResponse()
    }
}