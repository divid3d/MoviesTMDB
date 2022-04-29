package com.example.moviesapp.use_case.interfaces

import androidx.paging.PagingData
import com.example.moviesapp.model.TvSeriesFavourite
import kotlinx.coroutines.flow.Flow

interface GetFavouritesTvSeriesUseCase {
    operator fun invoke(): Flow<PagingData<TvSeriesFavourite>>
}