package com.example.moviesapp.use_case

import com.example.moviesapp.repository.favourites.FavouritesRepository
import com.example.moviesapp.use_case.interfaces.GetFavouriteMoviesIdsUseCase
import javax.inject.Inject

class GetFavouriteMoviesIdsUseCaseImpl @Inject constructor(
    private val favouritesRepository: FavouritesRepository
) : GetFavouriteMoviesIdsUseCase {
    override operator fun invoke() = favouritesRepository.getFavouritesMoviesIds()
}