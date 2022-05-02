package com.example.moviesapp.repository.movie

import androidx.paging.PagingData
import com.example.moviesapp.model.*
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

interface MovieRepository {
    fun discoverMovies(
        deviceLanguage: DeviceLanguage,
        sortType: SortType = SortType.Popularity,
        sortOrder: SortOrder = SortOrder.Desc,
        genresParam: GenresParam = GenresParam(genres = emptyList()),
        watchProvidersParam: WatchProvidersParam = WatchProvidersParam(watchProviders = emptyList()),
        voteRange: ClosedFloatingPointRange<Float> = 0f..10f,
        onlyWithPosters: Boolean = false,
        onlyWithScore: Boolean = false,
        onlyWithOverview: Boolean = false,
        releaseDateRange: DateRange = DateRange()
    ): Flow<PagingData<Movie>>

    fun popularMovies(
        deviceLanguage: DeviceLanguage = DeviceLanguage.default
    ): Flow<PagingData<MovieEntity>>

    fun upcomingMovies(
        deviceLanguage: DeviceLanguage = DeviceLanguage.default
    ): Flow<PagingData<MovieEntity>>

    fun trendingMovies(
        deviceLanguage: DeviceLanguage = DeviceLanguage.default
    ): Flow<PagingData<MovieEntity>>

    fun topRatedMovies(
        deviceLanguage: DeviceLanguage = DeviceLanguage.default
    ): Flow<PagingData<MovieEntity>>

    fun nowPlayingMovies(
        deviceLanguage: DeviceLanguage = DeviceLanguage.default
    ): Flow<PagingData<MovieDetailEntity>>

    fun similarMovies(
        movieId: Int,
        deviceLanguage: DeviceLanguage = DeviceLanguage.default
    ): Flow<PagingData<Movie>>

    fun moviesRecommendations(
        movieId: Int,
        deviceLanguage: DeviceLanguage = DeviceLanguage.default
    ): Flow<PagingData<Movie>>

    fun movieDetails(
        movieId: Int,
        isoCode: String = DeviceLanguage.default.languageCode
    ): Call<MovieDetails>

    fun movieCredits(
        movieId: Int,
        isoCode: String = DeviceLanguage.default.languageCode
    ): Call<Credits>

    fun movieImages(
        movieId: Int
    ): Call<ImagesResponse>

    fun movieReviews(movieId: Int): Flow<PagingData<Review>>

    fun movieReview(movieId: Int): Call<ReviewsResponse>

    fun collection(
        collectionId: Int,
        isoCode: String = DeviceLanguage.default.languageCode
    ): Call<CollectionResponse>

    fun watchProviders(movieId: Int): Call<WatchProvidersResponse>

    fun getExternalIds(movieId: Int): Call<ExternalIds>

    fun getMovieVideos(
        movieId: Int,
        isoCode: String = DeviceLanguage.default.languageCode
    ): Call<VideosResponse>

    fun moviesOfDirector(
        directorId: Int,
        deviceLanguage: DeviceLanguage = DeviceLanguage.default
    ): Flow<PagingData<Movie>>

}