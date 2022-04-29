package com.example.moviesapp.use_case.interfaces

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.model.AggregatedCredits
import com.example.moviesapp.model.DeviceLanguage

interface GetSeasonCreditsUseCase {
    suspend operator fun invoke(
        tvSeriesId: Int,
        seasonNumber: Int,
        deviceLanguage: DeviceLanguage
    ): ApiResponse<AggregatedCredits>
}