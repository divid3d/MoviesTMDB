package com.example.moviesapp.ui.screens.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.moviesapp.model.Config
import com.example.moviesapp.model.FavouriteType
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.other.appendUrls
import com.example.moviesapp.repository.ConfigRepository
import com.example.moviesapp.repository.FavouritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val configRepository: ConfigRepository,
    private val favouritesRepository: FavouritesRepository
) : ViewModel() {
    private val config: StateFlow<Config?> = configRepository.config

    private val _selectedFavouriteType: MutableStateFlow<FavouriteType> =
        MutableStateFlow(FavouriteType.Movie)
    val selectedFavouriteType: StateFlow<FavouriteType> = _selectedFavouriteType.asStateFlow()


    private val favouriteMovies: Flow<PagingData<Presentable>> =
        favouritesRepository.favouriteMovies()
            .cachedIn(viewModelScope)
            .combine(config) { pagingData, config ->
                pagingData.map { favouriteMovie -> favouriteMovie.appendUrls(config) }
            }

    private val favouriteTvSeries: Flow<PagingData<Presentable>> =
        favouritesRepository.favouritesTvSeries()
            .cachedIn(viewModelScope)
            .combine(config) { pagingData, config ->
                pagingData.map { favouriteTvSeries -> favouriteTvSeries.appendUrls(config) }
            }

    val favourites: Flow<PagingData<Presentable>> = combine(
        _selectedFavouriteType, favouriteMovies, favouriteTvSeries
    ) { type, movies, tvSeries ->
        when (type) {
            FavouriteType.Movie -> movies
            FavouriteType.TvSeries -> tvSeries
        }
    }.cachedIn(viewModelScope)

    fun onFavouriteTypeSelected(type: FavouriteType) {
        viewModelScope.launch {
            _selectedFavouriteType.emit(type)
        }
    }

}