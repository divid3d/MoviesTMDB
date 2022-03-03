package com.example.moviesapp.ui.screens.tv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.filter
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.TvSeries
import com.example.moviesapp.repository.browsed.RecentlyBrowsedRepository
import com.example.moviesapp.repository.config.ConfigRepository
import com.example.moviesapp.repository.favourites.FavouritesRepository
import com.example.moviesapp.repository.tv.TvSeriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class TvSeriesViewModel @Inject constructor(
    private val configRepository: ConfigRepository,
    private val tvSeriesRepository: TvSeriesRepository,
    private val favouritesRepository: FavouritesRepository,
    private val recentlyBrowsedRepository: RecentlyBrowsedRepository
) : ViewModel() {

    private val deviceLanguage: Flow<DeviceLanguage> = configRepository.getDeviceLanguage()

    private val tvSeriesState: StateFlow<TvSeriesState> = deviceLanguage.map { deviceLanguage ->
        TvSeriesState(
            onTheAir = tvSeriesRepository.onTheAirTvSeries(deviceLanguage).map { pagingData ->
                pagingData.filterCompleteInfo()
            },
            discover = tvSeriesRepository.discoverTvSeries(deviceLanguage),
            topRated = tvSeriesRepository.topRatedTvSeries(deviceLanguage),
            trending = tvSeriesRepository.trendingTvSeries(deviceLanguage),
            airingToday = tvSeriesRepository.airingTodayTvSeries(deviceLanguage)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), TvSeriesState.default)

    val tvScreenUiState: StateFlow<TvScreenUiState> = tvSeriesState.map { tvSeriesState ->
        TvScreenUiState(
            tvSeriesState = tvSeriesState,
            favourites = favouritesRepository.favouritesTvSeries(),
            recentlyBrowsed = recentlyBrowsedRepository.recentlyBrowsedTvSeries()
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), TvScreenUiState.default)

    private fun PagingData<TvSeries>.filterCompleteInfo(): PagingData<TvSeries> {
        return filter { tvSeries ->
            tvSeries.run {
                !backdropPath.isNullOrEmpty() &&
                        !posterPath.isNullOrEmpty() &&
                        title.isNotEmpty() &&
                        overview.isNotEmpty()
            }
        }
    }
}