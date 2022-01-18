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
            val favouriteMovie = MovieFavourite(
                id = movieDetails.id,
                backdropPath = movieDetails.backdropPath,
                posterPath = movieDetails.posterPath,
                title = movieDetails.title,
                originalTitle = movieDetails.originalTitle,
                overview = movieDetails.overview,
                voteAverage = movieDetails.voteAverage,
                voteCount = movieDetails.voteCount
            )

            favouritesMoviesDao.likeMovie(favouriteMovie)
        }
    }

    fun likeTvSeries(tvSeriesDetails: TvSeriesDetails) {
        externalScope.launch {
            val favouriteTvSeries = TvSeriesFavourite(
                id = tvSeriesDetails.id,
                backdropPath = tvSeriesDetails.backdropPath,
                posterPath = tvSeriesDetails.posterPath,
                name = tvSeriesDetails.name,
                overview = tvSeriesDetails.overview,
                voteAverage = tvSeriesDetails.voteAverage,
                voteCount = tvSeriesDetails.voteCount
            )

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

    fun getFavouritesMoviesIds() = favouritesMoviesDao.favouriteMoviesIds()

    fun getFavouriteTvSeriesIds() = favouritesTvSeriesDao.favouriteTvSeriesIds()

}