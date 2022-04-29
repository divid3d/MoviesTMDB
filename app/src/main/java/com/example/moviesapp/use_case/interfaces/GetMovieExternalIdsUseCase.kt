package com.example.moviesapp.use_case.interfaces

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.model.ExternalId

interface GetMovieExternalIdsUseCase {
    suspend operator fun invoke(movieId: Int): ApiResponse<List<ExternalId>>
}