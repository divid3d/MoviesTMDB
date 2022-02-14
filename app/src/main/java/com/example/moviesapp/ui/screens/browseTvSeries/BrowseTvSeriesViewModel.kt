package com.example.moviesapp.ui.screens.browseTvSeries

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.moviesapp.model.*
import com.example.moviesapp.other.asFlow
import com.example.moviesapp.repository.ConfigRepository
import com.example.moviesapp.repository.FavouritesRepository
import com.example.moviesapp.repository.RecentlyBrowsedRepository
import com.example.moviesapp.repository.TvSeriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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

    var tvSeries: Flow<PagingData<Presentable>>? = null

    private val tvSeriesType: Flow<TvSeriesType> = savedStateHandle
        .getLiveData("tvSeriesType", TvSeriesType.Popular.name)
        .asFlow().map { value ->
            TvSeriesType.valueOf(value)
        }

    private val topRated: Flow<PagingData<Presentable>> = deviceLanguage.map { deviceLanguage ->
        tvSeriesRepository.topRatedTvSeries(deviceLanguage = deviceLanguage)
    }.flattenMerge().map { data -> data.map { tvSeries -> tvSeries } }

    private val airingToday: Flow<PagingData<TvSeries>> = deviceLanguage.map { deviceLanguage ->
        tvSeriesRepository.airingTodayTvSeries(deviceLanguage = deviceLanguage)
    }.flattenMerge()

    private val popular: Flow<PagingData<TvSeries>> = deviceLanguage.map { deviceLanguage ->
        tvSeriesRepository.popularTvSeries(deviceLanguage = deviceLanguage)
    }.flattenMerge()

    private val trending: Flow<PagingData<TvSeries>> = deviceLanguage.map { deviceLanguage ->
        tvSeriesRepository.trendingTvSeries(deviceLanguage = deviceLanguage)
    }.flattenMerge()

    private val favourites: Flow<PagingData<TvSeriesFavourite>> =
        favouritesRepository.favouritesTvSeries()

    private val recentlyBrowsed: Flow<PagingData<RecentlyBrowsedTvSeries>> =
        recentlyBrowsedRepository.recentlyBrowsedTvSeries()

    val favouriteTvSeriesCount: StateFlow<Int> = favouritesRepository.getFavouriteTvSeriesCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), 0)

    init {
        viewModelScope.launch {
            tvSeriesType.collectLatest { type ->
                tvSeries = when (type) {
                    TvSeriesType.TopRated -> topRated
                    TvSeriesType.AiringToday -> airingToday
                    TvSeriesType.Popular -> popular
                    TvSeriesType.Favourite -> favourites
                    TvSeriesType.RecentlyBrowsed -> recentlyBrowsed
                    TvSeriesType.Trending -> trending
                }.map { data -> data.map { movie -> movie } }.cachedIn(viewModelScope)
            }
        }
    }

    fun onClearClicked() {
        recentlyBrowsedRepository.clearRecentlyBrowsedTvSeries()
    }

}