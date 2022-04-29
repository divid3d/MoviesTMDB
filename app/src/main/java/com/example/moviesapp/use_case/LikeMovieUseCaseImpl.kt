package com.example.moviesapp.use_case

import com.example.moviesapp.model.MovieDetails
import com.example.moviesapp.repository.favourites.FavouritesRepository
import com.example.moviesapp.use_case.interfaces.LikeMovieUseCase
import javax.inject.Inject

class LikeMovieUseCaseImpl @Inject constructor(
    private val favouritesRepository: FavouritesRepository
) : LikeMovieUseCase {
    override operator fun invoke(details: MovieDetails) {
        return favouritesRepository.likeMovie(details)
    }
}