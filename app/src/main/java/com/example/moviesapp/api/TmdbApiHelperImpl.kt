package com.example.moviesapp.api

import com.example.moviesapp.model.*
import retrofit2.Call
import javax.inject.Inject

class TmdbApiHelperImpl @Inject constructor(
    private val tmdbApi: TmdbApi
) : TmdbApiHelper {
    override fun getConfig(): Call<Config> = tmdbApi.getConfig()

    override suspend fun discoverMovies(
        page: Int,
        isoCode: String,
        sortType: SortTypeParam
    ): MoviesResponse =
        tmdbApi.discoverMovies(page, isoCode, sortType)

    override suspend fun getPopularMovies(page: Int, isoCode: String): MoviesResponse =
        tmdbApi.getPopularMovies(page, isoCode)

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

    override fun getMovieDetails(movieId: Int, isoCode: String): Call<MovieDetails> =
        tmdbApi.getMovieDetails(movieId, isoCode)

    override fun getTvSeriesDetails(tvSeriesId: Int, isoCode: String): Call<TvSeriesDetails> =
        tmdbApi.getTvSeriesDetails(tvSeriesId, isoCode)

    override fun getMovieCredits(movieId: Int, isoCode: String): Call<Credits> =
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

    override fun getTvSeasons(
        tvSeriesId: Int,
        seasonNumber: Int,
        isoCode: String
    ): Call<TvSeasonsResponse> =
        tmdbApi.getTvSeasons(tvSeriesId, seasonNumber, isoCode)

    override suspend fun multiSearch(
        page: Int,
        isoCode: String,
        query: String,
        includeAdult: Boolean,
        year: Int?,
        releaseYear: Int?
    ): SearchResponse = tmdbApi.multiSearch(
        page = page,
        isoCode = isoCode,
        query = query,
        includeAdult = includeAdult,
        year = year,
        releaseYear = releaseYear
    )

    override suspend fun getTrendingMovies(page: Int, isoCode: String): MoviesResponse =
        tmdbApi.getTrendingMovies(page, isoCode)

    override suspend fun getTrendingTvSeries(page: Int, isoCode: String): TvSeriesResponse =
        tmdbApi.getTrendingTvSeries(page, isoCode)

    override fun getSeasonDetails(
        tvSeriesId: Int,
        seasonNumber: Int,
        isoCode: String
    ): Call<SeasonDetails> =
        tmdbApi.getSeasonDetails(tvSeriesId, seasonNumber, isoCode)

    override fun getMovieImages(
        movieId: Int
    ): Call<ImagesResponse> = tmdbApi.getMovieImages(movieId)

    override fun getTvSeriesImages(
        tvSeriesId: Int
    ): Call<ImagesResponse> = tmdbApi.getTvSeriesImages(tvSeriesId)

    override fun getEpisodeImages(
        tvSeriesId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): Call<ImagesResponse> = tmdbApi.getEpisodeImages(tvSeriesId, seasonNumber, episodeNumber)

    override suspend fun getMovieReviews(
        movieId: Int,
        page: Int
    ): ReviewsResponse = tmdbApi.getMovieReviews(movieId, page)

    override fun getMovieReview(
        movieId: Int
    ): Call<ReviewsResponse> = tmdbApi.getMovieReview(movieId)

    override suspend fun getTvSeriesReviews(
        tvSeriesId: Int,
        page: Int
    ): ReviewsResponse = tmdbApi.getTvSeriesReviews(tvSeriesId, page)

    override fun getTvSeriesReview(
        tvSeriesId: Int
    ): Call<ReviewsResponse> = tmdbApi.getTvSeriesReview(tvSeriesId)

    override fun getCollection(collectionId: Int, isoCode: String): Call<CollectionResponse> =
        tmdbApi.getCollection(collectionId, isoCode)
}