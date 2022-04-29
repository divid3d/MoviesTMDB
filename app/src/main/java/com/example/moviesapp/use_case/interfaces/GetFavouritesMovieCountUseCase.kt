package com.example.moviesapp.use_case.interfaces

import kotlinx.coroutines.flow.Flow

interface GetFavouritesMovieCountUseCase {
    operator fun invoke(): Flow<Int>
}