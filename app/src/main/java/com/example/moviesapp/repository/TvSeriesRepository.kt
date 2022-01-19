package com.example.moviesapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviesapp.api.TmdbApiHelper
import com.example.moviesapp.data.TvSeriesDetailsResponseDataSource
import com.example.moviesapp.data.TvSeriesResponseDataSource
import com.example.moviesapp.model.TvSeasonsResponse
import com.example.moviesapp.model.TvSeries
import com.example.moviesapp.model.TvSeriesDetails
import kotlinx.coroutines.flow.Flow
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

    suspend fun getTvSeriesDetails(tvSeriesId: Int, isoCode: String = "pl-PL"): TvSeriesDetails =
        apiHelper.getTvSeriesDetails(tvSeriesId, isoCode)

    suspend fun getTvSeasons(
        tvSeriesId: Int,
        seasonNumber: Int,
        isoCode: String = "pl-PL"
    ): TvSeasonsResponse =
        apiHelper.getTvSeasons(tvSeriesId, seasonNumber, isoCode)

}