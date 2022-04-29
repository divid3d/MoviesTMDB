package com.example.moviesapp.use_case

import androidx.paging.PagingData
import com.example.moviesapp.model.RecentlyBrowsedTvSeries
import com.example.moviesapp.repository.browsed.RecentlyBrowsedRepository
import com.example.moviesapp.use_case.interfaces.GetRecentlyBrowsedTvSeriesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentlyBrowsedTvSeriesUseCaseImpl @Inject constructor(
    private val recentlyBrowsedRepository: RecentlyBrowsedRepository,
) : GetRecentlyBrowsedTvSeriesUseCase {
    override operator fun invoke(): Flow<PagingData<RecentlyBrowsedTvSeries>> {
        return recentlyBrowsedRepository.recentlyBrowsedTvSeries()
    }
}