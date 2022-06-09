package com.example.moviesapp.ui.screens.discover.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.SortOrder
import com.example.moviesapp.model.SortType
import com.example.moviesapp.use_case.interfaces.GetAllMoviesWatchProvidersUseCase
import com.example.moviesapp.use_case.interfaces.GetDeviceLanguageUseCase
import com.example.moviesapp.use_case.interfaces.GetDiscoverMoviesUseCase
import com.example.moviesapp.use_case.interfaces.GetMovieGenresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class DiscoverMoviesViewModel @Inject constructor(
    private val getDeviceLanguageUseCaseImpl: GetDeviceLanguageUseCase,
    private val getMovieGenresUseCase: GetMovieGenresUseCase,
    private val getAllMoviesWatchProvidersUseCase: GetAllMoviesWatchProvidersUseCase,
    private val getDiscoverMoviesUseCaseImpl: GetDiscoverMoviesUseCase
) : ViewModel() {

    private val deviceLanguage: Flow<DeviceLanguage> = getDeviceLanguageUseCaseImpl()
    private val availableMovieGenres = getMovieGenresUseCase()
    private val availableWatchProviders = getAllMoviesWatchProvidersUseCase()

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
    }.stateIn(viewModelScope, SharingStarted.Eagerly, MovieFilterState.default)

    val uiState: StateFlow<DiscoverMoviesScreenUiState> = combine(
        deviceLanguage, sortInfo, filterState
    ) { deviceLanguage, sortInfo, filterState ->
        val movies = getDiscoverMoviesUseCaseImpl(
            sortInfo = sortInfo,
            filterState = filterState,
            deviceLanguage = deviceLanguage
        ).cachedIn(viewModelScope)

        DiscoverMoviesScreenUiState(
            sortInfo = sortInfo,
            filterState = filterState,
            movies = movies
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
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