package com.example.moviesapp.ui.screens.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.example.moviesapp.model.FavouriteType
import com.example.moviesapp.repository.FavouritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val favouritesRepository: FavouritesRepository
) : ViewModel() {

    private val _selectedFavouriteType: MutableStateFlow<FavouriteType> =
        MutableStateFlow(FavouriteType.Movie)

    val uiState: StateFlow<FavouritesScreenUiState> = _selectedFavouriteType.map { type ->
        val favourites = when (type) {
            FavouriteType.Movie -> favouritesRepository.favouriteMovies()
            FavouriteType.TvSeries -> favouritesRepository.favouritesTvSeries()
        }.map { data -> data.map { tvSeries -> tvSeries } }

        FavouritesScreenUiState(
            selectedFavouriteType = type,
            favourites = favourites
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, FavouritesScreenUiState.default)

    fun onFavouriteTypeSelected(type: FavouriteType) {
        viewModelScope.launch {
            _selectedFavouriteType.emit(type)
        }
    }
}