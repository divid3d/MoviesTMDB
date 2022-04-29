package com.example.moviesapp.use_case

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.api.awaitApiResponse
import com.example.moviesapp.model.AggregatedCredits
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.repository.season.SeasonRepository
import com.example.moviesapp.use_case.interfaces.GetSeasonCreditsUseCase
import javax.inject.Inject

class GetSeasonCreditsUseCaseImpl @Inject constructor(
    private val seasonRepository: SeasonRepository
) : GetSeasonCreditsUseCase {
    override suspend operator fun invoke(
        tvSeriesId: Int,
        seasonNumber: Int,
        deviceLanguage: DeviceLanguage
    ): ApiResponse<AggregatedCredits> {
        return seasonRepository.seasonCredits(
            tvSeriesId = tvSeriesId,
            seasonNumber = seasonNumber,
            isoCode = deviceLanguage.languageCode
        ).awaitApiResponse()
    }
}