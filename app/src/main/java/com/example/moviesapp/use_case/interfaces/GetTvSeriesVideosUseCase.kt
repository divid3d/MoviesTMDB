package com.example.moviesapp.use_case.interfaces

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Video

interface GetTvSeriesVideosUseCase {
    suspend operator fun invoke(
        tvSeriesId: Int,
        deviceLanguage: DeviceLanguage
    ): ApiResponse<List<Video>>
}