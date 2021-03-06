package com.example.moviesapp.ui.screens.browse.movies

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.example.moviesapp.model.MovieType
import com.example.moviesapp.model.Presentable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Stable
data class BrowseMoviesScreenUiState(
    val selectedMovieType: MovieType,
    val movies: Flow<PagingData<Presentable>>,
    val favouriteMoviesCount: Int
) {
    companion object {
        fun getDefault(selectedMovieType: MovieType): BrowseMoviesScreenUiState {
            return BrowseMoviesScreenUiState(
                selectedMovieType = selectedMovieType,
                movies = emptyFlow(),
                favouriteMoviesCount = 0
            )
        }
    }
}