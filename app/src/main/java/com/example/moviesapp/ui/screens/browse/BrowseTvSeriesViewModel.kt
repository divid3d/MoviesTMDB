package com.example.moviesapp.ui.screens.browse

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.TvSeriesType
import com.example.moviesapp.repository.browsed.RecentlyBrowsedRepository
import com.example.moviesapp.repository.config.ConfigRepository
import com.example.moviesapp.repository.favourites.FavouritesRepository
import com.example.moviesapp.repository.tv.TvSeriesRepository
import com.example.moviesapp.ui.screens.destinations.BrowseTvSeriesScreenDestination
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

    private val navArgs: BrowseTvSeriesScreenArgs =
        BrowseTvSeriesScreenDestination.argsFrom(savedStateHandle)

    private val favouriteTvSeriesCount: StateFlow<Int> =
        favouritesRepository.getFavouriteTvSeriesCount()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), 0)

    val uiState: StateFlow<BrowseTvSeriesScreenUiState> = combine(
        deviceLanguage, favouriteTvSeriesCount
    ) { deviceLanguage, favouriteTvSeriesCount ->
        val tvSeries = when (navArgs.tvSeriesType) {
            TvSeriesType.TopRated -> tvSeriesRepository.topRatedTvSeries(deviceLanguage)
            TvSeriesType.AiringToday -> tvSeriesRepository.airingTodayTvSeries(deviceLanguage)
            TvSeriesType.Trending -> tvSeriesRepository.trendingTvSeries(deviceLanguage)
            TvSeriesType.Favourite -> favouritesRepository.favouritesTvSeries()
            TvSeriesType.RecentlyBrowsed -> recentlyBrowsedRepository.recentlyBrowsedTvSeries()
        }.map { data -> data.map { tvSeries -> tvSeries } }

        BrowseTvSeriesScreenUiState(
            selectedTvSeriesType = navArgs.tvSeriesType,
            tvSeries = tvSeries,
            favouriteMoviesCount = favouriteTvSeriesCount
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        BrowseTvSeriesScreenUiState.getDefault(navArgs.tvSeriesType)
    )

    fun onClearClicked() {
        recentlyBrowsedRepository.clearRecentlyBrowsedTvSeries()
    }
}