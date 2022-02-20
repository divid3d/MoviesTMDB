package com.example.moviesapp.ui.screens.discover

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviesapp.model.*
import com.example.moviesapp.repository.ConfigRepository
import com.example.moviesapp.repository.MovieRepository
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

    private val _sortType: MutableStateFlow<SortType> = MutableStateFlow(SortType.Popularity)
    val sortType: StateFlow<SortType> = _sortType.asStateFlow()

    private val _sortOrder: MutableStateFlow<SortOrder> = MutableStateFlow(SortOrder.Desc)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    private val _filterState: MutableStateFlow<MovieFilterState> =
        MutableStateFlow(MovieFilterState())
    val filterState: StateFlow<MovieFilterState> = combine(
        _filterState,
        availableMovieGenres,
        availableWatchProviders
    ) { filterState, genres, watchProviders ->
        filterState.copy(
            availableGenres = genres,
            availableWatchProviders = watchProviders
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), MovieFilterState())

    val movies: Flow<PagingData<Movie>> = combine(
        _filterState, _sortType, _sortOrder, deviceLanguage
    ) { filterState, type, order, deviceLanguage ->
        movieRepository.discoverMovies(
            deviceLanguage = deviceLanguage,
            sortType = type,
            sortOrder = order,
            genresParam = GenresParam(filterState.selectedGenres),
            watchProvidersParam = WatchProvidersParam(filterState.selectedWatchProviders),
            voteRange = filterState.voteRange.current,
            onlyWithPosters = filterState.showOnlyWithPoster,
            onlyWithScore = filterState.showOnlyWithScore,
            onlyWithOverview = filterState.showOnlyWithOverview,
            releaseDateRange = filterState.releaseDateRange
        )
    }.flattenMerge().cachedIn(viewModelScope)

    fun onSortTypeChange(sortType: SortType) {
        viewModelScope.launch {
            _sortType.emit(sortType)
        }
    }

    fun onSortOrderChange(sortOrder: SortOrder) {
        viewModelScope.launch {
            _sortOrder.emit(sortOrder)
        }
    }

    fun onFilterStateChange(filterState: MovieFilterState) {
        viewModelScope.launch {
            _filterState.emit(filterState)
        }
    }
}