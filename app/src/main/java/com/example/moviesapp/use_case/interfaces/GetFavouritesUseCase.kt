package com.example.moviesapp.use_case.interfaces

import androidx.paging.PagingData
import com.example.moviesapp.model.FavouriteType
import com.example.moviesapp.model.Presentable
import kotlinx.coroutines.flow.Flow

interface GetFavouritesUseCase {
    operator fun invoke(type: FavouriteType): Flow<PagingData<Presentable>>
}