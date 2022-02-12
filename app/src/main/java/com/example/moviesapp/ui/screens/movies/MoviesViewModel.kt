package com.example.moviesapp.ui.screens.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.repository.FavouritesRepository
import com.example.moviesapp.repository.MovieRepository
import com.example.moviesapp.repository.RecentlyBrowsedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val favouritesRepository: FavouritesRepository,
    private val recentlyBrowsedRepository: RecentlyBrowsedRepository,
    private val movieRepository: MovieRepository
) : ViewModel() {

    val discover: Flow<PagingData<Presentable>> =
        movieRepository.discoverMovies()
            .cachedIn(viewModelScope)
            .map { data -> data.map { movie -> movie } }

    val upcoming: Flow<PagingData<Presentable>> =
        movieRepository.upcomingMovies()
            .cachedIn(viewModelScope)
            .map { data -> data.map { movie -> movie } }

    val trending: Flow<PagingData<Presentable>> =
        movieRepository.trendingMovies()
            .cachedIn(viewModelScope)
            .map { data -> data.map { movie -> movie } }

    val topRated: Flow<PagingData<Presentable>> =
        movieRepository.topRatedMovies()
            .cachedIn(viewModelScope)
            .map { data -> data.map { movie -> movie } }

    val nowPlaying: Flow<PagingData<Presentable>> =
        movieRepository.nowPlayingMovies()
            .cachedIn(viewModelScope)
            .map { pagingData ->
                pagingData
                    .filter { tvSeries ->
                        tvSeries.run {
                            !backdropPath.isNullOrEmpty() && !posterPath.isNullOrEmpty() && title.isNotEmpty() && overview.isNotEmpty()
                        }
                    }.map { movie -> movie }
            }


    val favourites: Flow<PagingData<Presentable>> =
        favouritesRepository.favouriteMovies()
            .cachedIn(viewModelScope)
            .map { data -> data.map { movie -> movie } }


    val recentBrowsed: Flow<PagingData<Presentable>> =
        recentlyBrowsedRepository.recentlyBrowsedMovies()
            .cachedIn(viewModelScope)
            .map { data -> data.map { movie -> movie } }

}