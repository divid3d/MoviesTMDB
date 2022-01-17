package com.example.moviesapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviesapp.api.TmdbApiHelper
import com.example.moviesapp.data.TvSeriesResponseDataSource
import com.example.moviesapp.model.TvSeries
import com.example.moviesapp.model.TvSeriesResponse
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

    suspend fun similarTvSeries(
        tvSeriesId: Int,
        page: Int = 1,
        isoCode: String = "pl-PL"
    ): TvSeriesResponse =
        apiHelper.getSimilarTvSeries(tvSeriesId, page, isoCode)

    suspend fun tvSeriesRecommendations(
        tvSeriesId: Int,
        page: Int = 1,
        isoCode: String = "pl-PL"
    ): TvSeriesResponse =
        apiHelper.getTvSeriesRecommendations(tvSeriesId, page, isoCode)

}