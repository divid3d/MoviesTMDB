package com.example.moviesapp.use_case

import com.example.moviesapp.repository.browsed.RecentlyBrowsedRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClearRecentlyBrowsedMoviesUseCase @Inject constructor(
    private val recentlyBrowsedRepository: RecentlyBrowsedRepository
) {
    operator fun invoke() = recentlyBrowsedRepository.clearRecentlyBrowsedMovies()
}