package com.example.moviesapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviesapp.api.TmdbApiHelper
import com.example.moviesapp.data.MovieResponseDataSource
import com.example.moviesapp.model.Credits
import com.example.moviesapp.model.Movie
import com.example.moviesapp.model.MovieDetails
import com.example.moviesapp.model.MoviesResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val apiHelper: TmdbApiHelper
) {
    fun discoverMovies(): Flow<PagingData<Movie>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            MovieResponseDataSource(apiHelper::discoverMovies)
        }.flow

    fun upcomingMovies(): Flow<PagingData<Movie>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            MovieResponseDataSource(apiHelper::getUpcomingMovies)
        }.flow

    fun topRatedMovies(): Flow<PagingData<Movie>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            MovieResponseDataSource(apiHelper::getTopRatedMovies)
        }.flow

    fun nowPlayingMovies(): Flow<PagingData<Movie>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            MovieResponseDataSource(apiHelper::getNowPlayingMovies)
        }.flow

    suspend fun movieDetails(movieId: Int, isoCode: String = "pl-PL"): MovieDetails =
        apiHelper.getMovieDetails(movieId, isoCode)

    suspend fun movieCredits(movieId: Int, isoCode: String = "pl-PL"): Credits =
        apiHelper.getMovieCredits(movieId, isoCode)

    suspend fun similarMovies(
        movieId: Int,
        page: Int = 1,
        isoCode: String = "pl-PL"
    ): MoviesResponse =
        apiHelper.getSimilarMovies(movieId, page, isoCode)

    suspend fun moviesRecommendations(
        movieId: Int,
        page: Int = 1,
        isoCode: String = "pl-PL"
    ): MoviesResponse =
        apiHelper.getMoviesRecommendations(movieId, page, isoCode)

}