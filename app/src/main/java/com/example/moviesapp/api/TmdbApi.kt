package com.example.moviesapp.api

import com.example.moviesapp.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    @GET("configuration")
    fun getConfig(): Call<Config>

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
    fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("language") isoCode: String
    ): Call<MovieDetails>

    @GET("tv/{tv_id}")
    fun getTvSeriesDetails(
        @Path("tv_id") tvSeriesId: Int,
        @Query("language") isoCode: String
    ): Call<TvSeriesDetails>

    @GET("movie/{movie_id}/credits")
    fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("language") isoCode: String
    ): Call<Credits>

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
    fun getTvSeasons(
        @Path("tv_id") tvSeriesId: Int,
        @Path("season_number") seasonNumber: Int,
        @Query("language") isoCode: String
    ): Call<TvSeasonsResponse>

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
    fun getSeasonDetails(
        @Path("tv_id") tvSeriesId: Int,
        @Path("season_number") seasonNumber: Int,
        @Query("language") isoCode: String
    ): Call<SeasonDetails>

    @GET("movie/{movie_id}/images")
    fun getMovieImages(
        @Path("movie_id") movieId: Int
    ): Call<ImagesResponse>

    @GET("tv/{tv_id}/images")
    fun getTvSeriesImages(
        @Path("tv_id") tvSeriesId: Int
    ): Call<ImagesResponse>
}

