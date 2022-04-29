package com.example.moviesapp.use_case.interfaces

import kotlinx.coroutines.flow.Flow

interface GetFavouriteTvSeriesCountUseCase {
    operator fun invoke(): Flow<Int>
}