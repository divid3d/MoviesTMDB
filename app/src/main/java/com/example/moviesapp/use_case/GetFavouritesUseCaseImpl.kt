package com.example.moviesapp.use_case

import androidx.paging.PagingData
import androidx.paging.map
import com.example.moviesapp.model.FavouriteType
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.repository.favourites.FavouritesRepository
import com.example.moviesapp.use_case.interfaces.GetFavouritesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class GetFavouritesUseCaseImpl @Inject constructor(
    private val favouritesRepository: FavouritesRepository
) : GetFavouritesUseCase {
    override operator fun invoke(type: FavouriteType): Flow<PagingData<Presentable>> {
        val favourites = when (type) {
            FavouriteType.Movie -> favouritesRepository.favouriteMovies()
            FavouriteType.TvSeries -> favouritesRepository.favouritesTvSeries()
        }.mapLatest { data -> data.map { it } }

        return favourites
    }
}