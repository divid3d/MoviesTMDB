package com.example.moviesapp.use_case

import com.example.moviesapp.repository.favourites.FavouritesRepository
import com.example.moviesapp.use_case.interfaces.GetFavouritesMovieCountUseCase
import javax.inject.Inject

class GetFavouritesMovieCountUseCaseImpl @Inject constructor(
    private val favouritesRepository: FavouritesRepository
) : GetFavouritesMovieCountUseCase {
    override operator fun invoke() = favouritesRepository.getFavouriteMoviesCount()
}