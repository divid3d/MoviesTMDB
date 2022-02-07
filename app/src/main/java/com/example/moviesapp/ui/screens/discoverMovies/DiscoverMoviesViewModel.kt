package com.example.moviesapp.ui.screens.discoverMovies

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.model.SortOrder
import com.example.moviesapp.model.SortType
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

    private val _sortType: MutableStateFlow<SortType> = MutableStateFlow(SortType.Popularity)
    val sortType: StateFlow<SortType> = _sortType.asStateFlow()

    private val _sortOrder: MutableStateFlow<SortOrder> = MutableStateFlow(SortOrder.Desc)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

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

    private fun updateMovies() {
        val sortType = _sortType.value
        val sortOrder = _sortOrder.value

        movies = movieRepository.discoverMovies(sortType = sortType, sortOrder = sortOrder)
            .cachedIn(viewModelScope)
            .combine(config) { pagingData, config ->
                pagingData.map { movie -> movie.appendUrls(config) }
            }
    }

}