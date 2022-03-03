package com.example.moviesapp.ui.screens.discover

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.model.*
import com.example.moviesapp.repository.config.ConfigRepository
import com.example.moviesapp.repository.movie.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class DiscoverMoviesViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val configRepository: ConfigRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val deviceLanguage: Flow<DeviceLanguage> = configRepository.getDeviceLanguage()
    private val availableMovieGenres = configRepository.getMovieGenres()
    private val availableWatchProviders = configRepository.getAllMoviesWatchProviders()

    private val sortInfo: MutableStateFlow<SortInfo> = MutableStateFlow(SortInfo.default)

    private val _filterState: MutableStateFlow<MovieFilterState> =
        MutableStateFlow(MovieFilterState.default)
    private val filterState: StateFlow<MovieFilterState> = combine(
        _filterState,
        availableMovieGenres,
        availableWatchProviders
    ) { filterState, genres, watchProviders ->
        filterState.copy(
            availableGenres = genres,
            availableWatchProviders = watchProviders
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), MovieFilterState.default)

    val uiState: StateFlow<DiscoverMoviesScreenUiState> = combine(
        deviceLanguage, sortInfo, filterState
    ) { deviceLanguage, sortInfo, filterState ->
        val movies = movieRepository.discoverMovies(
            deviceLanguage = deviceLanguage,
            sortType = sortInfo.sortType,
            sortOrder = sortInfo.sortOrder,
            genresParam = GenresParam(filterState.selectedGenres),
            watchProvidersParam = WatchProvidersParam(filterState.selectedWatchProviders),
            voteRange = filterState.voteRange.current,
            onlyWithPosters = filterState.showOnlyWithPoster,
            onlyWithScore = filterState.showOnlyWithScore,
            onlyWithOverview = filterState.showOnlyWithOverview,
            releaseDateRange = filterState.releaseDateRange
        )

        DiscoverMoviesScreenUiState(
            sortInfo = sortInfo,
            filterState = filterState,
            movies = movies
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(10),
        DiscoverMoviesScreenUiState.default
    )

    fun onSortTypeChange(sortType: SortType) {
        viewModelScope.launch {
            val currentSortInfo = sortInfo.value

            sortInfo.emit(currentSortInfo.copy(sortType = sortType))
        }
    }

    fun onSortOrderChange(sortOrder: SortOrder) {
        viewModelScope.launch {
            val currentSortInfo = sortInfo.value

            sortInfo.emit(currentSortInfo.copy(sortOrder = sortOrder))
        }
    }

    fun onFilterStateChange(state: MovieFilterState) {
        viewModelScope.launch {
            _filterState.emit(state)
        }
    }
}