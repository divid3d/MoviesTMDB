package com.example.moviesapp.api

import com.example.moviesapp.model.*
import javax.inject.Inject

class TmdbApiHelperImpl @Inject constructor(
    private val tmdbApi: TmdbApi
) : TmdbApiHelper {
    override suspend fun getConfig(): Config = tmdbApi.getConfig()

    override suspend fun discoverMovies(page: Int, isoCode: String): MoviesResponse =
        tmdbApi.discoverMovies(page, isoCode)

    override suspend fun getUpcomingMovies(page: Int, isoCode: String): MoviesResponse =
        tmdbApi.getUpcomingMovies(page, isoCode)

    override suspend fun getTopRatedMovies(page: Int, isoCode: String): MoviesResponse =
        tmdbApi.getTopRatedMovies(page, isoCode)

    override suspend fun getNowPlayingMovies(page: Int, isoCode: String): MoviesResponse =
        tmdbApi.getNowPlayingMovies(page, isoCode)

    override suspend fun getTopRatedTvSeries(page: Int, isoCode: String): TvSeriesResponse =
        tmdbApi.getTopRatedTvSeries(page, isoCode)

    override suspend fun getOnTheAirTvSeries(page: Int, isoCode: String): TvSeriesResponse =
        tmdbApi.getOnTheAirTvSeries(page, isoCode)

    override suspend fun getPopularTvSeries(page: Int, isoCode: String): TvSeriesResponse =
        tmdbApi.getPopularTvSeries(page, isoCode)

    override suspend fun getAiringTodayTvSeries(page: Int, isoCode: String): TvSeriesResponse =
        tmdbApi.getAiringTodayTvSeries(page, isoCode)

    override suspend fun getMovieDetails(movieId: Int, isoCode: String): MovieDetails =
        tmdbApi.getMovieDetails(movieId, isoCode)

    override suspend fun getTvSeriesDetails(tvSeriesId: Int, isoCode: String): TvSeriesDetails =
        tmdbApi.getTvSeriesDetails(tvSeriesId, isoCode)

    override suspend fun getMovieCredits(movieId: Int, isoCode: String): Credits =
        tmdbApi.getMovieCredits(movieId, isoCode)

    override suspend fun getSimilarMovies(
        movieId: Int,
        page: Int,
        isoCode: String
    ): MoviesResponse =
        tmdbApi.getSimilarMovies(movieId, page, isoCode)

    override suspend fun getMoviesRecommendations(
        movieId: Int,
        page: Int,
        isoCode: String
    ): MoviesResponse =
        tmdbApi.getMoviesRecommendations(movieId, page, isoCode)

    override suspend fun getSimilarTvSeries(
        tvSeriesId: Int,
        page: Int,
        isoCode: String
    ): TvSeriesResponse =
        tmdbApi.getSimilarTvSeries(tvSeriesId, page, isoCode)

    override suspend fun getTvSeriesRecommendations(
        tvSeriesId: Int,
        page: Int,
        isoCode: String
    ): TvSeriesResponse =
        tmdbApi.getTvSeriesRecommendations(tvSeriesId, page, isoCode)

    override suspend fun getTvSeasons(
        tvSeriesId: Int,
        seasonNumber: Int,
        isoCode: String
    ): TvSeasonsResponse =
        tmdbApi.getTvSeasons(tvSeriesId, seasonNumber, isoCode)

}