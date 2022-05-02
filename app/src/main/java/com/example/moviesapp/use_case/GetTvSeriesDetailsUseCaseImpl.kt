package com.example.moviesapp.use_case

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.api.awaitApiResponse
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.TvSeriesDetails
import com.example.moviesapp.repository.tv.TvSeriesRepository
import com.example.moviesapp.use_case.interfaces.GetTvSeriesDetailsUseCase
import javax.inject.Inject

class GetTvSeriesDetailsUseCaseImpl @Inject constructor(
    private val tvSeriesRepository: TvSeriesRepository
) : GetTvSeriesDetailsUseCase {
    override suspend operator fun invoke(
        tvSeriesId: Int,
        deviceLanguage: DeviceLanguage
    ): ApiResponse<TvSeriesDetails> {
        return tvSeriesRepository.getTvSeriesDetails(
            tvSeriesId = tvSeriesId,
            deviceLanguage = deviceLanguage
        ).awaitApiResponse()
    }
}