package com.example.moviesapp.repository.tv

import androidx.paging.PagingData
import com.example.moviesapp.model.*
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

interface TvSeriesRepository {
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
    ): Flow<PagingData<TvSeries>>

    fun topRatedTvSeries(
        deviceLanguage: DeviceLanguage = DeviceLanguage.default
    ): Flow<PagingData<TvSeries>>

    fun onTheAirTvSeries(
        deviceLanguage: DeviceLanguage = DeviceLanguage.default
    ): Flow<PagingData<TvSeries>>

    fun trendingTvSeries(
        deviceLanguage: DeviceLanguage = DeviceLanguage.default
    ): Flow<PagingData<TvSeries>>

    fun popularTvSeries(
        deviceLanguage: DeviceLanguage = DeviceLanguage.default
    ): Flow<PagingData<TvSeries>>

    fun airingTodayTvSeries(
        deviceLanguage: DeviceLanguage = DeviceLanguage.default
    ): Flow<PagingData<TvSeries>>

    fun similarTvSeries(
        tvSeriesId: Int,
        deviceLanguage: DeviceLanguage = DeviceLanguage.default
    ): Flow<PagingData<TvSeries>>

    fun tvSeriesRecommendations(
        tvSeriesId: Int,
        deviceLanguage: DeviceLanguage = DeviceLanguage.default
    ): Flow<PagingData<TvSeries>>

    fun getTvSeriesDetails(
        tvSeriesId: Int,
        deviceLanguage: DeviceLanguage = DeviceLanguage.default
    ): Call<TvSeriesDetails>

    fun getTvSeason(
        tvSeriesId: Int,
        seasonNumber: Int,
        deviceLanguage: DeviceLanguage = DeviceLanguage.default
    ): Call<TvSeasonsResponse>

    fun tvSeriesImages(
        tvSeriesId: Int
    ): Call<ImagesResponse>

    fun seasonDetails(
        tvSeriesId: Int,
        seasonNumber: Int,
        deviceLanguage: DeviceLanguage = DeviceLanguage.default
    ): Call<SeasonDetails>

    fun episodeImages(
        tvSeriesId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): Call<ImagesResponse>

    fun tvSeriesReviews(tvSeriesId: Int): Flow<PagingData<Review>>

    fun tvSeriesReview(tvSeriesId: Int): Call<ReviewsResponse>

    fun watchProviders(tvSeriesId: Int): Call<WatchProvidersResponse>

    fun getExternalIds(tvSeriesId: Int): Call<ExternalIds>

    fun tvSeriesVideos(
        tvSeriesId: Int,
        isoCode: String = DeviceLanguage.default.languageCode
    ): Call<VideosResponse>

    fun seasonVideos(
        tvSeriesId: Int,
        seasonNumber: Int,
        isoCode: String = DeviceLanguage.default.languageCode
    ): Call<VideosResponse>

    fun seasonCredits(
        tvSeriesId: Int,
        seasonNumber: Int,
        isoCode: String = DeviceLanguage.default.languageCode
    ): Call<AggregatedCredits>

}