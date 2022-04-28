package com.example.moviesapp.use_case

import androidx.paging.PagingData
import com.example.moviesapp.model.RecentlyBrowsedTvSeries
import com.example.moviesapp.repository.browsed.RecentlyBrowsedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetRecentlyBrowsedTvSeriesUseCase @Inject constructor(
    private val recentlyBrowsedRepository: RecentlyBrowsedRepository,
) {
    operator fun invoke(): Flow<PagingData<RecentlyBrowsedTvSeries>> {
        return recentlyBrowsedRepository.recentlyBrowsedTvSeries()
    }
}