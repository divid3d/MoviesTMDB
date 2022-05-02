package com.example.moviesapp.use_case.interfaces

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.model.Image

interface GetTvSeriesImagesUseCase {
    suspend operator fun invoke(tvSeriesId: Int): ApiResponse<List<Image>>
}