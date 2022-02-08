package com.example.moviesapp.ui.screens.discoverMovies

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.moviesapp.model.*
import com.example.moviesapp.other.appendUrls
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

    private val config = configRepository.config
    private val availableMovieGenres = configRepository.movieGenres

    private val _sortType: MutableStateFlow<SortType> = MutableStateFlow(SortType.Popularity)
    val sortType: StateFlow<SortType> = _sortType.asStateFlow()

    private val _sortOrder: MutableStateFlow<SortOrder> = MutableStateFlow(SortOrder.Desc)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    private val _filterState: MutableStateFlow<FilterState> = MutableStateFlow(FilterState())
    val filterState: StateFlow<FilterState> = combine(
        _filterState,
        availableMovieGenres
    ) { filterState, genres ->
        filterState.copy(availableGenres = genres)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), FilterState())


    var movies: Flow<PagingData<Presentable>> =
        movieRepository.discoverMovies()
            .cachedIn(viewModelScope)
            .combine(config) { pagingData, config ->
                pagingData.map { movie -> movie.appendUrls(config) }
            }

    fun onSortTypeChange(sortType: SortType) {
        viewModelScope.launch {
            _sortType.emit(sortType)

            updateMovies()
        }
    }

    fun onSortOrderChange(sortOrder: SortOrder) {
        viewModelScope.launch {
            _sortOrder.emit(sortOrder)

            updateMovies()
        }
    }

    fun onFilterStateChange(filterState: FilterState) {
        viewModelScope.launch {
            _filterState.emit(filterState)

            updateMovies()
        }
    }

    private fun updateMovies() {
        val sortType = _sortType.value
        val sortOrder = _sortOrder.value
        val filterState = _filterState.value

        movies = movieRepository.discoverMovies(
            sortType = sortType,
            sortOrder = sortOrder,
            genresParam = GenresParam(filterState.selectedGenres),
            voteRange = filterState.voteRange.current,
            onlyWithPosters = filterState.showOnlyWithPoster
        )
            .cachedIn(viewModelScope)
            .combine(config) { pagingData, config ->
                pagingData.map { movie -> movie.appendUrls(config) }
            }
    }

}