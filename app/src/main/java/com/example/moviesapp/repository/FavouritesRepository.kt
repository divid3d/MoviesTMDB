package com.example.moviesapp.repository

import androidx.paging.DataSource
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviesapp.db.FavouritesMoviesDao
import com.example.moviesapp.model.MovieDetails
import com.example.moviesapp.model.MovieFavourite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavouritesRepository @Inject constructor(
    private val externalScope: CoroutineScope,
    private val favouritesMoviesDao: FavouritesMoviesDao,
    private val favouriteMoviesDataSource: DataSource.Factory<Int, MovieFavourite>
) {
    fun likeMovie(movieDetails: MovieDetails) {
        externalScope.launch {
            val favouriteMovies = MovieFavourite(
                id = movieDetails.id,
                backdropPath = movieDetails.backdropPath,
                posterPath = movieDetails.posterPath,
                title = movieDetails.title,
                originalTitle = movieDetails.originalTitle,
                overview = movieDetails.overview,
                voteAverage = movieDetails.voteAverage,
                voteCount = movieDetails.voteCount
            )

            favouritesMoviesDao.likeMovie(favouriteMovies)
        }
    }

    fun unlikeMovie(movieDetails: MovieDetails) {
        externalScope.launch {
            favouritesMoviesDao.unlikeMovie(movieDetails.id)
        }
    }

    fun getFavouritesMoviesDataSource(): Flow<PagingData<MovieFavourite>> = Pager(
        PagingConfig(pageSize = 20)
    ) {
        favouriteMoviesDataSource.asPagingSourceFactory()()
    }.flow

    fun getFavouritesMoviesIds() = favouritesMoviesDao.favouriteMoviesIds()

    suspend fun exists(movieId: Int) = favouritesMoviesDao.exists(movieId)

}