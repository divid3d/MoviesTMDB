package com.example.moviesapp.use_case

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.api.awaitApiResponse
import com.example.moviesapp.model.AggregatedCredits
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.repository.tv.TvSeriesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSeasonCreditsUseCase @Inject constructor(
    private val tvSeriesRepository: TvSeriesRepository
) {
    suspend operator fun invoke(
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