package com.example.moviesapp.ui.screens.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.moviesapp.model.FavouriteType
import com.example.moviesapp.model.MovieFavourite
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.model.TvSeriesFavourite
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

    private val _selectedFavouriteType: MutableStateFlow<FavouriteType> =
        MutableStateFlow(FavouriteType.Movie)
    val selectedFavouriteType: StateFlow<FavouriteType> = _selectedFavouriteType.asStateFlow()

    private val favouriteMovies: Flow<PagingData<MovieFavourite>> =
        favouritesRepository.favouriteMovies().cachedIn(viewModelScope)

    private val favouriteTvSeries: Flow<PagingData<TvSeriesFavourite>> =
        favouritesRepository.favouritesTvSeries().cachedIn(viewModelScope)

    val favourites: Flow<PagingData<Presentable>> = combine(
        _selectedFavouriteType, favouriteMovies, favouriteTvSeries
    ) { type, movies, tvSeries ->
        when (type) {
            FavouriteType.Movie -> movies
            FavouriteType.TvSeries -> tvSeries
        }
    }.map { data -> data.map { tvSeries -> tvSeries } }.cachedIn(viewModelScope)

    fun onFavouriteTypeSelected(type: FavouriteType) {
        viewModelScope.launch {
            _selectedFavouriteType.emit(type)
        }
    }

}