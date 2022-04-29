package com.example.moviesapp.use_case

import com.example.moviesapp.model.MovieDetails
import com.example.moviesapp.repository.browsed.RecentlyBrowsedRepository
import com.example.moviesapp.use_case.interfaces.AddRecentlyBrowsedMovieUseCase
import javax.inject.Inject

class AddRecentlyBrowsedMovieUseCaseImpl @Inject constructor(
    private val recentlyBrowsedRepository: RecentlyBrowsedRepository
) : AddRecentlyBrowsedMovieUseCase {
    override operator fun invoke(details: MovieDetails) {
        return recentlyBrowsedRepository.addRecentlyBrowsedMovie(details)
    }
}