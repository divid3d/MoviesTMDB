package com.example.moviesapp.use_case

import com.example.moviesapp.repository.favourites.FavouritesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetFavouritesMovieCountUseCase @Inject constructor(
    private val favouritesRepository: FavouritesRepository
) {
    operator fun invoke() = favouritesRepository.getFavouriteMoviesCount()
}