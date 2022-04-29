package com.example.moviesapp.use_case

import androidx.paging.PagingData
import com.example.moviesapp.model.TvSeriesFavourite
import com.example.moviesapp.repository.favourites.FavouritesRepository
import com.example.moviesapp.use_case.interfaces.GetFavouritesTvSeriesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavouritesTvSeriesUseCaseImpl @Inject constructor(
    private val favouritesRepository: FavouritesRepository,
) : GetFavouritesTvSeriesUseCase {
    override operator fun invoke(): Flow<PagingData<TvSeriesFavourite>> {
        return favouritesRepository.favouritesTvSeries()
    }
}