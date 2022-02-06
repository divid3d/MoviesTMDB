package com.example.moviesapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviesapp.api.TmdbApiHelper
import com.example.moviesapp.data.ReviewsDataSource
import com.example.moviesapp.data.TvSeriesDetailsResponseDataSource
import com.example.moviesapp.data.TvSeriesResponseDataSource
import com.example.moviesapp.model.*
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
    private val apiHelper: TmdbApiHelper
) {
    fun topRatedTvSeries(): Flow<PagingData<TvSeries>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            TvSeriesResponseDataSource(apiHelper::getTopRatedTvSeries)
        }.flow.flowOn(defaultDispatcher)

    fun onTheAirTvSeries(): Flow<PagingData<TvSeries>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            TvSeriesResponseDataSource(apiHelper::getOnTheAirTvSeries)
        }.flow.flowOn(defaultDispatcher)

    fun trendingTvSeries(): Flow<PagingData<TvSeries>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            TvSeriesResponseDataSource(apiHelper::getTrendingTvSeries)
        }.flow.flowOn(defaultDispatcher)

    fun popularTvSeries(): Flow<PagingData<TvSeries>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            TvSeriesResponseDataSource(apiHelper::getPopularTvSeries)
        }.flow.flowOn(defaultDispatcher)

    fun airingTodayTvSeries(): Flow<PagingData<TvSeries>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            TvSeriesResponseDataSource(apiHelper::getAiringTodayTvSeries)
        }.flow.flowOn(defaultDispatcher)

    fun similarTvSeries(tvSeriesId: Int): Flow<PagingData<TvSeries>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            TvSeriesDetailsResponseDataSource(
                tvSeriesId = tvSeriesId,
                apiHelperMethod = apiHelper::getSimilarTvSeries
            )
        }.flow.flowOn(defaultDispatcher)

    fun tvSeriesRecommendations(tvSeriesId: Int): Flow<PagingData<TvSeries>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            TvSeriesDetailsResponseDataSource(
                tvSeriesId = tvSeriesId,
                apiHelperMethod = apiHelper::getTvSeriesRecommendations
            )
        }.flow.flowOn(defaultDispatcher)

    fun getTvSeriesDetails(tvSeriesId: Int, isoCode: String = "pl-PL"): Call<TvSeriesDetails> =
        apiHelper.getTvSeriesDetails(tvSeriesId, isoCode)

    fun getTvSeason(
        tvSeriesId: Int,
        seasonNumber: Int,
        isoCode: String = "pl-PL"
    ): Call<TvSeasonsResponse> =
        apiHelper.getTvSeasons(tvSeriesId, seasonNumber, isoCode)

    fun tvSeriesImages(
        tvSeriesId: Int
    ): Call<ImagesResponse> = apiHelper.getTvSeriesImages(tvSeriesId)

    fun seasonDetails(
        tvSeriesId: Int,
        seasonNumber: Int
    ): Call<SeasonDetails> = apiHelper.getSeasonDetails(tvSeriesId, seasonNumber)

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
                apiHelperMethod = apiHelper::getTvSeriesReviews
            )
        }.flow.flowOn(defaultDispatcher)

    fun tvSeriesReview(tvSeriesId: Int): Call<ReviewsResponse> =
        apiHelper.getTvSeriesReview(tvSeriesId)

}