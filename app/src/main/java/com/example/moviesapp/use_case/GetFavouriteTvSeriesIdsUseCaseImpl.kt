package com.example.moviesapp.use_case

import com.example.moviesapp.repository.favourites.FavouritesRepository
import com.example.moviesapp.use_case.interfaces.GetFavouriteTvSeriesIdsUseCase
import javax.inject.Inject

class GetFavouriteTvSeriesIdsUseCaseImpl @Inject constructor(
    private val favouritesRepository: FavouritesRepository
) : GetFavouriteTvSeriesIdsUseCase {
    override operator fun invoke() = favouritesRepository.getFavouriteTvSeriesIds()
}