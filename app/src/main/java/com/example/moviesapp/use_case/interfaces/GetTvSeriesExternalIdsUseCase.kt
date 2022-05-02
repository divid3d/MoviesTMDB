package com.example.moviesapp.use_case.interfaces

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.model.ExternalId

interface GetTvSeriesExternalIdsUseCase {
    suspend operator fun invoke(tvSeriesId: Int): ApiResponse<List<ExternalId>>
}