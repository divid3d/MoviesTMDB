package com.example.moviesapp.use_case

import com.example.moviesapp.model.MovieDetails
import com.example.moviesapp.repository.browsed.RecentlyBrowsedRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddRecentlyBrowsedMovieUseCase @Inject constructor(
    private val recentlyBrowsedRepository: RecentlyBrowsedRepository
) {
    operator fun invoke(details: MovieDetails){
        return recentlyBrowsedRepository.addRecentlyBrowsedMovie(details)
    }
}