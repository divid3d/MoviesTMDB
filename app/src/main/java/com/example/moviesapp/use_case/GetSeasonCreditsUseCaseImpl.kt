package com.example.moviesapp.use_case

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.api.awaitApiResponse
import com.example.moviesapp.model.AggregatedCredits
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.repository.tv.TvSeriesRepository
import com.example.moviesapp.use_case.interfaces.GetSeasonCreditsUseCase
import javax.inject.Inject

class GetSeasonCreditsUseCaseImpl @Inject constructor(
    private val tvSeriesRepository: TvSeriesRepository
) : GetSeasonCreditsUseCase {
    override suspend operator fun invoke(
        tvSeriesId: Int,
        seasonNumber: Int,
        deviceLanguage: DeviceLanguage
    ): ApiResponse<AggregatedCredits> {
        return tvSeriesRepository.seasonCredits(
            tvSeriesId = tvSeriesId,
            seasonNumber = seasonNumber,
            isoCode = deviceLanguage.languageCode
        ).awaitApiResponse()
    }
}