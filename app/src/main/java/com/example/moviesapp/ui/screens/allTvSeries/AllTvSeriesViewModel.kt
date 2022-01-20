package com.example.moviesapp.ui.screens.allTvSeries

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
import com.example.moviesapp.repository.TvSeriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class AllTvSeriesViewModel @Inject constructor(
    private val tvSeriesRepository: TvSeriesRepository,
    private val favouritesRepository: FavouritesRepository,
    private val configRepository: ConfigRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val config = configRepository.config

    private val tvSeriesType: Flow<TvSeriesType> = savedStateHandle
        .getLiveData("tvSeriesType", TvSeriesType.Popular.name)
        .asFlow().map { value ->
            TvSeriesType.valueOf(value)
        }

    private val topRated: Flow<PagingData<Presentable>> =
        tvSeriesRepository.topRatedTvSeries()
            .cachedIn(viewModelScope)
            .combine(config) { pagingData, config ->
                pagingData.map { tvSeries -> tvSeries.appendUrls(config) }
            }

    private val airingToday: Flow<PagingData<Presentable>> =
        tvSeriesRepository.airingTodayTvSeries()
            .cachedIn(viewModelScope)
            .combine(config) { pagingData, config ->
                pagingData.map { tvSeries -> tvSeries.appendUrls(config) }
            }
    private val popular: Flow<PagingData<Presentable>> =
        tvSeriesRepository.popularTvSeries()
            .cachedIn(viewModelScope)
            .combine(config) { pagingData, config ->
                pagingData.map { tvSeries -> tvSeries.appendUrls(config) }
            }
    private val favourites: Flow<PagingData<Presentable>> =
        favouritesRepository.favouritesTvSeries()
            .cachedIn(viewModelScope)
            .combine(config) { pagingData, config ->
                pagingData.map { tvSeries -> tvSeries.appendUrls(config) }
            }

    val tvSeries: Flow<PagingData<Presentable>> = combine(
        tvSeriesType,
        topRated,
        airingToday,
        popular,
        favourites
    ) { type, topRated, airingToday, popular, favourites ->
        when (type) {
            TvSeriesType.TopRated -> topRated
            TvSeriesType.AiringToday -> airingToday
            TvSeriesType.Popular -> popular
            TvSeriesType.Favourite -> favourites
        }
    }

    val favouriteTvSeriesCount: StateFlow<Int> = favouritesRepository.getFavouriteTvSeriesCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), 0)

}