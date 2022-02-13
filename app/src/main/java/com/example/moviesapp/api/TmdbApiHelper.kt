package com.example.moviesapp.api

import com.example.moviesapp.model.*
import retrofit2.Call

interface TmdbApiHelper {

    fun getConfig(): Call<Config>

    suspend fun discoverMovies(
        page: Int,
        isoCode: String = DeviceLanguage.default.languageCode,
        region: String = DeviceLanguage.default.region,
        sortTypeParam: SortTypeParam = SortTypeParam.PopularityDesc,
        genresParam: GenresParam = GenresParam(genres = emptyList()),
        voteRange: ClosedFloatingPointRange<Float> = 0f..10f,
        fromReleaseDate: DateParam? = null,
        toReleaseDate: DateParam? = null
    ): MoviesResponse

    suspend fun getPopularMovies(
        page: Int,
        isoCode: String = DeviceLanguage.default.languageCode,
        region: String = DeviceLanguage.default.region
    ): MoviesResponse

    suspend fun getUpcomingMovies(
        page: Int,
        isoCode: String = DeviceLanguage.default.languageCode,
        region: String = DeviceLanguage.default.region
    ): MoviesResponse

    suspend fun getTopRatedMovies(
        page: Int,
        isoCode: String = DeviceLanguage.default.languageCode,
        region: String = DeviceLanguage.default.region
    ): MoviesResponse

    suspend fun getNowPlayingMovies(
        page: Int,
        isoCode: String = DeviceLanguage.default.languageCode,
        region: String = DeviceLanguage.default.region
    ): MoviesResponse

    suspend fun getTopRatedTvSeries(
        page: Int,
        isoCode: String = DeviceLanguage.default.languageCode,
        region: String = DeviceLanguage.default.region
    ): TvSeriesResponse

    suspend fun getOnTheAirTvSeries(
        page: Int,
        isoCode: String = DeviceLanguage.default.languageCode,
        region: String = DeviceLanguage.default.region
    ): TvSeriesResponse

    suspend fun getPopularTvSeries(
        page: Int,
        isoCode: String = DeviceLanguage.default.languageCode,
        region: String = DeviceLanguage.default.region
    ): TvSeriesResponse

    suspend fun getAiringTodayTvSeries(
        page: Int,
        isoCode: String = DeviceLanguage.default.languageCode,
        region: String = DeviceLanguage.default.region
    ): TvSeriesResponse

    fun getMovieDetails(
        movieId: Int,
        isoCode: String = DeviceLanguage.default.languageCode
    ): Call<MovieDetails>

    fun getTvSeriesDetails(
        tvSeriesId: Int,
        isoCode: String = DeviceLanguage.default.languageCode
    ): Call<TvSeriesDetails>

    fun getMovieCredits(
        movieId: Int,
        isoCode: String = DeviceLanguage.default.languageCode
    ): Call<Credits>

    suspend fun getSimilarMovies(
        movieId: Int,
        page: Int,
        isoCode: String = DeviceLanguage.default.languageCode,
        region: String = DeviceLanguage.default.region
    ): MoviesResponse

    suspend fun getMoviesRecommendations(
        movieId: Int,
        page: Int,
        isoCode: String = DeviceLanguage.default.languageCode,
        region: String = DeviceLanguage.default.region
    ): MoviesResponse

    suspend fun getSimilarTvSeries(
        tvSeriesId: Int,
        page: Int,
        isoCode: String = DeviceLanguage.default.languageCode,
        region: String = DeviceLanguage.default.region
    ): TvSeriesResponse

    suspend fun getTvSeriesRecommendations(
        tvSeriesId: Int,
        page: Int,
        isoCode: String = DeviceLanguage.default.languageCode,
        region: String = DeviceLanguage.default.region
    ): TvSeriesResponse

    fun getTvSeasons(
        tvSeriesId: Int,
        seasonNumber: Int,
        isoCode: String = DeviceLanguage.default.languageCode
    ): Call<TvSeasonsResponse>

    suspend fun multiSearch(
        page: Int,
        isoCode: String = DeviceLanguage.default.languageCode,
        region: String = DeviceLanguage.default.region,
        query: String,
        includeAdult: Boolean = false,
        year: Int? = null,
        releaseYear: Int? = null
    ): SearchResponse

    suspend fun getTrendingMovies(
        page: Int,
        isoCode: String = DeviceLanguage.default.languageCode,
        region: String = DeviceLanguage.default.region
    ): MoviesResponse

    suspend fun getTrendingTvSeries(
        page: Int,
        isoCode: String = DeviceLanguage.default.languageCode,
        region: String = DeviceLanguage.default.region
    ): TvSeriesResponse

    fun getSeasonDetails(
        tvSeriesId: Int,
        seasonNumber: Int,
        isoCode: String = DeviceLanguage.default.languageCode
    ): Call<SeasonDetails>

    fun getMovieImages(movieId: Int): Call<ImagesResponse>

    fun getTvSeriesImages(tvSeriesId: Int): Call<ImagesResponse>

    fun getEpisodeImages(
        tvSeriesId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): Call<ImagesResponse>

    suspend fun getMovieReviews(movieId: Int, page: Int): ReviewsResponse

    fun getMovieReview(movieId: Int): Call<ReviewsResponse>

    suspend fun getTvSeriesReviews(tvSeriesId: Int, page: Int): ReviewsResponse

    fun getTvSeriesReview(tvSeriesId: Int): Call<ReviewsResponse>

    fun getCollection(
        collectionId: Int,
        isoCode: String = DeviceLanguage.default.languageCode
    ): Call<CollectionResponse>

    fun getMoviesGenres(isoCode: String = DeviceLanguage.default.languageCode): Call<GenresResponse>

    fun getTvSeriesGenres(isoCode: String = DeviceLanguage.default.languageCode): Call<GenresResponse>

    fun getPersonDetails(
        personId: Int,
        isoCode: String = DeviceLanguage.default.languageCode
    ): Call<PersonDetails>

    fun getCombinedCredits(
        personId: Int,
        isoCode: String = DeviceLanguage.default.languageCode
    ): Call<CombinedCredits>

    fun getMovieWatchProviders(
        movieId: Int
    ): Call<MovieWatchProvidersResponse>

    fun getExternalIds(
        personId: Int,
        isoCode: String = DeviceLanguage.default.languageCode
    ): Call<ExternalIds>

}