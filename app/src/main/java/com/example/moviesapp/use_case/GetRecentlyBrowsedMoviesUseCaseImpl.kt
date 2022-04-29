package com.example.moviesapp.use_case

import androidx.paging.PagingData
import com.example.moviesapp.model.RecentlyBrowsedMovie
import com.example.moviesapp.repository.browsed.RecentlyBrowsedRepository
import com.example.moviesapp.use_case.interfaces.GetRecentlyBrowsedMoviesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentlyBrowsedMoviesUseCaseImpl @Inject constructor(
    private val recentlyBrowsedRepository: RecentlyBrowsedRepository,
) : GetRecentlyBrowsedMoviesUseCase {
    override operator fun invoke(): Flow<PagingData<RecentlyBrowsedMovie>> {
        return recentlyBrowsedRepository.recentlyBrowsedMovies()
    }
}