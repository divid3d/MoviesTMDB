package com.example.moviesapp.use_case.interfaces

import androidx.paging.PagingData
import com.example.moviesapp.model.RecentlyBrowsedMovie
import kotlinx.coroutines.flow.Flow

interface GetRecentlyBrowsedMoviesUseCase {
    operator fun invoke(): Flow<PagingData<RecentlyBrowsedMovie>>
}