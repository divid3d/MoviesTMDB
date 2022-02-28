package com.example.moviesapp.repository.movie

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviesapp.api.TmdbApiHelper
import com.example.moviesapp.data.DiscoverMoviesDataSource
import com.example.moviesapp.data.MovieDetailsResponseDataSource
import com.example.moviesapp.data.MovieResponseDataSource
import com.example.moviesapp.data.ReviewsDataSource
import com.example.moviesapp.model.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Call
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val apiHelper: TmdbApiHelper
) : MovieRepository {
    override fun discoverMovies(
        deviceLanguage: DeviceLanguage,
        sortType: SortType,
        sortOrder: SortOrder,
        genresParam: GenresParam,
        watchProvidersParam: WatchProvidersParam,
        voteRange: ClosedFloatingPointRange<Float>,
        onlyWithPosters: Boolean,
        onlyWithScore: Boolean,
        onlyWithOverview: Boolean,
        releaseDateRange: DateRange
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
                releaseDateRange = releaseDateRange
            )
        }.flow.flowOn(defaultDispatcher)

    override fun popularMovies(deviceLanguage: DeviceLanguage): Flow<PagingData<Movie>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            MovieResponseDataSource(
                language = deviceLanguage.languageCode,
                region = deviceLanguage.region,
                apiHelperMethod = apiHelper::getPopularMovies
            )
        }.flow.flowOn(defaultDispatcher)

    override fun upcomingMovies(deviceLanguage: DeviceLanguage): Flow<PagingData<Movie>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            MovieResponseDataSource(
                language = deviceLanguage.languageCode,
                region = deviceLanguage.region,
                apiHelperMethod = apiHelper::getUpcomingMovies
            )
        }.flow.flowOn(defaultDispatcher)

    override fun trendingMovies(deviceLanguage: DeviceLanguage): Flow<PagingData<Movie>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            MovieResponseDataSource(
                language = deviceLanguage.languageCode,
                region = deviceLanguage.region,
                apiHelperMethod = apiHelper::getTrendingMovies
            )
        }.flow.flowOn(defaultDispatcher)

    override fun topRatedMovies(deviceLanguage: DeviceLanguage): Flow<PagingData<Movie>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            MovieResponseDataSource(
                language = deviceLanguage.languageCode,
                region = deviceLanguage.region,
                apiHelperMethod = apiHelper::getTopRatedMovies
            )
        }.flow.flowOn(defaultDispatcher)

    override fun nowPlayingMovies(deviceLanguage: DeviceLanguage): Flow<PagingData<Movie>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            MovieResponseDataSource(
                language = deviceLanguage.languageCode,
                region = deviceLanguage.region,
                apiHelperMethod = apiHelper::getNowPlayingMovies
            )
        }.flow.flowOn(defaultDispatcher)

    override fun similarMovies(
        movieId: Int,
        deviceLanguage: DeviceLanguage
    ): Flow<PagingData<Movie>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            MovieDetailsResponseDataSource(
                movieId = movieId,
                language = deviceLanguage.languageCode,
                apiHelperMethod = apiHelper::getSimilarMovies
            )
        }.flow.flowOn(defaultDispatcher)

    override fun moviesRecommendations(
        movieId: Int,
        deviceLanguage: DeviceLanguage
    ): Flow<PagingData<Movie>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            MovieDetailsResponseDataSource(
                movieId = movieId,
                language = deviceLanguage.languageCode,
                apiHelperMethod = apiHelper::getMoviesRecommendations
            )
        }.flow.flowOn(defaultDispatcher)

    override fun movieDetails(
        movieId: Int,
        isoCode: String
    ): Call<MovieDetails> = apiHelper.getMovieDetails(movieId, isoCode)

    override fun movieCredits(
        movieId: Int,
        isoCode: String
    ): Call<Credits> = apiHelper.getMovieCredits(movieId, isoCode)

    override fun movieImages(
        movieId: Int
    ): Call<ImagesResponse> = apiHelper.getMovieImages(movieId)

    override fun movieReviews(movieId: Int): Flow<PagingData<Review>> =
        Pager(
            PagingConfig(pageSize = 5)
        ) {
            ReviewsDataSource(
                mediaId = movieId,
                apiHelperMethod = apiHelper::getMovieReviews
            )
        }.flow.flowOn(defaultDispatcher)

    override fun movieReview(movieId: Int): Call<ReviewsResponse> =
        apiHelper.getMovieReview(movieId)

    override fun collection(collectionId: Int, isoCode: String): Call<CollectionResponse> =
        apiHelper.getCollection(collectionId, isoCode)

    override fun watchProviders(movieId: Int): Call<WatchProvidersResponse> =
        apiHelper.getMovieWatchProviders(movieId)

    override fun getExternalIds(movieId: Int) = apiHelper.getMovieExternalIds(movieId)

    override fun getMovieVideos(
        movieId: Int,
        isoCode: String
    ) = apiHelper.getMovieVideos(movieId, isoCode)

}