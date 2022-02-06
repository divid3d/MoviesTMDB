package com.example.moviesapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviesapp.db.RecentlyBrowsedMoviesDao
import com.example.moviesapp.db.RecentlyBrowsedTvSeriesDao
import com.example.moviesapp.model.MovieDetails
import com.example.moviesapp.model.RecentlyBrowsedMovie
import com.example.moviesapp.model.RecentlyBrowsedTvSeries
import com.example.moviesapp.model.TvSeriesDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecentlyBrowsedRepository @Inject constructor(
    private val externalScope: CoroutineScope,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val recentlyBrowsedMoviesDao: RecentlyBrowsedMoviesDao,
    private val recentlyBrowsedTvSeriesDao: RecentlyBrowsedTvSeriesDao
) {
    private companion object {
        const val maxItems = 100
    }

    fun addRecentlyBrowsedMovie(movieDetails: MovieDetails) {
        externalScope.launch(defaultDispatcher) {
            val recentlyBrowsedMovie = movieDetails.run {
                RecentlyBrowsedMovie(
                    id = id,
                    posterPath = posterPath,
                    title = title,
                    backdropPath = backdropPath,
                    posterUrl = posterUrl,
                    backdropUrl = backdropUrl,
                    addedDate = Date()
                )
            }

            recentlyBrowsedMoviesDao.deleteAndAdd(
                recentlyBrowsedMovie,
                maxItems = maxItems
            )
        }
    }

    fun clearRecentlyBrowsedMovies() {
        externalScope.launch(defaultDispatcher) {
            recentlyBrowsedMoviesDao.clear()
        }
    }

    fun clearRecentlyBrowsedTvSeries() {
        externalScope.launch(defaultDispatcher) {
            recentlyBrowsedTvSeriesDao.clear()
        }
    }

    fun recentlyBrowsedMovies(): Flow<PagingData<RecentlyBrowsedMovie>> = Pager(
        PagingConfig(pageSize = 20)
    ) {
        recentlyBrowsedMoviesDao.recentBrowsedMovie().asPagingSourceFactory()()
    }.flow.flowOn(defaultDispatcher)

    fun addRecentlyBrowsedTvSeries(tvSeriesDetails: TvSeriesDetails) {
        externalScope.launch(defaultDispatcher) {
            val recentlyBrowsedTvSeries = tvSeriesDetails.run {
                RecentlyBrowsedTvSeries(
                    id = id,
                    posterPath = posterPath,
                    name = title,
                    backdropPath = backdropPath,
                    addedDate = Date()
                )
            }

            recentlyBrowsedTvSeriesDao.deleteAndAdd(
                recentlyBrowsedTvSeries,
                maxItems = maxItems
            )
        }
    }

    fun recentlyBrowsedTvSeries(): Flow<PagingData<RecentlyBrowsedTvSeries>> = Pager(
        PagingConfig(pageSize = 20)
    ) {
        recentlyBrowsedTvSeriesDao.recentBrowsedTvSeries().asPagingSourceFactory()()
    }.flow.flowOn(defaultDispatcher)

}