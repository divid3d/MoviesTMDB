package com.example.moviesapp.use_case.interfaces

import kotlinx.coroutines.flow.Flow

interface GetFavouriteTvSeriesIdsUseCase {
    operator fun invoke(): Flow<List<Int>>
}