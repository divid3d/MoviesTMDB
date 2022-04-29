package com.example.moviesapp.use_case.interfaces

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.model.Image

interface GetMovieBackdropsUseCase {
    suspend operator fun invoke(movieId: Int): ApiResponse<List<Image>>
}