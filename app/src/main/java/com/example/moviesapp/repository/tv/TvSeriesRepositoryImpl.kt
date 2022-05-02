package com.example.moviesapp.repository.tv

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
import javax.inject.Singleton

@Singleton
@OptIn(ExperimentalPagingApi::class)
class TvSeriesRepositoryImpl(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val apiHelper: TmdbApiHelper,
    private val appDatabase: AppDatabase
) : TvSeriesRepository {
    override fun discoverTvSeries(
        deviceLanguage: DeviceLanguage,
        sortType: SortType,
        sortOrder: SortOrder,
        genresParam: GenresParam,
        watchProvidersParam: WatchProvidersParam,
        voteRange: ClosedFloatingPointRange<Float>,
        onlyWithPosters: Boolean,
        onlyWithScore: Boolean,
        onlyWithOverview: Boolean,
        airDateRange: DateRange
    ): Flow<PagingData<TvSeries>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            DiscoverTvSeriesDataSource(
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
                airDateRange = airDateRange
            )
        }.flow.flowOn(defaultDispatcher)

    override fun topRatedTvSeries(deviceLanguage: DeviceLanguage): Flow<PagingData<TvSeriesEntity>> =
        Pager(
            config = PagingConfig(pageSize = 20, initialLoadSize = 40),
            remoteMediator = TvSeriesRemoteMediator(
                deviceLanguage = deviceLanguage,
                apiHelper = apiHelper,
                appDatabase = appDatabase,
                type = TvSeriesEntityType.TopRated
            ),
            pagingSourceFactory = {
                appDatabase.tvSeriesDao().getAllTvSeries(TvSeriesEntityType.TopRated)
            }
        ).flow.flowOn(defaultDispatcher)

    override fun onTheAirTvSeries(deviceLanguage: DeviceLanguage): Flow<PagingData<TvSeriesDetailEntity>> =
        Pager(
            config = PagingConfig(pageSize = 20, initialLoadSize = 40),
            remoteMediator = TvSeriesDetailsRemoteMediator(
                deviceLanguage = deviceLanguage,
                apiHelper = apiHelper,
                appDatabase = appDatabase,
            ),
            pagingSourceFactory = {
                appDatabase.tvSeriesDetailsDao().getAllTvSeries()
            }
        ).flow.flowOn(defaultDispatcher)

    override fun trendingTvSeries(deviceLanguage: DeviceLanguage): Flow<PagingData<TvSeriesEntity>> =
        Pager(
            config = PagingConfig(pageSize = 20, initialLoadSize = 40),
            remoteMediator = TvSeriesRemoteMediator(
                deviceLanguage = deviceLanguage,
                apiHelper = apiHelper,
                appDatabase = appDatabase,
                type = TvSeriesEntityType.Trending
            ),
            pagingSourceFactory = {
                appDatabase.tvSeriesDao().getAllTvSeries(TvSeriesEntityType.Trending)
            }
        ).flow.flowOn(defaultDispatcher)

    override fun popularTvSeries(deviceLanguage: DeviceLanguage): Flow<PagingData<TvSeriesEntity>> =
        Pager(
            config = PagingConfig(pageSize = 20, initialLoadSize = 40),
            remoteMediator = TvSeriesRemoteMediator(
                deviceLanguage = deviceLanguage,
                apiHelper = apiHelper,
                appDatabase = appDatabase,
                type = TvSeriesEntityType.Popular
            ),
            pagingSourceFactory = {
                appDatabase.tvSeriesDao().getAllTvSeries(TvSeriesEntityType.Popular)
            }
        ).flow.flowOn(defaultDispatcher)

    override fun airingTodayTvSeries(deviceLanguage: DeviceLanguage): Flow<PagingData<TvSeriesEntity>> =
        Pager(
            config = PagingConfig(pageSize = 20, initialLoadSize = 40),
            remoteMediator = TvSeriesRemoteMediator(
                deviceLanguage = deviceLanguage,
                apiHelper = apiHelper,
                appDatabase = appDatabase,
                type = TvSeriesEntityType.AiringToday
            ),
            pagingSourceFactory = {
                appDatabase.tvSeriesDao().getAllTvSeries(TvSeriesEntityType.AiringToday)
            }
        ).flow.flowOn(defaultDispatcher)

    override fun similarTvSeries(
        tvSeriesId: Int,
        deviceLanguage: DeviceLanguage
    ): Flow<PagingData<TvSeries>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            TvSeriesDetailsResponseDataSource(
                tvSeriesId = tvSeriesId,
                deviceLanguage = deviceLanguage,
                apiHelperMethod = apiHelper::getSimilarTvSeries
            )
        }.flow.flowOn(defaultDispatcher)

    override fun tvSeriesRecommendations(
        tvSeriesId: Int,
        deviceLanguage: DeviceLanguage
    ): Flow<PagingData<TvSeries>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            TvSeriesDetailsResponseDataSource(
                tvSeriesId = tvSeriesId,
                deviceLanguage = deviceLanguage,
                apiHelperMethod = apiHelper::getTvSeriesRecommendations
            )
        }.flow.flowOn(defaultDispatcher)

    override fun getTvSeriesDetails(
        tvSeriesId: Int,
        deviceLanguage: DeviceLanguage
    ): Call<TvSeriesDetails> =
        apiHelper.getTvSeriesDetails(tvSeriesId, deviceLanguage.languageCode)


    override fun tvSeriesImages(
        tvSeriesId: Int
    ): Call<ImagesResponse> = apiHelper.getTvSeriesImages(tvSeriesId)

    override fun tvSeriesReviews(tvSeriesId: Int): Flow<PagingData<Review>> =
        Pager(
            PagingConfig(pageSize = 5)
        ) {
            ReviewsDataSource(
                mediaId = tvSeriesId,
                apiHelperMethod = apiHelper::getTvSeriesReviews
            )
        }.flow.flowOn(defaultDispatcher)

    override fun tvSeriesReview(tvSeriesId: Int): Call<ReviewsResponse> =
        apiHelper.getTvSeriesReview(tvSeriesId)

    override fun watchProviders(tvSeriesId: Int): Call<WatchProvidersResponse> =
        apiHelper.getTvSeriesWatchProviders(tvSeriesId)

    override fun getExternalIds(tvSeriesId: Int): Call<ExternalIds> =
        apiHelper.getTvSeriesExternalIds(tvSeriesId)

    override fun tvSeriesVideos(
        tvSeriesId: Int,
        isoCode: String
    ): Call<VideosResponse> = apiHelper.getTvSeriesVideos(tvSeriesId, isoCode)
}