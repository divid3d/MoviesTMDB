package com.example.moviesapp.api

import com.example.moviesapp.model.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    @GET("configuration")
    suspend fun getConfig(): Config

    @GET("discover/movie")
    suspend fun discoverMovies(
        @Query("page") page: Int,
        @Query("language") isoCode: String
    ): MoviesResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("page") page: Int,
        @Query("language") isoCode: String
    ): MoviesResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int,
        @Query("language") isoCode: String
    ): MoviesResponse

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int,
        @Query("language") isoCode: String
    ): MoviesResponse

    @GET("tv/top_rated")
    suspend fun getTopRatedTvSeries(
        @Query("page") page: Int,
        @Query("language") isoCode: String
    ): TvSeriesResponse

    @GET("tv/on_the_air")
    suspend fun getOnTheAirTvSeries(
        @Query("page") page: Int,
        @Query("language") isoCode: String
    ): TvSeriesResponse

    @GET("tv/popular")
    suspend fun getPopularTvSeries(
        @Query("page") page: Int,
        @Query("language") isoCode: String
    ): TvSeriesResponse

    @GET("tv/airing_today")
    suspend fun getAiringTodayTvSeries(
        @Query("page") page: Int,
        @Query("language") isoCode: String
    ): TvSeriesResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("language") isoCode: String
    ): MovieDetails

    @GET("tv/{tv_id}")
    suspend fun getTvSeriesDetails(
        @Path("tv_id") tvSeriesId: Int,
        @Query("language") isoCode: String
    ): TvSeriesDetails

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("language") isoCode: String
    ): Credits

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: Int,
        @Query("page") page: Int,
        @Query("language") isoCode: String
    ): MoviesResponse

    @GET("tv/{tv_id}/similar")
    suspend fun getSimilarTvSeries(
        @Path("tv_id") tvSeriesId: Int,
        @Query("page") page: Int,
        @Query("language") isoCode: String
    ): TvSeriesResponse

    @GET("movie/{movie_id}/recommendations")
    suspend fun getMoviesRecommendations(
        @Path("movie_id") movieId: Int,
        @Query("page") page: Int,
        @Query("language") isoCode: String
    ): MoviesResponse


    @GET("tv/{tv_id}/recommendations")
    suspend fun getTvSeriesRecommendations(
        @Path("tv_id") tvSeriesId: Int,
        @Query("page") page: Int,
        @Query("language") isoCode: String
    ): TvSeriesResponse

    @GET("tv/{tv_id}/season/{season_number}")
    suspend fun getTvSeasons(
        @Path("tv_id") tvSeriesId: Int,
        @Path("season_number") seasonNumber: Int,
        @Query("language") isoCode: String
    ): TvSeasonsResponse

    @GET("search/multi")
    suspend fun multiSearch(
        @Query("page") page: Int,
        @Query("language") isoCode: String,
        @Query("query") query: String,
        @Query("year") year: Int?,
        @Query("include_adult") includeAdult: Boolean,
        @Query("primary_release_year") releaseYear: Int?
    ): SearchResponse

    @GET("trending/movie/week")
    suspend fun getTrendingMovies(
        @Query("page") page: Int,
        @Query("language") isoCode: String
    ): MoviesResponse

    @GET("trending/tv/week")
    suspend fun getTrendingTvSeries(
        @Query("page") page: Int,
        @Query("language") isoCode: String
    ): TvSeriesResponse

    @GET("tv/{tv_id}/season/{season_number}")
    suspend fun getSeasonDetails(
        @Path("tv_id") tvSeriesId: Int,
        @Path("season_number") seasonNumber: Int,
        @Query("language") isoCode: String
    ): SeasonDetails

    @GET("movie/{movie_id}/images")
    suspend fun getMovieImages(
        @Path("movie_id") movieId: Int
    ): ImagesResponse

    @GET("tv/{tv_id}/images")
    suspend fun getTvSeriesImages(
        @Path("tv_id") tvSeriesId: Int
    ): ImagesResponse
}

