package com.example.moviesapp.ui.screens.browse

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.model.TvSeriesType
import com.example.moviesapp.other.asFlow
import com.example.moviesapp.repository.ConfigRepository
import com.example.moviesapp.repository.FavouritesRepository
import com.example.moviesapp.repository.RecentlyBrowsedRepository
import com.example.moviesapp.repository.TvSeriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class BrowseTvSeriesViewModel @Inject constructor(
    private val tvSeriesRepository: TvSeriesRepository,
    private val configRepository: ConfigRepository,
    private val favouritesRepository: FavouritesRepository,
    private val recentlyBrowsedRepository: RecentlyBrowsedRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val deviceLanguage: Flow<DeviceLanguage> = configRepository.getDeviceLanguage()

    private val tvSeriesType: Flow<TvSeriesType> = savedStateHandle
        .getLiveData("tvSeriesType", TvSeriesType.Trending.name)
        .asFlow().map { value ->
            TvSeriesType.valueOf(value)
        }

    val favouriteTvSeriesCount: StateFlow<Int> = favouritesRepository.getFavouriteTvSeriesCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), 0)

    var tvSeries: Flow<PagingData<Presentable>>? = combine(
        tvSeriesType, deviceLanguage
    ) { type, deviceLanguage ->
        when (type) {
            TvSeriesType.TopRated -> tvSeriesRepository.topRatedTvSeries(deviceLanguage)
            TvSeriesType.AiringToday -> tvSeriesRepository.airingTodayTvSeries(deviceLanguage)
            TvSeriesType.Favourite -> favouritesRepository.favouritesTvSeries()
            TvSeriesType.RecentlyBrowsed -> recentlyBrowsedRepository.recentlyBrowsedTvSeries()
            TvSeriesType.Trending -> tvSeriesRepository.trendingTvSeries(deviceLanguage)
        }
    }.flattenMerge().map { data -> data.map { tvSeries -> tvSeries } }.cachedIn(viewModelScope)

    fun onClearClicked() {
        recentlyBrowsedRepository.clearRecentlyBrowsedTvSeries()
    }
}