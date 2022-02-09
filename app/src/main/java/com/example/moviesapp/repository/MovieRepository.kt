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
    private val apiHelper: TmdbApiHelper
) {
    fun discoverMovies(
        sortType: SortType = SortType.Popularity,
        sortOrder: SortOrder = SortOrder.Desc,
        genresParam: GenresParam = GenresParam(genres = emptyList()),
        voteRange: ClosedFloatingPointRange<Float> = 0f..10f,
        onlyWithPosters: Boolean = false,
        onlyWithScore: Boolean = false,
        onlyWithOverview: Boolean = false,
        releaseDateRange: ReleaseDateRange = ReleaseDateRange()
    ): Flow<PagingData<Movie>> =
        Pager(
            PagingConfig(pageSize = 30)
        ) {
            DiscoverMoviesDataSource(
                apiHelper = apiHelper,
                sortType = sortType,
                sortOrder = sortOrder,
                genresParam = genresParam,
                voteRange = voteRange,
                onlyWithPosters = onlyWithPosters,
                onlyWithScore = onlyWithScore,
                onlyWithOverview = onlyWithOverview,
                releaseDateRange = releaseDateRange
            )
        }.flow.flowOn(defaultDispatcher)

    fun popularMovies(): Flow<PagingData<Movie>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            MovieResponseDataSource(apiHelper::getPopularMovies)
        }.flow.flowOn(defaultDispatcher)

    fun upcomingMovies(): Flow<PagingData<Movie>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            MovieResponseDataSource(apiHelper::getUpcomingMovies)
        }.flow.flowOn(defaultDispatcher)

    fun trendingMovies(): Flow<PagingData<Movie>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            MovieResponseDataSource(apiHelper::getTrendingMovies)
        }.flow.flowOn(defaultDispatcher)

    fun topRatedMovies(): Flow<PagingData<Movie>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            MovieResponseDataSource(apiHelper::getTopRatedMovies)
        }.flow.flowOn(defaultDispatcher)

    fun nowPlayingMovies(): Flow<PagingData<Movie>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            MovieResponseDataSource(apiHelper::getNowPlayingMovies)
        }.flow.flowOn(defaultDispatcher)

    fun similarMovies(movieId: Int): Flow<PagingData<Movie>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            MovieDetailsResponseDataSource(
                movieId = movieId,
                apiHelperMethod = apiHelper::getSimilarMovies
            )
        }.flow.flowOn(defaultDispatcher)

    fun moviesRecommendations(movieId: Int): Flow<PagingData<Movie>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            MovieDetailsResponseDataSource(
                movieId = movieId,
                apiHelperMethod = apiHelper::getMoviesRecommendations
            )
        }.flow.flowOn(defaultDispatcher)

    fun movieDetails(movieId: Int, isoCode: String = "pl-PL"): Call<MovieDetails> =
        apiHelper.getMovieDetails(movieId, isoCode)

    fun movieCredits(movieId: Int, isoCode: String = "pl-PL"): Call<Credits> =
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
                apiHelperMethod = apiHelper::getMovieReviews
            )
        }.flow.flowOn(defaultDispatcher)

    fun movieReview(movieId: Int): Call<ReviewsResponse> = apiHelper.getMovieReview(movieId)

    fun collection(collectionId: Int): Call<CollectionResponse> =
        apiHelper.getCollection(collectionId)
}