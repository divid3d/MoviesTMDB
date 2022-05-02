package com.example.moviesapp.ui.screens.movies

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.example.moviesapp.model.DetailPresentable
import com.example.moviesapp.model.MovieFavourite
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.model.RecentlyBrowsedMovie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Stable
data class MovieScreenUiState(
    val moviesState: MoviesState,
    val favourites: Flow<PagingData<MovieFavourite>>,
    val recentlyBrowsed: Flow<PagingData<RecentlyBrowsedMovie>>
) {
    companion object {
        val default: MovieScreenUiState = MovieScreenUiState(
            moviesState = MoviesState.default,
            favourites = emptyFlow(),
            recentlyBrowsed = emptyFlow()
        )
    }
}

@Stable
data class MoviesState(
    val discover: Flow<PagingData<Presentable>>,
    val upcoming: Flow<PagingData<Presentable>>,
    val topRated: Flow<PagingData<Presentable>>,
    val trending: Flow<PagingData<Presentable>>,
    val nowPlaying: Flow<PagingData<DetailPresentable>>
) {
    companion object {
        val default: MoviesState = MoviesState(
            discover = emptyFlow(),
            upcoming = emptyFlow(),
            topRated = emptyFlow(),
            trending = emptyFlow(),
            nowPlaying = emptyFlow()
        )
    }
}