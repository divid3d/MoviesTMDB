package com.example.moviesapp.ui.screens.tv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.other.appendUrls
import com.example.moviesapp.repository.ConfigRepository
import com.example.moviesapp.repository.FavouritesRepository
import com.example.moviesapp.repository.RecentlyBrowsedRepository
import com.example.moviesapp.repository.TvSeriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class TvSeriesViewModel @Inject constructor(
    private val configRepository: ConfigRepository,
    private val tvSeriesRepository: TvSeriesRepository,
    private val favouritesRepository: FavouritesRepository,
    private val recentlyBrowsedRepository: RecentlyBrowsedRepository
) : ViewModel() {

    private val config = configRepository.config

    val onTheAir: Flow<PagingData<Presentable>> =
        tvSeriesRepository.onTheAirTvSeries()
            .cachedIn(viewModelScope)
            .combine(config) { pagingData, config ->
                pagingData
                    .filter { tvSeries ->
                        tvSeries.run {
                            !backdropPath.isNullOrEmpty() && !posterPath.isNullOrEmpty() && title.isNotEmpty() && overview.isNotEmpty()
                        }
                    }
                    .map { tvSeries -> tvSeries.appendUrls(config) }
            }

    val popular: Flow<PagingData<Presentable>> =
        tvSeriesRepository.popularTvSeries()
            .cachedIn(viewModelScope)
            .combine(config) { pagingData, config ->
                pagingData.map { tvSeries -> tvSeries.appendUrls(config) }
            }

    val topRated: Flow<PagingData<Presentable>> =
        tvSeriesRepository.topRatedTvSeries()
            .cachedIn(viewModelScope)
            .combine(config) { pagingData, config ->
                pagingData.map { tvSeries -> tvSeries.appendUrls(config) }
            }

    val airingToday: Flow<PagingData<Presentable>> =
        tvSeriesRepository.airingTodayTvSeries()
            .cachedIn(viewModelScope)
            .combine(config) { pagingData, config ->
                pagingData.map { tvSeries -> tvSeries.appendUrls(config) }
            }

    val favourites: Flow<PagingData<Presentable>> =
        favouritesRepository.favouritesTvSeries()
            .cachedIn(viewModelScope)
            .combine(config) { pagingData, config ->
                pagingData.map { tvSeries -> tvSeries.appendUrls(config) }
            }

    val recentlyBrowsed: Flow<PagingData<Presentable>> =
        recentlyBrowsedRepository.recentlyBrowsedTvSeries()
            .cachedIn(viewModelScope)
            .combine(config) { pagingData, config ->
                pagingData.map { tvSeries -> tvSeries.appendUrls(config) }
            }

}