package com.example.moviesapp.ui.screens.discover

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.moviesapp.model.*
import com.example.moviesapp.repository.ConfigRepository
import com.example.moviesapp.repository.TvSeriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class DiscoverTvSeriesViewModel @Inject constructor(
    private val tvSeriesRepository: TvSeriesRepository,
    private val configRepository: ConfigRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val deviceLanguage: Flow<DeviceLanguage> = configRepository.getDeviceLanguage()
    private val availableTvSeriesGenres = configRepository.getTvSeriesGenres()
    private val availableWatchProviders = configRepository.getAllTvSeriesWatchProviders()

    private val _sortType: MutableStateFlow<SortType> = MutableStateFlow(SortType.Popularity)
    val sortType: StateFlow<SortType> = _sortType.asStateFlow()

    private val _sortOrder: MutableStateFlow<SortOrder> = MutableStateFlow(SortOrder.Desc)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    private val _filterState: MutableStateFlow<TvSeriesFilterState> =
        MutableStateFlow(TvSeriesFilterState())
    val filterState: StateFlow<TvSeriesFilterState> = combine(
        _filterState,
        availableTvSeriesGenres,
        availableWatchProviders
    ) { filterState, genres, watchProviders ->
        filterState.copy(
            availableGenres = genres,
            availableWatchProviders = watchProviders
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), TvSeriesFilterState())


    var movies: Flow<PagingData<TvSeries>> = deviceLanguage.map { deviceLanguage ->
        tvSeriesRepository.discoverTvSeries(deviceLanguage = deviceLanguage)
    }.flattenMerge().cachedIn(viewModelScope)

    fun onSortTypeChange(sortType: SortType) {
        viewModelScope.launch {
            _sortType.emit(sortType)

            updateTvSeries()
        }
    }

    fun onSortOrderChange(sortOrder: SortOrder) {
        viewModelScope.launch {
            _sortOrder.emit(sortOrder)

            updateTvSeries()
        }
    }

    fun onFilterStateChange(filterState: TvSeriesFilterState) {
        viewModelScope.launch {
            _filterState.emit(filterState)

            updateTvSeries()
        }
    }

    private fun updateTvSeries() {
        val sortType = _sortType.value
        val sortOrder = _sortOrder.value
        val filterState = _filterState.value

        movies = deviceLanguage.map { deviceLanguage ->
            tvSeriesRepository.discoverTvSeries(
                deviceLanguage = deviceLanguage,
                sortType = sortType,
                sortOrder = sortOrder,
                genresParam = GenresParam(filterState.selectedGenres),
                watchProvidersParam = WatchProvidersParam(filterState.selectedWatchProviders),
                voteRange = filterState.voteRange.current,
                onlyWithPosters = filterState.showOnlyWithPoster,
                onlyWithScore = filterState.showOnlyWithScore,
                onlyWithOverview = filterState.showOnlyWithOverview,
                airDateRange = filterState.airDateRange
            )
        }.flattenMerge().map { data -> data.map { movie -> movie } }.cachedIn(viewModelScope)
    }

}