package com.example.moviesapp.use_case

import com.example.moviesapp.model.MovieDetails
import com.example.moviesapp.repository.favourites.FavouritesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LikeMovieUseCase @Inject constructor(
    private val favouritesRepository: FavouritesRepository
) {
    operator  fun invoke(details: MovieDetails){
        return favouritesRepository.likeMovie(details)
    }
}