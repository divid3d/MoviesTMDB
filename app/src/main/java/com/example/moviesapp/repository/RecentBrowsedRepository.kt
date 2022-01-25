package com.example.moviesapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviesapp.db.RecentBrowsedMoviesDao
import com.example.moviesapp.model.MovieDetails
import com.example.moviesapp.model.RecentlyBrowsedMovie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecentBrowsedRepository @Inject constructor(
    private val externalScope: CoroutineScope,
    private val recentBrowsedMoviesDao: RecentBrowsedMoviesDao
) {
    fun addRecentBrowsedMovie(movieDetails: MovieDetails) {
        externalScope.launch {
            val recentBrowsedMovie = RecentlyBrowsedMovie(
                id = movieDetails.id,
                posterPath = movieDetails.posterPath,
                title = movieDetails.title,
                backdropPath = movieDetails.backdropPath,
                posterUrl = movieDetails.posterUrl,
                backdropUrl = movieDetails.backdropUrl,
                addedDate = Date()
            )

            recentBrowsedMoviesDao.addBrowsedMovie(recentBrowsedMovie)
        }
    }

    fun recentBrowsedMovies(): Flow<PagingData<RecentlyBrowsedMovie>> = Pager(
        PagingConfig(pageSize = 20)
    ) {
        recentBrowsedMoviesDao.recentBrowsedMovie().asPagingSourceFactory()()
    }.flow

}