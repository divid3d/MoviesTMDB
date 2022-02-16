package com.example.moviesapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviesapp.api.TmdbApiHelper
import com.example.moviesapp.data.DiscoverMoviesDataSource
import com.example.moviesapp.data.MovieDetailsResponseDataSource
import com.example.moviesapp.data.MovieResponseDataSource
import com.example.moviesapp.data.ReviewsDataSource
import com.example.moviesapp.model.*
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val crashlytics: FirebaseCrashlytics,
    private val apiHelper: TmdbApiHelper
) {
    fun discoverMovies(
        deviceLanguage: DeviceLanguage = DeviceLanguage.default,
        sortType: SortType = SortType.Popularity,
        sortOrder: SortOrder = SortOrder.Desc,
        genresParam: GenresParam = GenresParam(genres = emptyList()),
        watchProvidersParam: WatchProvidersParam = WatchProvidersParam(watchProviders = emptyList()),
        voteRange: ClosedFloatingPointRange<Float> = 0f..10f,
        onlyWithPosters: Boolean = false,
        onlyWithScore: Boolean = false,
        onlyWithOverview: Boolean = false,
        releaseDateRange: DateRange = DateRange()
    ): Flow<PagingData<Movie>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            DiscoverMoviesDataSource(
                apiHelper = apiHelper,
                language = deviceLanguage.languageCode,
                region = deviceLanguage.region,
                sortType = sortType,
                sortOrder = sortOrder,
                genresParam = genresParam,
                watchProvidersParam = watchProvidersParam,
                voteRange = voteRange,
                onlyWithPosters = onlyWithPosters,
                onlyWithScore = onlyWithScore,
                onlyWithOverview = onlyWithOverview,
                releaseDateRange = releaseDateRange,
                firebaseCrashlytics = crashlytics
            )
        }.flow.flowOn(defaultDispatcher)

    fun popularMovies(deviceLanguage: DeviceLanguage = DeviceLanguage.default): Flow<PagingData<Movie>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            MovieResponseDataSource(
                language = deviceLanguage.languageCode,
                region = deviceLanguage.region,
                apiHelperMethod = apiHelper::getPopularMovies,
                firebaseCrashlytics = crashlytics
            )
        }.flow.flowOn(defaultDispatcher)

    fun upcomingMovies(deviceLanguage: DeviceLanguage = DeviceLanguage.default): Flow<PagingData<Movie>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            MovieResponseDataSource(
                language = deviceLanguage.languageCode,
                region = deviceLanguage.region,
                apiHelperMethod = apiHelper::getUpcomingMovies,
                firebaseCrashlytics = crashlytics
            )
        }.flow.flowOn(defaultDispatcher)

    fun trendingMovies(deviceLanguage: DeviceLanguage = DeviceLanguage.default): Flow<PagingData<Movie>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            MovieResponseDataSource(
                language = deviceLanguage.languageCode,
                region = deviceLanguage.region,
                apiHelperMethod = apiHelper::getTrendingMovies,
                firebaseCrashlytics = crashlytics
            )
        }.flow.flowOn(defaultDispatcher)

    fun topRatedMovies(deviceLanguage: DeviceLanguage = DeviceLanguage.default): Flow<PagingData<Movie>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            MovieResponseDataSource(
                language = deviceLanguage.languageCode,
                region = deviceLanguage.region,
                apiHelperMethod = apiHelper::getTopRatedMovies,
                firebaseCrashlytics = crashlytics
            )
        }.flow.flowOn(defaultDispatcher)

    fun nowPlayingMovies(deviceLanguage: DeviceLanguage = DeviceLanguage.default): Flow<PagingData<Movie>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            MovieResponseDataSource(
                language = deviceLanguage.languageCode,
                region = deviceLanguage.region,
                apiHelperMethod = apiHelper::getNowPlayingMovies,
                firebaseCrashlytics = crashlytics
            )
        }.flow.flowOn(defaultDispatcher)

    fun similarMovies(
        movieId: Int,
        deviceLanguage: DeviceLanguage = DeviceLanguage.default
    ): Flow<PagingData<Movie>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            MovieDetailsResponseDataSource(
                movieId = movieId,
                language = deviceLanguage.languageCode,
                apiHelperMethod = apiHelper::getSimilarMovies,
                firebaseCrashlytics = crashlytics
            )
        }.flow.flowOn(defaultDispatcher)

    fun moviesRecommendations(
        movieId: Int,
        deviceLanguage: DeviceLanguage = DeviceLanguage.default
    ): Flow<PagingData<Movie>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            MovieDetailsResponseDataSource(
                movieId = movieId,
                language = deviceLanguage.languageCode,
                apiHelperMethod = apiHelper::getMoviesRecommendations,
                firebaseCrashlytics = crashlytics
            )
        }.flow.flowOn(defaultDispatcher)

    fun movieDetails(
        movieId: Int,
        isoCode: String = DeviceLanguage.default.languageCode
    ): Call<MovieDetails> =
        apiHelper.getMovieDetails(movieId, isoCode)

    fun movieCredits(
        movieId: Int,
        isoCode: String = DeviceLanguage.default.languageCode
    ): Call<Credits> =
        apiHelper.getMovieCredits(movieId, isoCode)

    fun movieImages(
        movieId: Int
    ): Call<ImagesResponse> = apiHelper.getMovieImages(movieId)

    fun movieReviews(movieId: Int): Flow<PagingData<Review>> =
        Pager(
            PagingConfig(pageSize = 5)
        ) {
            ReviewsDataSource(
                mediaId = movieId,
                apiHelperMethod = apiHelper::getMovieReviews,
                firebaseCrashlytics = crashlytics
            )
        }.flow.flowOn(defaultDispatcher)

    fun movieReview(movieId: Int): Call<ReviewsResponse> = apiHelper.getMovieReview(movieId)

    fun collection(
        collectionId: Int,
        isoCode: String = DeviceLanguage.default.languageCode
    ): Call<CollectionResponse> =
        apiHelper.getCollection(collectionId, isoCode)

    fun watchProviders(movieId: Int): Call<WatchProvidersResponse> =
        apiHelper.getMovieWatchProviders(movieId)

    fun getExternalIds(movieId: Int) = apiHelper.getMovieExternalIds(movieId)

}