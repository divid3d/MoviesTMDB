package com.example.moviesapp.ui.screens.tv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.RecentlyBrowsedTvSeries
import com.example.moviesapp.model.TvSeries
import com.example.moviesapp.model.TvSeriesFavourite
import com.example.moviesapp.repository.ConfigRepository
import com.example.moviesapp.repository.FavouritesRepository
import com.example.moviesapp.repository.RecentlyBrowsedRepository
import com.example.moviesapp.repository.TvSeriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class TvSeriesViewModel @Inject constructor(
    private val tvSeriesRepository: TvSeriesRepository,
    private val configRepository: ConfigRepository,
    private val favouritesRepository: FavouritesRepository,
    private val recentlyBrowsedRepository: RecentlyBrowsedRepository
) : ViewModel() {

    private val deviceLanguage: Flow<DeviceLanguage> = configRepository.getDeviceLanguage()

    val onTheAir: Flow<PagingData<TvSeries>> = deviceLanguage.map { deviceLanguage ->
        tvSeriesRepository
            .onTheAirTvSeries(deviceLanguage = deviceLanguage)
    }.flattenMerge().map { pagingData ->
        pagingData
            .filter { tvSeries ->
                tvSeries.run {
                    !backdropPath.isNullOrEmpty() && !posterPath.isNullOrEmpty() && title.isNotEmpty() && overview.isNotEmpty()
                }
            }
    }.cachedIn(viewModelScope)

    val discover: Flow<PagingData<TvSeries>> = deviceLanguage.map { deviceLanguage ->
        tvSeriesRepository
            .discoverTvSeries(deviceLanguage = deviceLanguage)
            .cachedIn(viewModelScope)
    }.flattenMerge()

    val topRated: Flow<PagingData<TvSeries>> = deviceLanguage.map { deviceLanguage ->
        tvSeriesRepository.topRatedTvSeries(deviceLanguage = deviceLanguage)
    }.flattenMerge().cachedIn(viewModelScope)

    val trending: Flow<PagingData<TvSeries>> = deviceLanguage.map { deviceLanguage ->
        tvSeriesRepository.trendingTvSeries(deviceLanguage = deviceLanguage)
    }.flattenMerge().cachedIn(viewModelScope)

    val airingToday: Flow<PagingData<TvSeries>> = deviceLanguage.map { deviceLanguage ->
        tvSeriesRepository.airingTodayTvSeries(deviceLanguage = deviceLanguage)
    }.flattenMerge().cachedIn(viewModelScope)

    val favourites: Flow<PagingData<TvSeriesFavourite>> =
        favouritesRepository.favouritesTvSeries().cachedIn(viewModelScope)

    val recentlyBrowsed: Flow<PagingData<RecentlyBrowsedTvSeries>> =
        recentlyBrowsedRepository.recentlyBrowsedTvSeries().cachedIn(viewModelScope)
}