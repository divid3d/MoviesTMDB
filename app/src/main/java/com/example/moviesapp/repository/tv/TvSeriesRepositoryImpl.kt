package com.example.moviesapp.repository.tv

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviesapp.api.TmdbApiHelper
import com.example.moviesapp.data.DiscoverTvSeriesDataSource
import com.example.moviesapp.data.ReviewsDataSource
import com.example.moviesapp.data.TvSeriesDetailsResponseDataSource
import com.example.moviesapp.data.TvSeriesResponseDataSource
import com.example.moviesapp.model.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Call
import javax.inject.Singleton

@Singleton
class TvSeriesRepositoryImpl(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val apiHelper: TmdbApiHelper
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
                airDateRange = airDateRange
            )
        }.flow.flowOn(defaultDispatcher)

    override fun topRatedTvSeries(deviceLanguage: DeviceLanguage): Flow<PagingData<TvSeries>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            TvSeriesResponseDataSource(
                language = deviceLanguage.languageCode,
                region = deviceLanguage.region,
                apiHelperMethod = apiHelper::getTopRatedTvSeries
            )
        }.flow.flowOn(defaultDispatcher)

    override fun onTheAirTvSeries(deviceLanguage: DeviceLanguage): Flow<PagingData<TvSeries>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            TvSeriesResponseDataSource(
                language = deviceLanguage.languageCode,
                region = deviceLanguage.region,
                apiHelperMethod = apiHelper::getOnTheAirTvSeries
            )
        }.flow.flowOn(defaultDispatcher)

    override fun trendingTvSeries(deviceLanguage: DeviceLanguage): Flow<PagingData<TvSeries>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            TvSeriesResponseDataSource(
                language = deviceLanguage.languageCode,
                region = deviceLanguage.region,
                apiHelperMethod = apiHelper::getTrendingTvSeries
            )
        }.flow.flowOn(defaultDispatcher)

    override fun popularTvSeries(deviceLanguage: DeviceLanguage): Flow<PagingData<TvSeries>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            TvSeriesResponseDataSource(
                language = deviceLanguage.languageCode,
                region = deviceLanguage.region,
                apiHelperMethod = apiHelper::getPopularTvSeries
            )
        }.flow.flowOn(defaultDispatcher)

    override fun airingTodayTvSeries(deviceLanguage: DeviceLanguage): Flow<PagingData<TvSeries>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            TvSeriesResponseDataSource(
                language = deviceLanguage.languageCode,
                region = deviceLanguage.region,
                apiHelperMethod = apiHelper::getAiringTodayTvSeries
            )
        }.flow.flowOn(defaultDispatcher)

    override fun similarTvSeries(
        tvSeriesId: Int,
        deviceLanguage: DeviceLanguage
    ): Flow<PagingData<TvSeries>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            TvSeriesDetailsResponseDataSource(
                tvSeriesId = tvSeriesId,
                language = deviceLanguage.languageCode,
                region = deviceLanguage.region,
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
                language = deviceLanguage.languageCode,
                region = deviceLanguage.region,
                apiHelperMethod = apiHelper::getTvSeriesRecommendations
            )
        }.flow.flowOn(defaultDispatcher)

    override fun getTvSeriesDetails(
        tvSeriesId: Int,
        deviceLanguage: DeviceLanguage
    ): Call<TvSeriesDetails> =
        apiHelper.getTvSeriesDetails(tvSeriesId, deviceLanguage.languageCode)

    override fun getTvSeason(
        tvSeriesId: Int,
        seasonNumber: Int,
        deviceLanguage: DeviceLanguage
    ): Call<TvSeasonsResponse> =
        apiHelper.getTvSeasons(tvSeriesId, seasonNumber, deviceLanguage.languageCode)

    override fun tvSeriesImages(
        tvSeriesId: Int
    ): Call<ImagesResponse> = apiHelper.getTvSeriesImages(tvSeriesId)

    override fun seasonDetails(
        tvSeriesId: Int,
        seasonNumber: Int,
        deviceLanguage: DeviceLanguage
    ): Call<SeasonDetails> =
        apiHelper.getSeasonDetails(tvSeriesId, seasonNumber, deviceLanguage.languageCode)

    override fun episodeImages(
        tvSeriesId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): Call<ImagesResponse> = apiHelper.getEpisodeImages(tvSeriesId, seasonNumber, episodeNumber)

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

    override fun seasonVideos(
        tvSeriesId: Int,
        seasonNumber: Int,
        isoCode: String
    ): Call<VideosResponse> = apiHelper.getSeasonVideos(tvSeriesId, seasonNumber, isoCode)
}