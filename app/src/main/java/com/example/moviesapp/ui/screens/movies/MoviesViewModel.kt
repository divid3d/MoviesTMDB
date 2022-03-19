package com.example.moviesapp.ui.screens.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Movie
import com.example.moviesapp.repository.browsed.RecentlyBrowsedRepository
import com.example.moviesapp.repository.config.ConfigRepository
import com.example.moviesapp.repository.favourites.FavouritesRepository
import com.example.moviesapp.repository.movie.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val configRepository: ConfigRepository,
    private val favouritesRepository: FavouritesRepository,
    private val recentlyBrowsedRepository: RecentlyBrowsedRepository,
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val deviceLanguage: Flow<DeviceLanguage> = configRepository.getDeviceLanguage()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val moviesState: StateFlow<MoviesState> = deviceLanguage.mapLatest { deviceLanguage ->
        MoviesState(
            nowPlaying = movieRepository.nowPlayingMovies(deviceLanguage).mapLatest { pagingDate ->
                pagingDate.filterCompleteInfo()
            }.cachedIn(viewModelScope),
            discover = movieRepository.discoverMovies(deviceLanguage).cachedIn(viewModelScope),
            upcoming = movieRepository.upcomingMovies(deviceLanguage).cachedIn(viewModelScope),
            trending = movieRepository.trendingMovies(deviceLanguage).cachedIn(viewModelScope),
            topRated = movieRepository.topRatedMovies(deviceLanguage).cachedIn(viewModelScope),
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), MoviesState.default)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<MovieScreenUiState> = moviesState.mapLatest { moviesState ->
        MovieScreenUiState(
            moviesState = moviesState,
            favourites = favouritesRepository.favouriteMovies(),
            recentlyBrowsed = recentlyBrowsedRepository.recentlyBrowsedMovies()
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, MovieScreenUiState.default)

    private fun PagingData<Movie>.filterCompleteInfo(): PagingData<Movie> {
        return filter { tvSeries ->
            tvSeries.run {
                !backdropPath.isNullOrEmpty() &&
                        !posterPath.isNullOrEmpty() &&
                        title.isNotEmpty() &&
                        overview.isNotEmpty()
            }
        }
    }
}