package com.example.moviesapp.ui.screens.tv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.use_case.interfaces.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TvSeriesViewModel @Inject constructor(
    private val getDeviceLanguageUseCaseImpl: GetDeviceLanguageUseCase,
    private val getOnTheAirTvSeriesUseCase: GetOnTheAirTvSeriesUseCase,
    private val getDiscoverAllTvSeriesUseCaseImpl: GetDiscoverAllTvSeriesUseCase,
    private val getTopRatedTvSeriesUseCase: GetTopRatedTvSeriesUseCase,
    private val getTrendingTvSeriesUseCase: GetTrendingTvSeriesUseCase,
    private val getAiringTodayTvSeriesUseCaseImpl: GetAiringTodayTvSeriesUseCase,
    private val getFavouriteTvSeriesUseCase: GetFavouritesTvSeriesUseCase,
    private val getRecentlyBrowsedTvSeriesUseCase: GetRecentlyBrowsedTvSeriesUseCase
) : ViewModel() {

    private val deviceLanguage: Flow<DeviceLanguage> = getDeviceLanguageUseCaseImpl()

    private val tvSeriesState: StateFlow<TvSeriesState> =
        deviceLanguage.mapLatest { deviceLanguage ->
            TvSeriesState(
                onTheAir = getOnTheAirTvSeriesUseCase(deviceLanguage, true)
                    .cachedIn(viewModelScope),
                discover = getDiscoverAllTvSeriesUseCaseImpl(deviceLanguage)
                    .cachedIn(viewModelScope),
                topRated = getTopRatedTvSeriesUseCase(deviceLanguage)
                    .cachedIn(viewModelScope),
                trending = getTrendingTvSeriesUseCase(deviceLanguage)
                    .cachedIn(viewModelScope),
                airingToday = getAiringTodayTvSeriesUseCaseImpl(deviceLanguage)
                    .cachedIn(viewModelScope)
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), TvSeriesState.default)

    val uiState: StateFlow<TvScreenUiState> = tvSeriesState.mapLatest { tvSeriesState ->
        TvScreenUiState(
            tvSeriesState = tvSeriesState,
            favourites = getFavouriteTvSeriesUseCase(),
            recentlyBrowsed = getRecentlyBrowsedTvSeriesUseCase()
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, TvScreenUiState.default)

}