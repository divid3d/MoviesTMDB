package com.example.moviesapp.ui.screens.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.moviesapp.model.FavouriteType
import com.example.moviesapp.use_case.interfaces.GetFavouritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val getFavouritesUseCaseImpl: GetFavouritesUseCase
) : ViewModel() {

    private val _selectedFavouriteType: MutableStateFlow<FavouriteType> =
        MutableStateFlow(FavouriteType.Movie)

    val uiState: StateFlow<FavouritesScreenUiState> = _selectedFavouriteType.mapLatest { type ->
        val favourites = getFavouritesUseCaseImpl(type).cachedIn(viewModelScope)

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