package com.example.moviesapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviesapp.api.TmdbApiHelper
import com.example.moviesapp.data.MovieDetailsResponseDataSource
import com.example.moviesapp.data.MovieResponseDataSource
import com.example.moviesapp.model.Credits
import com.example.moviesapp.model.ImagesResponse
import com.example.moviesapp.model.Movie
import com.example.moviesapp.model.MovieDetails
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
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

    fun trendingMovies(): Flow<PagingData<Movie>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            MovieResponseDataSource(apiHelper::getTrendingMovies)
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

    fun similarMovies(movieId: Int): Flow<PagingData<Movie>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            MovieDetailsResponseDataSource(
                movieId = movieId,
                apiHelperMethod = apiHelper::getSimilarMovies
            )
        }.flow

    fun moviesRecommendations(movieId: Int): Flow<PagingData<Movie>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            MovieDetailsResponseDataSource(
                movieId = movieId,
                apiHelperMethod = apiHelper::getMoviesRecommendations
            )
        }.flow

    fun movieDetails(movieId: Int, isoCode: String = "pl-PL"): Call<MovieDetails> =
        apiHelper.getMovieDetails(movieId, isoCode)

    fun movieCredits(movieId: Int, isoCode: String = "pl-PL"): Call<Credits> =
        apiHelper.getMovieCredits(movieId, isoCode)

    fun movieImages(
        movieId: Int
    ): Call<ImagesResponse> = apiHelper.getMovieImages(movieId)

}