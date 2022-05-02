package com.example.moviesapp.use_case

import com.example.moviesapp.model.TvSeriesDetails
import com.example.moviesapp.repository.browsed.RecentlyBrowsedRepository
import com.example.moviesapp.use_case.interfaces.AddRecentlyBrowsedTvSeriesUseCase
import javax.inject.Inject

class AddRecentlyBrowsedTvSeriesUseCaseImpl @Inject constructor(
    private val recentlyBrowsedRepository: RecentlyBrowsedRepository
) : AddRecentlyBrowsedTvSeriesUseCase {
    override operator fun invoke(details: TvSeriesDetails) {
        return recentlyBrowsedRepository.addRecentlyBrowsedTvSeries(details)
    }
}