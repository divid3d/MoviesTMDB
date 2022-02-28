package com.example.moviesapp.repository.browsed

import androidx.paging.PagingData
import com.example.moviesapp.model.MovieDetails
import com.example.moviesapp.model.RecentlyBrowsedMovie
import com.example.moviesapp.model.RecentlyBrowsedTvSeries
import com.example.moviesapp.model.TvSeriesDetails
import kotlinx.coroutines.flow.Flow

interface RecentlyBrowsedRepository {
    fun addRecentlyBrowsedMovie(movieDetails: MovieDetails)

    fun clearRecentlyBrowsedMovies()

    fun clearRecentlyBrowsedTvSeries()

    fun recentlyBrowsedMovies(): Flow<PagingData<RecentlyBrowsedMovie>>

    fun addRecentlyBrowsedTvSeries(tvSeriesDetails: TvSeriesDetails)

    fun recentlyBrowsedTvSeries(): Flow<PagingData<RecentlyBrowsedTvSeries>>
}