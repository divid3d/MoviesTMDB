package com.example.moviesapp.api

import com.example.moviesapp.model.*

interface TmdbApiHelper {

    suspend fun getConfig(): Config

    suspend fun discoverMovies(page: Int, isoCode: String = "pl-PL"): MoviesResponse

    suspend fun getUpcomingMovies(page: Int, isoCode: String = "pl-PL"): MoviesResponse

    suspend fun getTopRatedMovies(page: Int, isoCode: String = "pl-PL"): MoviesResponse

    suspend fun getNowPlayingMovies(page: Int, isoCode: String = "pl-PL"): MoviesResponse

    suspend fun getTopRatedTvSeries(page: Int, isoCode: String = "pl-PL"): TvSeriesResponse

    suspend fun getOnTheAirTvSeries(page: Int, isoCode: String = "pl-PL"): TvSeriesResponse

    suspend fun getPopularTvSeries(page: Int, isoCode: String = "pl-PL"): TvSeriesResponse

    suspend fun getAiringTodayTvSeries(page: Int, isoCode: String = "pl-PL"): TvSeriesResponse

    suspend fun getMovieDetails(movieId: Int, isoCode: String = "pl-PL"): MovieDetails

    suspend fun getTvSeriesDetails(tvSeriesId: Int, isoCode: String = "pl-PL"): TvSeriesDetails

    suspend fun getMovieCredits(movieId: Int, isoCode: String = "pl-PL"): Credits

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

    suspend fun getTvSeasons(
        tvSeriesId: Int,
        seasonNumber: Int,
        isoCode: String = "pl-PL"
    ): TvSeasonsResponse

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

    suspend fun getSeasonDetails(
        tvSeriesId: Int,
        seasonNumber: Int,
        isoCode: String = "pl-PL"
    ): SeasonDetails

    suspend fun getMovieImages(
        movieId: Int
    ): ImagesResponse

    suspend fun getTvSeriesImages(
        movieId: Int
    ): ImagesResponse

}