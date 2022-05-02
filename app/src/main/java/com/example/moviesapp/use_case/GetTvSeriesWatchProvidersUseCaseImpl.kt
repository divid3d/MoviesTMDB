package com.example.moviesapp.use_case

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.api.awaitApiResponse
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.WatchProviders
import com.example.moviesapp.repository.tv.TvSeriesRepository
import com.example.moviesapp.use_case.interfaces.GetTvSeriesWatchProvidersUseCase
import javax.inject.Inject

class GetTvSeriesWatchProvidersUseCaseImpl @Inject constructor(
    private val tvSeriesRepository: TvSeriesRepository
) : GetTvSeriesWatchProvidersUseCase {
    override suspend fun invoke(
        tvSeriesId: Int,
        deviceLanguage: DeviceLanguage
    ): ApiResponse<WatchProviders?> {
        val response = tvSeriesRepository
            .watchProviders(
                tvSeriesId = tvSeriesId
            ).awaitApiResponse()

        return when (response) {
            is ApiResponse.Success -> {
                val results = response.data?.results
                val providers = results?.getOrElse(deviceLanguage.region) { null }

                ApiResponse.Success(providers)
            }
            is ApiResponse.Failure -> ApiResponse.Failure(response.apiError)
            is ApiResponse.Exception -> ApiResponse.Exception(response.exception)
        }
    }
}