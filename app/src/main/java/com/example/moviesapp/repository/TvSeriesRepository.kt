package com.example.moviesapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviesapp.api.TmdbApiHelper
import com.example.moviesapp.data.DiscoverTvSeriesDataSource
import com.example.moviesapp.data.ReviewsDataSource
import com.example.moviesapp.data.TvSeriesDetailsResponseDataSource
import com.example.moviesapp.data.TvSeriesResponseDataSource
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
class TvSeriesRepository @Inject constructor(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val apiHelper: TmdbApiHelper,
    private val crashlytics: FirebaseCrashlytics
) {
    fun discoverTvSeries(
        deviceLanguage: DeviceLanguage = DeviceLanguage.default,
        sortType: SortType = SortType.Popularity,
        sortOrder: SortOrder = SortOrder.Desc,
        genresParam: GenresParam = GenresParam(genres = emptyList()),
        watchProvidersParam: WatchProvidersParam = WatchProvidersParam(watchProviders = emptyList()),
        voteRange: ClosedFloatingPointRange<Float> = 0f..10f,
        onlyWithPosters: Boolean = false,
        onlyWithScore: Boolean = false,
        onlyWithOverview: Boolean = false,
        airDateRange: DateRange = DateRange()
    ): Flow<PagingData<TvSeries>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            DiscoverTvSeriesDataSource(
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
                airDateRange = airDateRange,
                firebaseCrashlytics = crashlytics
            )
        }.flow.flowOn(defaultDispatcher)

    fun topRatedTvSeries(deviceLanguage: DeviceLanguage = DeviceLanguage.default): Flow<PagingData<TvSeries>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            TvSeriesResponseDataSource(
                language = deviceLanguage.languageCode,
                region = deviceLanguage.region,
                apiHelperMethod = apiHelper::getTopRatedTvSeries,
                firebaseCrashlytics = crashlytics
            )
        }.flow.flowOn(defaultDispatcher)

    fun onTheAirTvSeries(deviceLanguage: DeviceLanguage = DeviceLanguage.default): Flow<PagingData<TvSeries>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            TvSeriesResponseDataSource(
                language = deviceLanguage.languageCode,
                region = deviceLanguage.region,
                apiHelperMethod = apiHelper::getOnTheAirTvSeries,
                firebaseCrashlytics = crashlytics
            )
        }.flow.flowOn(defaultDispatcher)

    fun trendingTvSeries(deviceLanguage: DeviceLanguage = DeviceLanguage.default): Flow<PagingData<TvSeries>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            TvSeriesResponseDataSource(
                language = deviceLanguage.languageCode,
                region = deviceLanguage.region,
                apiHelperMethod = apiHelper::getTrendingTvSeries,
                firebaseCrashlytics = crashlytics
            )
        }.flow.flowOn(defaultDispatcher)

    fun popularTvSeries(deviceLanguage: DeviceLanguage = DeviceLanguage.default): Flow<PagingData<TvSeries>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            TvSeriesResponseDataSource(
                language = deviceLanguage.languageCode,
                region = deviceLanguage.region,
                apiHelperMethod = apiHelper::getPopularTvSeries,
                firebaseCrashlytics = crashlytics
            )
        }.flow.flowOn(defaultDispatcher)

    fun airingTodayTvSeries(deviceLanguage: DeviceLanguage = DeviceLanguage.default): Flow<PagingData<TvSeries>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            TvSeriesResponseDataSource(
                language = deviceLanguage.languageCode,
                region = deviceLanguage.region,
                apiHelperMethod = apiHelper::getAiringTodayTvSeries,
                firebaseCrashlytics = crashlytics
            )
        }.flow.flowOn(defaultDispatcher)

    fun similarTvSeries(
        tvSeriesId: Int,
        deviceLanguage: DeviceLanguage = DeviceLanguage.default
    ): Flow<PagingData<TvSeries>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            TvSeriesDetailsResponseDataSource(
                tvSeriesId = tvSeriesId,
                language = deviceLanguage.languageCode,
                region = deviceLanguage.region,
                apiHelperMethod = apiHelper::getSimilarTvSeries,
                firebaseCrashlytics = crashlytics
            )
        }.flow.flowOn(defaultDispatcher)

    fun tvSeriesRecommendations(
        tvSeriesId: Int,
        deviceLanguage: DeviceLanguage = DeviceLanguage.default
    ): Flow<PagingData<TvSeries>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            TvSeriesDetailsResponseDataSource(
                tvSeriesId = tvSeriesId,
                language = deviceLanguage.languageCode,
                region = deviceLanguage.region,
                apiHelperMethod = apiHelper::getTvSeriesRecommendations,
                firebaseCrashlytics = crashlytics
            )
        }.flow.flowOn(defaultDispatcher)

    fun getTvSeriesDetails(
        tvSeriesId: Int,
        deviceLanguage: DeviceLanguage = DeviceLanguage.default
    ): Call<TvSeriesDetails> =
        apiHelper.getTvSeriesDetails(tvSeriesId, deviceLanguage.languageCode)

    fun getTvSeason(
        tvSeriesId: Int,
        seasonNumber: Int,
        deviceLanguage: DeviceLanguage = DeviceLanguage.default
    ): Call<TvSeasonsResponse> =
        apiHelper.getTvSeasons(tvSeriesId, seasonNumber, deviceLanguage.languageCode)

    fun tvSeriesImages(
        tvSeriesId: Int
    ): Call<ImagesResponse> = apiHelper.getTvSeriesImages(tvSeriesId)

    fun seasonDetails(
        tvSeriesId: Int,
        seasonNumber: Int,
        deviceLanguage: DeviceLanguage = DeviceLanguage.default
    ): Call<SeasonDetails> =
        apiHelper.getSeasonDetails(tvSeriesId, seasonNumber, deviceLanguage.languageCode)

    fun episodeImages(
        tvSeriesId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): Call<ImagesResponse> = apiHelper.getEpisodeImages(tvSeriesId, seasonNumber, episodeNumber)

    fun tvSeriesReviews(tvSeriesId: Int): Flow<PagingData<Review>> =
        Pager(
            PagingConfig(pageSize = 5)
        ) {
            ReviewsDataSource(
                mediaId = tvSeriesId,
                apiHelperMethod = apiHelper::getTvSeriesReviews,
                firebaseCrashlytics = crashlytics
            )
        }.flow.flowOn(defaultDispatcher)

    fun tvSeriesReview(tvSeriesId: Int): Call<ReviewsResponse> =
        apiHelper.getTvSeriesReview(tvSeriesId)

    fun watchProviders(tvSeriesId: Int): Call<WatchProvidersResponse> =
        apiHelper.getTvSeriesWatchProviders(tvSeriesId)

    fun getExternalIds(tvSeriesId: Int) = apiHelper.getTvSeriesExternalIds(tvSeriesId)

    fun getTvSeriesVideos(tvSeriesId: Int, isoCode: String = DeviceLanguage.default.languageCode) =
        apiHelper.getTvSeriesVideos(tvSeriesId, isoCode)

}