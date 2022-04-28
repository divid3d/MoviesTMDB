package com.example.moviesapp.use_case

import androidx.paging.PagingData
import androidx.paging.map
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.model.TvSeriesType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class GetTvSeriesOfTypeUseCase @Inject constructor(
    private val getFavouriteTvSeriesCountUseCase: GetFavouriteTvSeriesCountUseCase,
    private val getOnTheAirTvSeriesUseCase: GetOnTheAirTvSeriesUseCase,
    private val getTopRatedTvSeriesUseCase: GetTopRatedTvSeriesUseCase,
    private val getAiringTodayTvSeriesUseCase: GetAiringTodayTvSeriesUseCase,
    private val getTrendingTvSeriesUseCase: GetTrendingTvSeriesUseCase,
    private val getFavouriteTvSeriesUseCase: GetFavouritesTvSeriesUseCase,
    private val getRecentlyBrowsedTvSeriesUseCase: GetRecentlyBrowsedTvSeriesUseCase,
){
    operator fun invoke(type: TvSeriesType, deviceLanguage: DeviceLanguage): Flow<PagingData<Presentable>> {
        return when (type) {
            TvSeriesType.OnTheAir -> getOnTheAirTvSeriesUseCase(deviceLanguage)
            TvSeriesType.TopRated  -> getTopRatedTvSeriesUseCase(deviceLanguage)
            TvSeriesType.AiringToday -> getAiringTodayTvSeriesUseCase(deviceLanguage)
            TvSeriesType.Trending -> getTrendingTvSeriesUseCase(deviceLanguage)
            TvSeriesType.Favourite -> getFavouriteTvSeriesUseCase()
            TvSeriesType.RecentlyBrowsed -> getRecentlyBrowsedTvSeriesUseCase()
        }.mapLatest { data -> data.map { it } }
    }
}