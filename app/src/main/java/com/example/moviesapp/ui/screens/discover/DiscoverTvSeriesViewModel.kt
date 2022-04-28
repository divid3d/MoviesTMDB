package com.example.moviesapp.ui.screens.discover

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.SortOrder
import com.example.moviesapp.model.SortType
import com.example.moviesapp.use_case.GetAllTvSeriesWatchProvidersUseCase
import com.example.moviesapp.use_case.GetDeviceLanguageUseCase
import com.example.moviesapp.use_case.GetDiscoverTvSeriesUseCase
import com.example.moviesapp.use_case.GetTvSeriesGenresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class DiscoverTvSeriesViewModel @Inject constructor(
    private val getDeviceLanguageUseCase: GetDeviceLanguageUseCase,
    private val getTvSeriesGenresUseCase: GetTvSeriesGenresUseCase,
    private val getAllTvSeriesWatchProvidersUseCase: GetAllTvSeriesWatchProvidersUseCase,
    private val getDiscoverTvSeriesUseCase: GetDiscoverTvSeriesUseCase
) : ViewModel() {

    private val deviceLanguage: Flow<DeviceLanguage> = getDeviceLanguageUseCase()
    private val availableTvSeriesGenres = getTvSeriesGenresUseCase()
    private val availableWatchProviders = getAllTvSeriesWatchProvidersUseCase()

    private val sortInfo: MutableStateFlow<SortInfo> = MutableStateFlow(SortInfo.default)

    private val _filterState: MutableStateFlow<TvSeriesFilterState> =
        MutableStateFlow(TvSeriesFilterState.default)
    private val filterState: StateFlow<TvSeriesFilterState> = combine(
        _filterState,
        availableTvSeriesGenres,
        availableWatchProviders
    ) { filterState, genres, watchProviders ->
        filterState.copy(
            availableGenres = genres,
            availableWatchProviders = watchProviders
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, TvSeriesFilterState.default)

    val uiState: StateFlow<DiscoverTvSeriesScreenUiState> = combine(
        deviceLanguage, sortInfo, filterState
    ) { deviceLanguage, sortInfo, filterState ->
        val tvSeries = getDiscoverTvSeriesUseCase(
            sortInfo = sortInfo,
            filterState = filterState,
            deviceLanguage = deviceLanguage
        ).cachedIn(viewModelScope)

        DiscoverTvSeriesScreenUiState(
            sortInfo = sortInfo,
            filterState = filterState,
            tvSeries = tvSeries
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        DiscoverTvSeriesScreenUiState.default
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

    fun onFilterStateChange(state: TvSeriesFilterState) {
        viewModelScope.launch {
            _filterState.emit(state)
        }
    }
}