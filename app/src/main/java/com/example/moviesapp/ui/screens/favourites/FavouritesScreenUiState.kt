package com.example.moviesapp.ui.screens.favourites

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.example.moviesapp.model.FavouriteType
import com.example.moviesapp.model.Presentable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Stable
data class FavouritesScreenUiState(
    val selectedFavouriteType: FavouriteType,
    val favourites: Flow<PagingData<Presentable>>,
) {
    companion object {
        val default: FavouritesScreenUiState
            get() = FavouritesScreenUiState(
                selectedFavouriteType = FavouriteType.Movie,
                favourites = emptyFlow()
            )
    }
}