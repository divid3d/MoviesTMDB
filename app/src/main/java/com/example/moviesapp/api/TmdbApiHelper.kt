package com.example.moviesapp.api

import com.example.moviesapp.model.*
import retrofit2.Call

interface TmdbApiHelper {

    fun getConfig(): Call<Config>

    suspend fun discoverMovies(page: Int, isoCode: String = "pl-PL"): MoviesResponse

    suspend fun getUpcomingMovies(page: Int, isoCode: String = "pl-PL"): MoviesResponse

    suspend fun getTopRatedMovies(page: Int, isoCode: String = "pl-PL"): MoviesResponse

    suspend fun getNowPlayingMovies(page: Int, isoCode: String = "pl-PL"): MoviesResponse

    suspend fun getTopRatedTvSeries(page: Int, isoCode: String = "pl-PL"): TvSeriesResponse

    suspend fun getOnTheAirTvSeries(page: Int, isoCode: String = "pl-PL"): TvSeriesResponse

    suspend fun getPopularTvSeries(page: Int, isoCode: String = "pl-PL"): TvSeriesResponse

    suspend fun getAiringTodayTvSeries(page: Int, isoCode: String = "pl-PL"): TvSeriesResponse

    fun getMovieDetails(movieId: Int, isoCode: String = "pl-PL"): Call<MovieDetails>

    fun getTvSeriesDetails(tvSeriesId: Int, isoCode: String = "pl-PL"): Call<TvSeriesDetails>

    fun getMovieCredits(movieId: Int, isoCode: String = "pl-PL"): Call<Credits>

    suspend fun getSimilarMovies(movieId: Int, page: Int, isoCode: String = "pl-PL"): MoviesResponse

    suspend fun getMoviesRecommendations(
        movieId: Int,
        page: Int,
        isoCode: String = "pl-PL"
    ): MoviesResponse

    suspend fun getSimilarTvSeries(
        tvSeriesId: Int,
        page: Int,
        isoCode: String = "pl-PL"
    ): TvSeriesResponse

    suspend fun getTvSeriesRecommendations(
        tvSeriesId: Int,
        page: Int,
        isoCode: String = "pl-PL"
    ): TvSeriesResponse

    fun getTvSeasons(
        tvSeriesId: Int,
        seasonNumber: Int,
        isoCode: String = "pl-PL"
    ): Call<TvSeasonsResponse>

    suspend fun multiSearch(
        page: Int,
        isoCode: String = "pl-PL",
        query: String,
        includeAdult: Boolean = false,
        year: Int? = null,
        releaseYear: Int? = null
    ): SearchResponse

    suspend fun getTrendingMovies(page: Int, isoCode: String = "pl-PL"): MoviesResponse

    suspend fun getTrendingTvSeries(page: Int, isoCode: String = "pl-PL"): TvSeriesResponse

    fun getSeasonDetails(
        tvSeriesId: Int,
        seasonNumber: Int,
        isoCode: String = "pl-PL"
    ): Call<SeasonDetails>

    fun getMovieImages(movieId: Int): Call<ImagesResponse>

    fun getTvSeriesImages(tvSeriesId: Int): Call<ImagesResponse>

    fun getEpisodeImages(
        tvSeriesId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): Call<ImagesResponse>

    suspend fun getMovieReviews(movieId: Int, page: Int): MovieReviewsResponse

    fun getMovieReview(movieId: Int): Call<MovieReviewsResponse>

}