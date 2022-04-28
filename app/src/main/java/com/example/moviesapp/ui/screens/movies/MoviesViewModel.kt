package com.example.moviesapp.ui.screens.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.use_case.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getDeviceLanguageUseCase: GetDeviceLanguageUseCase,
    private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,
    private val getDiscoverAllMoviesUseCase: GetDiscoverAllMoviesUseCase,
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase,
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val getFavouritesMoviesUseCase: GetFavouritesMoviesUseCase,
    private val getRecentlyBrowsedMoviesUseCase: GetRecentlyBrowsedMoviesUseCase
) : ViewModel() {

    private val deviceLanguage: Flow<DeviceLanguage> = getDeviceLanguageUseCase()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val moviesState: StateFlow<MoviesState> = deviceLanguage.mapLatest { deviceLanguage ->
        MoviesState(
            nowPlaying = getNowPlayingMoviesUseCase(deviceLanguage, true).cachedIn(viewModelScope),
            discover = getDiscoverAllMoviesUseCase(deviceLanguage).cachedIn(viewModelScope),
            upcoming = getUpcomingMoviesUseCase(deviceLanguage).cachedIn(viewModelScope),
            trending = getTrendingMoviesUseCase(deviceLanguage).cachedIn(viewModelScope),
            topRated = getTopRatedMoviesUseCase(deviceLanguage).cachedIn(viewModelScope),
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), MoviesState.default)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<MovieScreenUiState> = moviesState.mapLatest { moviesState ->
        MovieScreenUiState(
            moviesState = moviesState,
            favourites = getFavouritesMoviesUseCase().cachedIn(viewModelScope),
            recentlyBrowsed = getRecentlyBrowsedMoviesUseCase().cachedIn(viewModelScope)
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, MovieScreenUiState.default)
}