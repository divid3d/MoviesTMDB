package com.example.moviesapp.ui.screens.movies

import androidx.paging.PagingData
import com.example.moviesapp.model.Movie
import com.example.moviesapp.model.MovieFavourite
import com.example.moviesapp.model.RecentlyBrowsedMovie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class MovieScreenUiState(
    val moviesState: MoviesState,
    val favourites: Flow<PagingData<MovieFavourite>>,
    val recentlyBrowsed: Flow<PagingData<RecentlyBrowsedMovie>>
) {
    companion object {
        val default: MovieScreenUiState
            get() = MovieScreenUiState(
                moviesState = MoviesState.default,
                favourites = emptyFlow(),
                recentlyBrowsed = emptyFlow()
            )
    }
}

data class MoviesState(
    val discover: Flow<PagingData<Movie>>,
    val upcoming: Flow<PagingData<Movie>>,
    val topRated: Flow<PagingData<Movie>>,
    val trending: Flow<PagingData<Movie>>,
    val nowPlaying: Flow<PagingData<Movie>>
) {
    companion object {
        val default: MoviesState
            get() = MoviesState(
                discover = emptyFlow(),
                upcoming = emptyFlow(),
                topRated = emptyFlow(),
                trending = emptyFlow(),
                nowPlaying = emptyFlow()
            )
    }
}