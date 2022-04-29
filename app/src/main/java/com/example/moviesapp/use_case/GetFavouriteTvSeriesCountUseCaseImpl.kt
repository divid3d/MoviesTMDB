package com.example.moviesapp.use_case

import com.example.moviesapp.repository.favourites.FavouritesRepository
import com.example.moviesapp.use_case.interfaces.GetFavouriteTvSeriesCountUseCase
import javax.inject.Inject

class GetFavouriteTvSeriesCountUseCaseImpl @Inject constructor(
    private val favouritesRepository: FavouritesRepository
) : GetFavouriteTvSeriesCountUseCase {
    override operator fun invoke() = favouritesRepository.getFavouriteTvSeriesCount()
}