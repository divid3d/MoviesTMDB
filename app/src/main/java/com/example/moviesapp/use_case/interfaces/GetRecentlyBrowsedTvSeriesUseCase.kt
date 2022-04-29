package com.example.moviesapp.use_case.interfaces

import androidx.paging.PagingData
import com.example.moviesapp.model.RecentlyBrowsedTvSeries
import kotlinx.coroutines.flow.Flow

interface GetRecentlyBrowsedTvSeriesUseCase {
    operator fun invoke(): Flow<PagingData<RecentlyBrowsedTvSeries>>
}