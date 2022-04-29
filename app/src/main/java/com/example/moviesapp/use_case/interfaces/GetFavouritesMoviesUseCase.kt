package com.example.moviesapp.use_case.interfaces

import androidx.paging.PagingData
import com.example.moviesapp.model.MovieFavourite
import kotlinx.coroutines.flow.Flow

interface GetFavouritesMoviesUseCase {
    operator fun invoke(): Flow<PagingData<MovieFavourite>>
}