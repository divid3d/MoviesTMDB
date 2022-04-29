package com.example.moviesapp.use_case.interfaces

import kotlinx.coroutines.flow.Flow

interface GetFavouriteMoviesIdsUseCase {
    operator fun invoke(): Flow<List<Int>>
}