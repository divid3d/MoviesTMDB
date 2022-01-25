package com.example.moviesapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviesapp.db.FavouritesMoviesDao
import com.example.moviesapp.db.FavouritesTvSeriesDao
import com.example.moviesapp.model.MovieDetails
import com.example.moviesapp.model.MovieFavourite
import com.example.moviesapp.model.TvSeriesDetails
import com.example.moviesapp.model.TvSeriesFavourite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavouritesRepository @Inject constructor(
    private val externalScope: CoroutineScope,
    private val favouritesMoviesDao: FavouritesMoviesDao,
    private val favouritesTvSeriesDao: FavouritesTvSeriesDao
) {
    fun likeMovie(movieDetails: MovieDetails) {
        externalScope.launch {
            val favouriteMovie = movieDetails.run {
                MovieFavourite(
                    id = id,
                    backdropPath = backdropPath,
                    posterPath = posterPath,
                    title = title,
                    originalTitle = originalTitle,
                    overview = overview,
                    voteAverage = voteAverage,
                    voteCount = voteCount,
                    addedDate = Date()
                )
            }

            favouritesMoviesDao.likeMovie(favouriteMovie)
        }
    }

    fun likeTvSeries(tvSeriesDetails: TvSeriesDetails) {
        externalScope.launch {
            val favouriteTvSeries = tvSeriesDetails.run {
                TvSeriesFavourite(
                    id = id,
                    backdropPath = backdropPath,
                    posterPath = posterPath,
                    name = name,
                    overview = overview,
                    voteAverage = voteAverage,
                    voteCount = voteCount,
                    addedDate = Date()
                )
            }

            favouritesTvSeriesDao.likeTvSeries(favouriteTvSeries)
        }
    }

    fun unlikeMovie(movieDetails: MovieDetails) {
        externalScope.launch {
            favouritesMoviesDao.unlikeMovie(movieDetails.id)
        }
    }

    fun unlikeTvSeries(tvSeriesDetails: TvSeriesDetails) {
        externalScope.launch {
            favouritesTvSeriesDao.unlikeTvSeries(tvSeriesDetails.id)
        }
    }

    fun favouriteMovies(): Flow<PagingData<MovieFavourite>> = Pager(
        PagingConfig(pageSize = 20)
    ) {
        favouritesMoviesDao.favouriteMovies().asPagingSourceFactory()()
    }.flow

    fun favouritesTvSeries(): Flow<PagingData<TvSeriesFavourite>> = Pager(
        PagingConfig(pageSize = 20)
    ) {
        favouritesTvSeriesDao.favouriteTvSeries().asPagingSourceFactory()()
    }.flow

    fun getFavouritesMoviesIds(): Flow<List<Int>> = favouritesMoviesDao.favouriteMoviesIds()

    fun getFavouriteTvSeriesIds(): Flow<List<Int>> = favouritesTvSeriesDao.favouriteTvSeriesIds()

    fun getFavouriteMoviesCount(): Flow<Int> = favouritesMoviesDao.favouriteMoviesCount()

    fun getFavouriteTvSeriesCount(): Flow<Int> = favouritesTvSeriesDao.favouriteTvSeriesCount()

}