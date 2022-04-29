package com.example.moviesapp.use_case.interfaces

import com.example.moviesapp.api.ApiResponse

interface GetMovieReviewsCountUseCase {
    suspend operator fun invoke(movieId: Int): ApiResponse<Int>
}