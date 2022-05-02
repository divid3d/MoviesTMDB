package com.example.moviesapp.use_case.interfaces

import com.example.moviesapp.api.ApiResponse

interface GetTvSeriesReviewsCountUseCase {
    suspend operator fun invoke(tvSeriesId: Int): ApiResponse<Int>
}