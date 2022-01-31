package com.example.moviesapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviesapp.api.TmdbApiHelper
import com.example.moviesapp.data.TvSeriesDetailsResponseDataSource
import com.example.moviesapp.data.TvSeriesResponseDataSource
import com.example.moviesapp.model.ImagesResponse
import com.example.moviesapp.model.TvSeasonsResponse
import com.example.moviesapp.model.TvSeries
import com.example.moviesapp.model.TvSeriesDetails
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvSeriesRepository @Inject constructor(
    private val apiHelper: TmdbApiHelper,
) {
    fun topRatedTvSeries(): Flow<PagingData<TvSeries>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            TvSeriesResponseDataSource(apiHelper::getTopRatedTvSeries)
        }.flow

    fun onTheAirTvSeries(): Flow<PagingData<TvSeries>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            TvSeriesResponseDataSource(apiHelper::getOnTheAirTvSeries)
        }.flow

    fun trendingTvSeries(): Flow<PagingData<TvSeries>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            TvSeriesResponseDataSource(apiHelper::getTrendingTvSeries)
        }.flow

    fun popularTvSeries(): Flow<PagingData<TvSeries>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            TvSeriesResponseDataSource(apiHelper::getPopularTvSeries)
        }.flow

    fun airingTodayTvSeries(): Flow<PagingData<TvSeries>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            TvSeriesResponseDataSource(apiHelper::getAiringTodayTvSeries)
        }.flow

    fun similarTvSeries(tvSeriesId: Int): Flow<PagingData<TvSeries>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            TvSeriesDetailsResponseDataSource(
                tvSeriesId = tvSeriesId,
                apiHelperMethod = apiHelper::getSimilarTvSeries
            )
        }.flow

    fun tvSeriesRecommendations(tvSeriesId: Int): Flow<PagingData<TvSeries>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            TvSeriesDetailsResponseDataSource(
                tvSeriesId = tvSeriesId,
                apiHelperMethod = apiHelper::getTvSeriesRecommendations
            )
        }.flow

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

}