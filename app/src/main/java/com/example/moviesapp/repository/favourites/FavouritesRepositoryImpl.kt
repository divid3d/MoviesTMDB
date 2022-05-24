package com.example.moviesapp.repository.favourites

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviesapp.db.FavouritesMoviesDao
import com.example.moviesapp.db.FavouritesTvSeriesDao
import com.example.moviesapp.model.MovieDetails
import com.example.moviesapp.model.MovieFavourite
import com.example.moviesapp.model.TvSeriesDetails
import com.example.moviesapp.model.TvSeriesFavourite
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavouritesRepositoryImpl @Inject constructor(
    private val externalScope: CoroutineScope,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val favouritesMoviesDao: FavouritesMoviesDao,
    private val favouritesTvSeriesDao: FavouritesTvSeriesDao
) : FavouritesRepository {
    override fun likeMovie(movieDetails: MovieDetails) {
        externalScope.launch(defaultDispatcher) {
            val favouriteMovie = movieDetails.run {
                MovieFavourite(
                    id = id,
                    posterPath = posterPath,
                    title = title,
                    originalTitle = originalTitle,
                    addedDate = Date()
                )
            }

            favouritesMoviesDao.likeMovie(favouriteMovie)
        }
    }

    override fun likeTvSeries(tvSeriesDetails: TvSeriesDetails) {
        externalScope.launch {
            val favouriteTvSeries = tvSeriesDetails.run {
                TvSeriesFavourite(
                    id = id,
                    posterPath = posterPath,
                    name = name,
                    addedDate = Date()
                )
            }

            favouritesTvSeriesDao.likeTvSeries(favouriteTvSeries)
        }
    }

    override fun unlikeMovie(movieDetails: MovieDetails) {
        externalScope.launch {
            favouritesMoviesDao.unlikeMovie(movieDetails.id)
        }
    }

    override fun unlikeTvSeries(tvSeriesDetails: TvSeriesDetails) {
        externalScope.launch {
            favouritesTvSeriesDao.unlikeTvSeries(tvSeriesDetails.id)
        }
    }

    override fun favouriteMovies(): Flow<PagingData<MovieFavourite>> = Pager(
        PagingConfig(pageSize = 20)
    ) {
        favouritesMoviesDao.favouriteMovies().asPagingSourceFactory()()
    }.flow

    override fun favouritesTvSeries(): Flow<PagingData<TvSeriesFavourite>> = Pager(
        PagingConfig(pageSize = 20)
    ) {
        favouritesTvSeriesDao.favouriteTvSeries().asPagingSourceFactory()()
    }.flow

    override fun getFavouritesMoviesIds(): Flow<List<Int>> =
        favouritesMoviesDao.favouriteMoviesIds().distinctUntilChanged()

    override fun getFavouriteTvSeriesIds(): Flow<List<Int>> =
        favouritesTvSeriesDao.favouriteTvSeriesIds().distinctUntilChanged()

    override fun getFavouriteMoviesCount(): Flow<Int> =
        favouritesMoviesDao.favouriteMoviesCount().distinctUntilChanged()

    override fun getFavouriteTvSeriesCount(): Flow<Int> =
        favouritesTvSeriesDao.favouriteTvSeriesCount().distinctUntilChanged()

}