package com.example.moviesapp.use_case

import com.example.moviesapp.repository.browsed.RecentlyBrowsedRepository
import com.example.moviesapp.use_case.interfaces.ClearRecentlyBrowsedMoviesUseCase
import javax.inject.Inject

class ClearRecentlyBrowsedMoviesUseCaseImpl @Inject constructor(
    private val recentlyBrowsedRepository: RecentlyBrowsedRepository
) : ClearRecentlyBrowsedMoviesUseCase {
    override operator fun invoke() = recentlyBrowsedRepository.clearRecentlyBrowsedMovies()
}