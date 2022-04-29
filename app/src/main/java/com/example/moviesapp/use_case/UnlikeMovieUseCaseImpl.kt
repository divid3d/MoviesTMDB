package com.example.moviesapp.use_case

import com.example.moviesapp.model.MovieDetails
import com.example.moviesapp.repository.favourites.FavouritesRepository
import com.example.moviesapp.use_case.interfaces.UnlikeMovieUseCase
import javax.inject.Inject

class UnlikeMovieUseCaseImpl @Inject constructor(
    private val favouritesRepository: FavouritesRepository
) : UnlikeMovieUseCase {
    override operator fun invoke(details: MovieDetails) {
        return favouritesRepository.unlikeMovie(details)
    }
}