package com.example.moviesapp.use_case.interfaces

import com.example.moviesapp.model.Genre
import kotlinx.coroutines.flow.Flow

interface GetMovieGenresUseCase {
    operator fun invoke(): Flow<List<Genre>>
}