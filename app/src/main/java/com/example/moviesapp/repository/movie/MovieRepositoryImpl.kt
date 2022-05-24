package com.example.moviesapp.repository.movie

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviesapp.api.TmdbApiHelper
import com.example.moviesapp.data.*
import com.example.moviesapp.db.AppDatabase
import com.example.moviesapp.model.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val apiHelper: TmdbApiHelper,
    private val appDatabase: AppDatabase
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
                deviceLanguage = deviceLanguage,
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

    override fun popularMovies(deviceLanguage: DeviceLanguage): Flow<PagingData<MovieEntity>> =
        Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = MoviesRemoteMediator(
                deviceLanguage = deviceLanguage,
                apiHelper = apiHelper,
                appDatabase = appDatabase,
                type = MovieEntityType.Popular
            ),
            pagingSourceFactory = {
                appDatabase.movieDao().getAllMovies(
                    type = MovieEntityType.Popular,
                    language = deviceLanguage.languageCode
                )
            }
        ).flow.flowOn(defaultDispatcher)

    override fun upcomingMovies(deviceLanguage: DeviceLanguage): Flow<PagingData<MovieEntity>> =
        Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = MoviesRemoteMediator(
                deviceLanguage = deviceLanguage,
                apiHelper = apiHelper,
                appDatabase = appDatabase,
                type = MovieEntityType.Upcoming
            ),
            pagingSourceFactory = {
                appDatabase.movieDao().getAllMovies(
                    type = MovieEntityType.Upcoming,
                    language = deviceLanguage.languageCode
                )
            }
        ).flow.flowOn(defaultDispatcher)

    override fun trendingMovies(deviceLanguage: DeviceLanguage): Flow<PagingData<MovieEntity>> =
        Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = MoviesRemoteMediator(
                deviceLanguage = deviceLanguage,
                apiHelper = apiHelper,
                appDatabase = appDatabase,
                type = MovieEntityType.Trending
            ),
            pagingSourceFactory = {
                appDatabase.movieDao().getAllMovies(
                    type = MovieEntityType.Trending,
                    language = deviceLanguage.languageCode
                )
            }
        ).flow.flowOn(defaultDispatcher)

    override fun topRatedMovies(deviceLanguage: DeviceLanguage): Flow<PagingData<MovieEntity>> =
        Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = MoviesRemoteMediator(
                deviceLanguage = deviceLanguage,
                apiHelper = apiHelper,
                appDatabase = appDatabase,
                type = MovieEntityType.TopRated
            ),
            pagingSourceFactory = {
                appDatabase.movieDao().getAllMovies(
                    type = MovieEntityType.TopRated,
                    language = deviceLanguage.languageCode
                )
            }
        ).flow.flowOn(defaultDispatcher)

    override fun nowPlayingMovies(deviceLanguage: DeviceLanguage): Flow<PagingData<MovieDetailEntity>> =
        Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = MoviesDetailsRemoteMediator(
                deviceLanguage = deviceLanguage,
                apiHelper = apiHelper,
                appDatabase = appDatabase
            ),
            pagingSourceFactory = {
                appDatabase.moviesDetailsDao().getAllMovies(language = deviceLanguage.languageCode)
            }
        ).flow.flowOn(defaultDispatcher)

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

    override fun moviesOfDirector(
        directorId: Int,
        deviceLanguage: DeviceLanguage
    ): Flow<PagingData<Movie>> = Pager(
        PagingConfig(pageSize = 20)
    ) {
        DirectorOtherMoviesDataSource(
            apiHelper = apiHelper,
            language = deviceLanguage.languageCode,
            region = deviceLanguage.region,
            directorId = directorId
        )
    }.flow.flowOn(defaultDispatcher)
}