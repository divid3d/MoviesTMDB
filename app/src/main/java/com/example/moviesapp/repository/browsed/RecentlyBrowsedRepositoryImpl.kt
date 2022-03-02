package com.example.moviesapp.repository.browsed

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
import javax.inject.Singleton

@Singleton
class RecentlyBrowsedRepositoryImpl(
    private val externalScope: CoroutineScope,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val recentlyBrowsedMoviesDao: RecentlyBrowsedMoviesDao,
    private val recentlyBrowsedTvSeriesDao: RecentlyBrowsedTvSeriesDao
) : RecentlyBrowsedRepository {
    private companion object {
        const val maxItems = 100
    }

    override fun addRecentlyBrowsedMovie(movieDetails: MovieDetails) {
        externalScope.launch(defaultDispatcher) {
            val recentlyBrowsedMovie = movieDetails.run {
                RecentlyBrowsedMovie(
                    id = id,
                    posterPath = posterPath,
                    title = title,
                    addedDate = Date()
                )
            }

            recentlyBrowsedMoviesDao.deleteAndAdd(
                recentlyBrowsedMovie,
                maxItems = maxItems
            )
        }
    }

    override fun clearRecentlyBrowsedMovies() {
        externalScope.launch(defaultDispatcher) {
            recentlyBrowsedMoviesDao.clear()
        }
    }

    override fun clearRecentlyBrowsedTvSeries() {
        externalScope.launch(defaultDispatcher) {
            recentlyBrowsedTvSeriesDao.clear()
        }
    }

    override fun recentlyBrowsedMovies(): Flow<PagingData<RecentlyBrowsedMovie>> = Pager(
        PagingConfig(pageSize = 20)
    ) {
        recentlyBrowsedMoviesDao.recentBrowsedMovies().asPagingSourceFactory()()
    }.flow.flowOn(defaultDispatcher)

    override fun addRecentlyBrowsedTvSeries(tvSeriesDetails: TvSeriesDetails) {
        externalScope.launch(defaultDispatcher) {
            val recentlyBrowsedTvSeries = tvSeriesDetails.run {
                RecentlyBrowsedTvSeries(
                    id = id,
                    posterPath = posterPath,
                    name = title,
                    addedDate = Date()
                )
            }

            recentlyBrowsedTvSeriesDao.deleteAndAdd(
                recentlyBrowsedTvSeries,
                maxItems = maxItems
            )
        }
    }

    override fun recentlyBrowsedTvSeries(): Flow<PagingData<RecentlyBrowsedTvSeries>> = Pager(
        PagingConfig(pageSize = 20)
    ) {
        recentlyBrowsedTvSeriesDao.recentBrowsedTvSeries().asPagingSourceFactory()()
    }.flow.flowOn(defaultDispatcher)

}