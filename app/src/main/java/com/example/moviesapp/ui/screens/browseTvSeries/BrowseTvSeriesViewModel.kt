package com.example.moviesapp.ui.screens.browseTvSeries

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.model.TvSeriesType
import com.example.moviesapp.other.appendUrls
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
    private val favouritesRepository: FavouritesRepository,
    private val recentlyBrowsedRepository: RecentlyBrowsedRepository,
    private val configRepository: ConfigRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val config = configRepository.config

    var tvSeries: Flow<PagingData<Presentable>>? = null

    private val tvSeriesType: Flow<TvSeriesType> = savedStateHandle
        .getLiveData("tvSeriesType", TvSeriesType.Popular.name)
        .asFlow().map { value ->
            TvSeriesType.valueOf(value)
        }

    private val topRated: Flow<PagingData<Presentable>> =
        tvSeriesRepository.topRatedTvSeries()
            .combine(config) { pagingData, config ->
                pagingData.map { tvSeries -> tvSeries.appendUrls(config) }
            }

    private val airingToday: Flow<PagingData<Presentable>> =
        tvSeriesRepository.airingTodayTvSeries()
            .combine(config) { pagingData, config ->
                pagingData.map { tvSeries -> tvSeries.appendUrls(config) }
            }

    private val popular: Flow<PagingData<Presentable>> =
        tvSeriesRepository.popularTvSeries()
            .combine(config) { pagingData, config ->
                pagingData.map { tvSeries -> tvSeries.appendUrls(config) }
            }

    private val favourites: Flow<PagingData<Presentable>> =
        favouritesRepository.favouritesTvSeries()
            .combine(config) { pagingData, config ->
                pagingData.map { tvSeries -> tvSeries.appendUrls(config) }
            }

    private val recentlyBrowsed: Flow<PagingData<Presentable>> =
        recentlyBrowsedRepository.recentlyBrowsedTvSeries()
            .combine(config) { pagingData, config ->
                pagingData.map { tvSeries -> tvSeries.appendUrls(config) }
            }

    private val trending: Flow<PagingData<Presentable>> =
        tvSeriesRepository.trendingTvSeries()
            .combine(config) { pagingData, config ->
                pagingData.map { tvSeries -> tvSeries.appendUrls(config) }
            }

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
                }.cachedIn(viewModelScope)
            }
        }
    }

}